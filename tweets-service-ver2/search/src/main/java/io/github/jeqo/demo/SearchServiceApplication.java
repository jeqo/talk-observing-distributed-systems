package io.github.jeqo.demo;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TweetRepository;
import io.github.jeqo.demo.infra.ElasticsearchTweetRepository;
import io.github.jeqo.demo.rest.TweetsResource;
import io.github.jeqo.demo.util.TracingBuilder;
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

  private final Config config = ConfigFactory.load();

  public static void main(String[] args) throws Exception {
    new SearchServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "tweets-search-v2";
  }

  public void run(Configuration configuration, Environment environment) {
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
    final String tracingProvider = config.getString("tweets.tracing-provider");
    final Tracer tracer = TracingBuilder.getTracer(tracingProvider, getName());
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

}
