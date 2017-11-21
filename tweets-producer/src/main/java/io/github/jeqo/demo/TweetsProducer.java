package io.github.jeqo.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

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

    final Properties consumerConfigs = new Properties();
    consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "tweets-source-kafka:9092");
    consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, System.getenv("GROUP_ID"));
    consumerConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerConfigs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

    final KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(consumerConfigs, new StringDeserializer(), new StringDeserializer());

    kafkaConsumer.subscribe(Collections.singletonList("tweets"));

    final HttpClient httpClient = HttpClientBuilder.create().build();

    while (true) {
      final ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Long.MAX_VALUE);

      for (final ConsumerRecord<String, String> consumerRecord : consumerRecords) {
        final String value = consumerRecord.value();

        try {
          final JsonNode valueNode = objectMapper.readTree(value);
          out.println(valueNode.toString());
          final JsonNode payloadNode = valueNode.get("payload");
          ObjectNode node = (ObjectNode) payloadNode;
          node.remove("lang");
          ((ObjectNode) node.get("entities")).remove("user_mentions");
          ((ObjectNode) node.get("entities")).remove("media");
          ((ObjectNode) node.get("entities")).remove("urls");
          ((ObjectNode) node.get("user")).remove("friends_count");
          ((ObjectNode) node.get("user")).remove("followers_count");
          ((ObjectNode) node.get("user")).remove("statuses_count");
          out.println(node.toString());
          final String payloadValue = node.toString();
          final HttpPost httpPost = new HttpPost(tweetsEndpoint);
          final HttpEntity entity = new NStringEntity(payloadValue, ContentType.APPLICATION_JSON);
          httpPost.setEntity(entity);
          HttpResponse response = httpClient.execute(httpPost);
          out.println("Response: " + response.getStatusLine().getStatusCode());
          out.println("Response: " + IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
        } catch (Exception e) {
          e.printStackTrace();
        }

      }

      kafkaConsumer.commitSync();
    }
  }
}
