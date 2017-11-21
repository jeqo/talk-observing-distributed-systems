package io.github.jeqo.demo;

import brave.Tracing;
import brave.opentracing.BraveTracer;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TweetsRepository;
import io.github.jeqo.demo.domain.TweetsService;
import io.github.jeqo.demo.infra.JooqPostgresTweetsRepository;
import io.github.jeqo.demo.rest.TweetsResource;
import io.opentracing.Tracer;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.ServerTracingFeature;
import io.opentracing.contrib.metrics.prometheus.PrometheusMetricsReporter;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

/**
 *
 */
public class TweetsServiceApplication extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new TweetsServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "tweets-service-v1";
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    final CollectorRegistry collectorRegistry = new CollectorRegistry();
    collectorRegistry.register(new DropwizardExports(environment.metrics()));
    environment.admin()
        .addServlet("metrics", new MetricsServlet(collectorRegistry))
        .addMapping("/metrics");

    final PrometheusMetricsReporter reporter = PrometheusMetricsReporter.newMetricsReporter()
        .withCollectorRegistry(collectorRegistry)
        .withConstLabel("service", getName())
        .build();

    final Tracer tracer = getTracer();
    final Tracer metricsTracer = io.opentracing.contrib.metrics.Metrics.decorate(tracer, reporter);
    GlobalTracer.register(metricsTracer);
    final DropWizardTracer dropWizardTracer = new DropWizardTracer(metricsTracer);
    final ServerTracingFeature serverTracingFeature =
        new ServerTracingFeature.Builder(dropWizardTracer)
            .withTraceAnnotations()
            .build();
    environment.jersey().register(serverTracingFeature);

    final String jdbcUrl = "jdbc:tracing:postgresql://tweets-db/postgres";
    final String jdbcUsername = "postgres";
    final String jdbcPassword = "example";
    final TweetsRepository tweetsRepository = new JooqPostgresTweetsRepository(jdbcUrl, jdbcUsername, jdbcPassword);
    final TweetsService tweetsService = new TweetsService(tweetsRepository);
    final TweetsResource tweetsResource = new TweetsResource(tweetsService, dropWizardTracer);
    environment.jersey().register(tweetsResource);
  }

  private Tracer getTracer() {
    try {
      return new com.uber.jaeger.Configuration(
          getName(),
          new com.uber.jaeger.Configuration.SamplerConfiguration("const", 1),
          new com.uber.jaeger.Configuration.ReporterConfiguration(
              true,
              "tracing-jaeger",
              6831,
              1000,   // flush interval in milliseconds
              10000)  /*max buffered Spans*/)
          .getTracer();
    } catch (Exception e) {
      e.printStackTrace();
      return new MockTracer();
    }
  }

  private Tracer getZipkinTracer() {
    try {
      // Configure a reporter, which controls how often spans are sent
      //   (the dependency is io.zipkin.reporter2:zipkin-sender-okhttp3)
      final OkHttpSender sender = OkHttpSender.create("http://tracing-zipkin:9411/api/v2/spans");
      final AsyncReporter<Span> spanReporter = AsyncReporter.create(sender);

      // Now, create a Brave tracing component with the service name you want to see in Zipkin.
      //   (the dependency is io.zipkin.brave:brave)
      final Tracing braveTracing = Tracing.newBuilder()
          .localServiceName("my-service")
          .spanReporter(spanReporter)
          .build();

      // use this to create an OpenTracing Tracer
      return BraveTracer.create(braveTracing);
    } catch (Exception e) {
      e.printStackTrace();
      return new MockTracer();
    }
  }
}
