package io.github.jeqo.demo;

import io.opentracing.ActiveSpan;
import io.opentracing.References;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapExtractAdapter;
import io.opentracing.tag.Tags;

import java.util.Map;

/**
 *
 */
public class AnotherProcess {
  private final Tracer tracer;

  AnotherProcess(Tracer tracer) {
    this.tracer = tracer;
  }

  public void run(Map<String, String> map) {
    TextMap carrier = new TextMapExtractAdapter(map);
    SpanContext spanContext = tracer.extract(Format.Builtin.TEXT_MAP, carrier);

    ActiveSpan span = tracer.buildSpan("main")
        .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT)
        .addReference(References.FOLLOWS_FROM, spanContext)
        .startActive();

    waitABit();

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
