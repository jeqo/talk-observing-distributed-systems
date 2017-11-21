package io.github.jeqo.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TweetEventRepository;
import io.github.jeqo.demo.domain.TweetsService;
import io.github.jeqo.demo.infra.KafkaTweetEventRepository;
import io.github.jeqo.demo.rest.TweetsResource;
import io.opentracing.Tracer;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.ServerTracingFeature;
import io.opentracing.contrib.kafka.TracingKafkaProducer;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 *
 */
public class WorkerServiceApplication extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new WorkerServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "tweets-worker-service";
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    final Tracer tracer = getTracer();
    GlobalTracer.register(tracer);
    final DropWizardTracer dropWizardTracer = new DropWizardTracer(tracer);
    final ServerTracingFeature serverTracingFeature =
        new ServerTracingFeature.Builder(dropWizardTracer)
            .withTraceAnnotations()
            .build();
    environment.jersey().register(serverTracingFeature);

    final Properties producerConfigs = new Properties();
    producerConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
    producerConfigs.put(ProducerConfig.ACKS_CONFIG, "all");
    producerConfigs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    final KafkaProducer<Long, String> kafkaProducer =
        new KafkaProducer<>(producerConfigs, new LongSerializer(), new StringSerializer());
    final Producer<Long, String> tracingKafkaProducer =
        new TracingKafkaProducer<>(kafkaProducer, tracer);
    final ObjectMapper objectMapper = environment.getObjectMapper();
    final TweetEventRepository tweetRepository = new KafkaTweetEventRepository(tracingKafkaProducer, objectMapper);
    final TweetsService tweetsService = new TweetsService(tweetRepository);
    final TweetsResource tweetsResource = new TweetsResource(tweetsService, dropWizardTracer);
    environment.jersey().register(tweetsResource);
  }

  private Tracer getTracer() {
    try {
      return new com.uber.jaeger.Configuration(
          "tweets-worker-v2",
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
}
