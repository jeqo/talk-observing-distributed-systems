package io.github.jeqo.demo.ot;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.opentracing.References;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapExtractAdapter;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.StreamSupport;

/**
 *
 */
public class PongService extends AbstractActor {
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  //// (1) Tracer
  private final Tracer tracer = TracerBuilder.build("pong-service");

  private Random random = new Random();

  @Override
  public Receive createReceive() {
    return
        receiveBuilder()
            .match(PongMessage.class, pongMessage -> {
              //// (4) Extracting context from Text Map
              final Map<String, String> context = pongMessage.getContext();
              final SpanContext spanContext =
                  tracer.extract(
                      Format.Builtin.TEXT_MAP,
                      new TextMapExtractAdapter(context));

              //// (2) Start Span Manually
              final Span span =
                  tracer.buildSpan("pong")
                      .addReference(References.FOLLOWS_FROM, spanContext) //Reference to a parent span
                      .start();

              Optional<String> transactionIdOption =
                  StreamSupport.stream(spanContext.baggageItems().spliterator(), false)
                      .filter(entry -> entry.getKey().equalsIgnoreCase("transactionId"))
                      .findAny()
                      .map(Map.Entry::getValue);

              transactionIdOption.ifPresent(s -> span.log("pong receive for tId: " + s));
              log.info("pong");

              Thread.sleep(random.nextInt(1000));
              span.finish();
              //// (3) Closing Span manually
            })
            .build();
  }
}
