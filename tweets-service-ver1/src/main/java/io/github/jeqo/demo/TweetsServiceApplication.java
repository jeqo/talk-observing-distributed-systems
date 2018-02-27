package io.github.jeqo.demo;

import brave.Tracing;
import brave.opentracing.BraveTracer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TweetsRepository;
import io.github.jeqo.demo.domain.TweetsService;
import io.github.jeqo.demo.infra.JooqPostgresTweetsRepository;
import io.github.jeqo.demo.rest.TweetsResource;
import io.opentracing.Tracer;
import io.opentracing.contrib.jaxrs2.server.ServerTracingDynamicFeature;
import io.opentracing.contrib.metrics.prometheus.PrometheusMetricsReporter;
import io.opentracing.noop.NoopTracerFactory;
import io.opentracing.util.GlobalTracer;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.kafka11.KafkaSender;

import javax.ws.rs.container.DynamicFeature;

/**
 *
 */
public class TweetsServiceApplication extends Application<Configuration> {

  private final Config config = ConfigFactory.load();

  public static void main(String[] args) throws Exception {
    new TweetsServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "tweets-service-v1";
  }

  public void run(Configuration configuration, Environment environment) {
    //////////////////////////  METRICS INSTRUMENTATION ////////////////////////////////////
    final CollectorRegistry collectorRegistry = new CollectorRegistry();
    collectorRegistry.register(new DropwizardExports(environment.metrics()));

    final PrometheusMetricsReporter reporter =
        PrometheusMetricsReporter.newMetricsReporter()
            .withCollectorRegistry(collectorRegistry)
            .withConstLabel("service", getName())
            .build();

    /////////////////////////// TRACING INSTRUMENTATION ////////////////////////////////////
    final Tracer tracer = getTracer();
    final Tracer metricsTracer = io.opentracing.contrib.metrics.Metrics.decorate(tracer, reporter);
    GlobalTracer.register(metricsTracer);

    /////////////////////////// SERVICE CONFIGURATION //////////////////////////////////////

    final String jdbcUrl = config.getString("tweets.db-url");
    final String jdbcUsername = "postgres";
    final String jdbcPassword = "example";
    final TweetsRepository tweetsRepository = new JooqPostgresTweetsRepository(jdbcUrl, jdbcUsername, jdbcPassword);
    final TweetsService tweetsService = new TweetsService(tweetsRepository);
    final TweetsResource tweetsResource = new TweetsResource(tweetsService);

    /////////////////////////// HTTP APIS CONFIGURATION ////////////////////////////////////////
    environment.jersey().register(tweetsResource);

    final DynamicFeature tracing = new ServerTracingDynamicFeature.Builder(metricsTracer).build();
    environment.jersey().register(tracing);
    environment.admin()
        .addServlet("metrics", new MetricsServlet(collectorRegistry))
        .addMapping("/metrics");

  }

  private Tracer getTracer() {
    try {
      String tracingProvider = config.getString("tweets.tracing-provider");
      switch (tracingProvider) {
        case "JAEGER":
          return new com.uber.jaeger.Configuration(
              getName(),
              new com.uber.jaeger.Configuration.SamplerConfiguration("const", 1),
              new com.uber.jaeger.Configuration.ReporterConfiguration(
                  true,
                  "tracing-jaeger-agent",
                  6831,
                  1000,   // flush interval in milliseconds
                  10000)  /*max buffered Spans*/)
              .getTracer();
        case "ZIPKIN":
          // Configure a reporter, which controls how often spans are sent
          //   (the dependency is io.zipkin.reporter2:zipkin-sender-okhttp3)
          final KafkaSender sender = KafkaSender.create("zipkin-").toBuilder().autoBuild();
          final AsyncReporter<Span> spanReporter = AsyncReporter.create(sender);

          // Now, create a Brave tracing component with the service name you want to see in Zipkin.
          //   (the dependency is io.zipkin.brave:brave)
          final Tracing braveTracing =
              Tracing.newBuilder()
                  .localServiceName(getName())
                  .spanReporter(spanReporter)
                  .build();

          // use this to create an OpenTracing Tracer
          return BraveTracer.create(braveTracing);
        default:
          return NoopTracerFactory.create();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return NoopTracerFactory.create();
    }
  }
}
