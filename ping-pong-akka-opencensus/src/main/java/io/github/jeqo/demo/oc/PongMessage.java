package io.github.jeqo.demo.oc;

import io.opencensus.trace.SpanContext;

import java.io.Serializable;

/**
 *
 */
class PongMessage implements Serializable {

  private final SpanContext spanContext;

  PongMessage(SpanContext spanContext) {
    this.spanContext = spanContext;
  }

  SpanContext getSpanContext() {
    return spanContext;
  }
}
