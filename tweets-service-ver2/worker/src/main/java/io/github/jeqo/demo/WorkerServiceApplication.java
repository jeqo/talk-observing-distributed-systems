package io.github.jeqo.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TweetEventRepository;
import io.github.jeqo.demo.domain.TweetsService;
import io.github.jeqo.demo.infra.KafkaTweetEventRepository;
import io.github.jeqo.demo.rest.TweetsResource;
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
    final Properties producerConfigs = new Properties();
    producerConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
    producerConfigs.put(ProducerConfig.ACKS_CONFIG, "all");
    producerConfigs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    final Producer<Long, String> kafkaProducer =
        new KafkaProducer<>(producerConfigs, new LongSerializer(), new StringSerializer());
    final ObjectMapper objectMapper = environment.getObjectMapper();
    final TweetEventRepository tweetRepository = new KafkaTweetEventRepository(kafkaProducer, objectMapper);
    final TweetsService tweetsService = new TweetsService(tweetRepository);
    final TweetsResource tweetsResource = new TweetsResource(tweetsService);
    environment.jersey().register(tweetsResource);
  }
}
