package io.github.jeqo.demo.infra;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Collections;

/**
 *
 */
public class KafkaTweetEventConsumer implements Runnable {

  private final Consumer<Long, String> kafkaConsumer;
  private final ElasticsearchTweetRepository elasticsearchTweetRepository;

  public KafkaTweetEventConsumer(Consumer<Long, String> kafkaConsumer,
                                 ElasticsearchTweetRepository elasticsearchTweetRepository) {
    this.kafkaConsumer = kafkaConsumer;
    this.elasticsearchTweetRepository = elasticsearchTweetRepository;
  }

  public void run() {
    try {
      kafkaConsumer.subscribe(Collections.singletonList("tweet-events"));

      while (true) {
        final ConsumerRecords<Long, String> consumerRecords = kafkaConsumer.poll(Long.MAX_VALUE);

        for (final ConsumerRecord<Long, String> consumerRecord : consumerRecords) {
          final String key = consumerRecord.key().toString();
          final String value = consumerRecord.value();

          elasticsearchTweetRepository.index(key, value);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
