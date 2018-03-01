package io.github.jeqo.demo.oc;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.opencensus.trace.Span;
import io.opencensus.trace.SpanContext;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;

import java.util.Random;

/**
 *
 */
public class PongService extends AbstractActor {
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  //// (1) Tracer
  private final Tracer tracer = Tracing.getTracer();

  private Random random = new Random();

  @Override
  public Receive createReceive() {
    return
        receiveBuilder()
            .match(PongMessage.class, pongMessage -> {
              final SpanContext spanContext = pongMessage.getSpanContext();

              //// (2) Start Span Manually
              final Span span =
                  tracer.spanBuilderWithRemoteParent("pong", spanContext)
                      //.addReference(References.FOLLOWS_FROM, spanContext) //Reference to a parent span
                      .startSpan();

              log.info("pong");

              Thread.sleep(random.nextInt(1000));
              span.end();
              //// (3) Closing Span manually
            })
            .build();
  }
}
