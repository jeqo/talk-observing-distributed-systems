package io.github.jeqo.demo;

import io.opentracing.ActiveSpan;
import io.opentracing.References;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapExtractAdapter;

import java.util.Map;

/**
 *
 */
public class AnotherProcess {
  private final Tracer tracer;

  public AnotherProcess(Tracer tracer) {
    this.tracer = tracer;
  }

  void process(Map<String, String> map) throws InterruptedException {
    TextMap carrier = new TextMapExtractAdapter(map);
    SpanContext spanContext = tracer.extract(Format.Builtin.TEXT_MAP, carrier);


    final ActiveSpan span =
        tracer.buildSpan("main")
            .addReference(References.FOLLOWS_FROM, spanContext)
            .startActive();

    spanContext.baggageItems()
        .forEach(entry -> span.log("Doing something with: " + entry.getKey() + "->" + entry.getValue()));

    waitABit();

    span.close();
  }

  private void waitABit() throws InterruptedException {
    Thread.sleep((long) (Math.random() * 1000));
  }
}
