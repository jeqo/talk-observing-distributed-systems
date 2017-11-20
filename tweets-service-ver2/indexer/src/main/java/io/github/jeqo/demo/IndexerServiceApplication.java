package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.infra.ElasticsearchTweetRepository;
import io.github.jeqo.demo.infra.KafkaTweetEventConsumer;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.Consumer;
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
    return "tweets-indexer-service";
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    final Properties consumerConfigs = new Properties();
    consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
    consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, getName());
    consumerConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerConfigs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    final Consumer<Long, String> kafkaConsumer = new KafkaConsumer<>(consumerConfigs, new LongDeserializer(), new StringDeserializer());
    final HttpHost httpHost = new HttpHost("tweets-elasticsearch", 9200);
    final RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
    final RestClient restClient = restClientBuilder.build();
    final ElasticsearchTweetRepository elasticsearchRepository = new ElasticsearchTweetRepository(restClient);
    final Runnable kafkaTweetEventConsumer = new KafkaTweetEventConsumer(kafkaConsumer, elasticsearchRepository);
    final ExecutorService executorService = environment.lifecycle().executorService("kafka-consumer").build();
    executorService.submit(kafkaTweetEventConsumer);
  }
}
