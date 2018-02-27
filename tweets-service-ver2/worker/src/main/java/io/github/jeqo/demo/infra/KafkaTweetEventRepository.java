package io.github.jeqo.demo.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jeqo.demo.domain.Tweet;
import io.github.jeqo.demo.domain.TweetEventRepository;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * Kafka Producer Repository
 */
public class KafkaTweetEventRepository implements TweetEventRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaTweetEventRepository.class);

  private final Producer<Long, String> kafkaProducer;
  private final ObjectMapper objectMapper;

  public KafkaTweetEventRepository(Producer<Long, String> kafkaProducer,
                                   ObjectMapper objectMapper) {
    this.kafkaProducer = kafkaProducer;
    this.objectMapper = objectMapper;
  }

  @Override
  public void put(Tweet tweet) {
    try {
      final Long key = tweet.id();
      final String value = objectMapper.writeValueAsString(tweet);
      final ProducerRecord<Long, String> producerRecord = new ProducerRecord<>("tweet-events", key, value);
      final RecordMetadata metadata = kafkaProducer.send(producerRecord).get();
      LOGGER.info("Kafka Producer Metadata: {}-{}:{}", metadata.topic(), metadata.partition(), metadata.offset());
    } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }
}
