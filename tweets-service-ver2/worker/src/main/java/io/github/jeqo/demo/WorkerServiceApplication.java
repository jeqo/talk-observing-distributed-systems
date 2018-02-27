package io.github.jeqo.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TweetEventRepository;
import io.github.jeqo.demo.domain.TweetsService;
import io.github.jeqo.demo.infra.KafkaTweetEventRepository;
import io.github.jeqo.demo.rest.TweetsResource;
import io.github.jeqo.demo.util.TracingBuilder;
import io.opentracing.Tracer;
import io.opentracing.contrib.jaxrs2.server.ServerTracingDynamicFeature;
import io.opentracing.contrib.kafka.TracingKafkaProducer;
import io.opentracing.contrib.metrics.prometheus.PrometheusMetricsReporter;
import io.opentracing.util.GlobalTracer;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.ws.rs.container.DynamicFeature;
import java.util.Properties;

/**
 *
 */
public class WorkerServiceApplication extends Application<Configuration> {

  private final Config config = ConfigFactory.load();

  public static void main(String[] args) throws Exception {
    new WorkerServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "tweets-worker-v2";
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

    final DynamicFeature tracing = new ServerTracingDynamicFeature.Builder(metricsTracer).build();
    environment.jersey().register(tracing);

    // Kafka Client Configuration
    final Properties producerConfigs = new Properties();
    producerConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "tweets-kafka:9092");
    producerConfigs.put(ProducerConfig.ACKS_CONFIG, "all");
    producerConfigs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    final KafkaProducer<Long, String> kafkaProducer =
        new KafkaProducer<>(producerConfigs, new LongSerializer(), new StringSerializer());
    final Producer<Long, String> tracingKafkaProducer =
        new TracingKafkaProducer<>(kafkaProducer, metricsTracer);

    // Service Instantiation
    final ObjectMapper objectMapper = environment.getObjectMapper();
    final TweetEventRepository tweetRepository = new KafkaTweetEventRepository(tracingKafkaProducer, objectMapper);
    final TweetsService tweetsService = new TweetsService(tweetRepository);
    final TweetsResource tweetsResource = new TweetsResource(tweetsService);
    environment.jersey().register(tweetsResource);
  }
}
