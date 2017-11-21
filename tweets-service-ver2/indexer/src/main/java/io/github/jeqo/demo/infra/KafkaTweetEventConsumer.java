package io.github.jeqo.demo.infra;

import io.opentracing.ActiveSpan;
import io.opentracing.References;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.contrib.kafka.TracingKafkaUtils;
import io.opentracing.util.GlobalTracer;
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
          final Tracer tracer = GlobalTracer.get();
          final SpanContext spanContext = TracingKafkaUtils.extractSpanContext(consumerRecord.headers(), tracer);

          final String key = consumerRecord.key().toString();
          final String value = consumerRecord.value();

          try (ActiveSpan ignored =
                   tracer.buildSpan("indexTweet")
                       .withTag("tweet_id", key)
                       .asChildOf(spanContext)
                       .startActive()) {
            elasticsearchTweetRepository.index(key, value);
          }

          kafkaConsumer.commitSync();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
