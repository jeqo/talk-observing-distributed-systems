package io.github.jeqo.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

import static java.lang.System.out;

/**
 *
 */
public class TweetsProducer {

  private static ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) {
    final String tweetsEndpoint = System.getenv("TWEETS_ENDPOINT");

    if (tweetsEndpoint == null || tweetsEndpoint.trim().isEmpty()) {
      throw new RuntimeException("TWEETS_ENDPOINT env variable empty");
    }

    final Properties consumerProps = new Properties();

    final KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(consumerProps);

    kafkaConsumer.subscribe(Collections.singletonList("tweets"));

    final HttpClient httpClient = HttpClientBuilder.create().build();

    while (true) {
      final ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Long.MAX_VALUE);

      for (final ConsumerRecord<String, String> consumerRecord : consumerRecords) {
        final String value = consumerRecord.value();

        try {
          final JsonNode valueNode = objectMapper.readTree(value);
          final JsonNode payloadNode = valueNode.get("payload");
          final String payloadValue = payloadNode.toString();
          final HttpPost httpPost = new HttpPost();
          final HttpEntity entity = new NStringEntity(payloadValue, ContentType.APPLICATION_JSON);
          httpPost.setEntity(entity);
          HttpResponse response = httpClient.execute(httpPost);
          out.println("Response: " + response.getStatusLine().getStatusCode());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
