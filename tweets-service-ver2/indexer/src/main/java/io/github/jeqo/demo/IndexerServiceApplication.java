package io.github.jeqo.demo;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.infra.ElasticsearchTweetRepository;
import io.github.jeqo.demo.infra.KafkaTweetEventConsumer;
import io.github.jeqo.demo.util.TracingBuilder;
import io.opentracing.Tracer;
import io.opentracing.contrib.elasticsearch.TracingHttpClientConfigCallback;
import io.opentracing.contrib.kafka.TracingKafkaConsumer;
import io.opentracing.contrib.metrics.prometheus.PrometheusMetricsReporter;
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

  private final Config config = ConfigFactory.load();

  public static void main(String[] args) throws Exception {
    new IndexerServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "tweets-indexer-v2";
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
    final String tracingProvider = config.getString("tweets.tracing-provider");
    final Tracer tracer = TracingBuilder.getTracer(tracingProvider, getName());
    final Tracer metricsTracer = io.opentracing.contrib.metrics.Metrics.decorate(tracer, reporter);
    GlobalTracer.register(metricsTracer);

    // Elasticsearch Configuration
    final HttpHost httpHost = new HttpHost("tweets-elasticsearch", 9200);
    final RestClientBuilder restClientBuilder =
        RestClient.builder(httpHost).setHttpClientConfigCallback(new TracingHttpClientConfigCallback(metricsTracer));
    final RestClient restClient = restClientBuilder.build();
    final ElasticsearchTweetRepository elasticsearchRepository = new ElasticsearchTweetRepository(restClient);

    // Kafka Configuration
    final Properties consumerConfigs = new Properties();
    consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "tweets-kafka:9092");
    consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, getName());
    consumerConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerConfigs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    final KafkaConsumer<Long, String> kafkaConsumer = new KafkaConsumer<>(consumerConfigs, new LongDeserializer(), new StringDeserializer());
    final TracingKafkaConsumer<Long, String> tracingKafkaConsumer = new TracingKafkaConsumer<>(kafkaConsumer, metricsTracer);

    // Service Instantiation
    final Runnable kafkaTweetEventConsumer = new KafkaTweetEventConsumer(tracingKafkaConsumer, elasticsearchRepository);
    final ExecutorService executorService = environment.lifecycle().executorService("kafka-consumer").build();
    executorService.submit(kafkaTweetEventConsumer);
  }
}
