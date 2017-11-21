package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.infra.ElasticsearchTweetRepository;
import io.github.jeqo.demo.infra.KafkaTweetEventConsumer;
import io.opentracing.Tracer;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.ServerTracingFeature;
import io.opentracing.contrib.elasticsearch.TracingHttpClientConfigCallback;
import io.opentracing.contrib.kafka.TracingKafkaConsumer;
import io.opentracing.contrib.metrics.prometheus.PrometheusMetricsReporter;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 *
 */
public class IndexerServiceApplication extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new IndexerServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "tweets-indexer-v2";
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    // INSTRUMENTATION
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
    final DropWizardTracer dropWizardTracer = new DropWizardTracer(metricsTracer);
    final ServerTracingFeature serverTracingFeature =
        new ServerTracingFeature.Builder(dropWizardTracer)
            .withTraceAnnotations()
            .build();
    environment.jersey().register(serverTracingFeature);

    final HttpHost httpHost = new HttpHost("tweets-elasticsearch", 9200);
    final RestClientBuilder restClientBuilder =
        RestClient.builder(httpHost).setHttpClientConfigCallback(new TracingHttpClientConfigCallback(metricsTracer));
    final RestClient restClient = restClientBuilder.build();
    final ElasticsearchTweetRepository elasticsearchRepository = new ElasticsearchTweetRepository(restClient);

    final Properties consumerConfigs = new Properties();
    consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "tweets-kafka:9092");
    consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, getName());
    consumerConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerConfigs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    final KafkaConsumer<Long, String> kafkaConsumer = new KafkaConsumer<>(consumerConfigs, new LongDeserializer(), new StringDeserializer());
    final TracingKafkaConsumer<Long, String> tracingKafkaConsumer = new TracingKafkaConsumer<>(kafkaConsumer, metricsTracer);
    final Runnable kafkaTweetEventConsumer = new KafkaTweetEventConsumer(tracingKafkaConsumer, elasticsearchRepository);
    final ExecutorService executorService = environment.lifecycle().executorService("kafka-consumer").build();
    executorService.submit(kafkaTweetEventConsumer);
  }

  private Tracer getTracer() {
    try {
      return new com.uber.jaeger.Configuration(
          getName(),
          new com.uber.jaeger.Configuration.SamplerConfiguration("const", 1), // 100%
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
}
