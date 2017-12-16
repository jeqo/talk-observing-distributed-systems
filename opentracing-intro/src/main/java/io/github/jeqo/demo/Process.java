package io.github.jeqo.demo;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapInjectAdapter;
import io.opentracing.tag.Tags;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
class Process {

  private final Tracer tracer;

  Process(Tracer tracer) {
    this.tracer = tracer;
  }

  Map<String, String> run() {
    ActiveSpan span = tracer.buildSpan("main")
        .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER)
        .startActive();

    waitABit();

    childOperation();

    span.close();

    Map<String, String> map = new HashMap<>();
    TextMap carrier = new TextMapInjectAdapter(map);
    tracer.inject(span.context(), Format.Builtin.TEXT_MAP, carrier);

    return map;
  }

  private void childOperation() {
    ActiveSpan span = tracer.buildSpan("child")
        .withTag("transactionId", "1")
        .startActive();

    waitABit();

    span.log("Something happened");

    span.close();
  }

  private void waitABit() {
    try {
      Thread.sleep((long) (Math.random() * 1000));
    }catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}