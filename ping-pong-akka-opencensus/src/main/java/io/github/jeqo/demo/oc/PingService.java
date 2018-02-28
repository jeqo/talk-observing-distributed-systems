package io.github.jeqo.demo.oc;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.opencensus.common.Scope;
import io.opencensus.exporter.trace.zipkin.ZipkinTraceExporter;
import io.opencensus.trace.AttributeValue;
import io.opencensus.trace.Span;
import io.opencensus.trace.SpanContext;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.samplers.Samplers;

import java.util.Random;

/**
 *
 */
public class PingService extends AbstractActor {
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  private final ActorRef pongActor;

  private final Random random = new Random();

  //// (1) Tracer ////
  private final Tracer tracer = Tracing.getTracer();

  public PingService(ActorRef pongActor) {

    this.pongActor = pongActor;
  }

  @Override
  public Receive createReceive() {
    return
        receiveBuilder()
            .match(PingMessage.class, message -> {
              try (Scope ignored = //// (2) Scope = Span
                       tracer
                           .spanBuilder("ping")
                           .setSampler(Samplers.alwaysSample())
                           .startScopedSpan()) {
                final Span currentSpan = tracer.getCurrentSpan();
                currentSpan.addAnnotation("ping received"); //// (3) logs in-context
                log.info("ping");
                Thread.sleep(random.nextInt(1000));
                //// (4) Tags to add span-scoped metadata
                currentSpan.putAttribute("userId", AttributeValue.stringAttributeValue(message.getUserId()));

                //currentSpan.setBaggageItem("transactionId", message.getTransactionId());
                final SpanContext spanContext = currentSpan.getContext();
                log.info("traceId: " + spanContext.getTraceId().toString());
                log.info("spanId: " + spanContext.getSpanId().toString());
                //// (5) Context propagation using TextMap - No propagation
                pongActor.tell(new PongMessage(spanContext), pongActor);
//                currentSpan.end();
              }
            })
            .build();
  }
}
