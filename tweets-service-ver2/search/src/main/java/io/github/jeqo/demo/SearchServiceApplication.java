package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TweetRepository;
import io.github.jeqo.demo.infra.ElasticsearchTweetRepository;
import io.github.jeqo.demo.rest.TweetsResource;
import io.opentracing.NoopTracerFactory;
import io.opentracing.Tracer;
import io.opentracing.contrib.elasticsearch.TracingHttpClientConfigCallback;
import io.opentracing.contrib.jaxrs2.server.ServerTracingDynamicFeature;
import io.opentracing.contrib.metrics.prometheus.PrometheusMetricsReporter;
import io.opentracing.util.GlobalTracer;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import javax.ws.rs.container.DynamicFeature;

/**
 *
 */
public class SearchServiceApplication extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new SearchServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "tweets-search-v2";
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    // Metrics Instrumentation
    final CollectorRegistry collectorRegistry = new CollectorRegistry();
    collectorRegistry.register(new DropwizardExports(environment.metrics()));
    environment.admin()
        .addServlet("metrics", new MetricsServlet(collectorRegistry))
        .addMapping("/metrics");

    final PrometheusMetricsReporter reporter = PrometheusMetricsReporter.newMetricsReporter()
        .withCollectorRegistry(collectorRegistry)
        .withConstLabel("service", getName())
        .build();

    // Tracing Instrumentation
    final Tracer tracer = getTracer();
    final Tracer metricsTracer = io.opentracing.contrib.metrics.Metrics.decorate(tracer, reporter);
    GlobalTracer.register(metricsTracer);

    final DynamicFeature tracing = new ServerTracingDynamicFeature.Builder(metricsTracer).build();
    environment.jersey().register(tracing);

    // Service Instantiation
    final HttpHost httpHost = new HttpHost("tweets-elasticsearch", 9200);
    final RestClientBuilder restClientBuilder =
        RestClient.builder(httpHost)
            .setHttpClientConfigCallback(new TracingHttpClientConfigCallback(metricsTracer));
    final RestClient restClient = restClientBuilder.build();
    final RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClient);
    final TweetRepository tweetRepository = new ElasticsearchTweetRepository(restHighLevelClient);
    final TweetsResource tweetsResource = new TweetsResource(tweetRepository);
    environment.jersey().register(tweetsResource);
  }

  private Tracer getTracer() {
    try {
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
    } catch (Exception e) {
      e.printStackTrace();
      return NoopTracerFactory.create();
    }
  }
}
