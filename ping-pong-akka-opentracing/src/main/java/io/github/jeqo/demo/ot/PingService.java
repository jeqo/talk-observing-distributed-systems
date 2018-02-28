package io.github.jeqo.demo.ot;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.opentracing.Scope;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapInjectAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 */
public class PingService extends AbstractActor {
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  private final ActorRef pongService;

  private final Random random = new Random();

  //// (1) Tracer ////
  private final Tracer tracer = TracerBuilder.build("ping-service");

  public PingService(ActorRef pongService) {
    this.pongService = pongService;
  }

  @Override
  public Receive createReceive() {
    return
        receiveBuilder()
            .match(PingMessage.class, message -> {
              try (Scope scope = //// (2) Scope = Span
                       tracer
                           .buildSpan("ping")
                           .startActive(true)) {
                scope.span().log("ping received"); //// (3) logs in-context
                log.info("ping");
                Thread.sleep(random.nextInt(1000));

                scope.span() //// (4) Tags to add span-scoped metadata
                    .setTag("userId", message.getUserId());

                scope.span().setBaggageItem("transactionId", message.getTransactionId());
                final SpanContext spanContext = scope.span().context();

                //// (5) Context propagation using TextMap
                final Map<String, String> context = new HashMap<>();
                tracer.inject(
                    spanContext,
                    Format.Builtin.TEXT_MAP,
                    new TextMapInjectAdapter(context));
                pongService.tell(new PongMessage(context), pongService);
              }
            })
            .build();
  }
}
