package io.github.jeqo.demo;

import io.opentracing.ActiveSpan;
import io.opentracing.References;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapInjectAdapter;
import io.opentracing.util.GlobalTracer;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

/**
 *
 */
public class Process {

  private final Tracer tracer;

  public Process(Tracer tracer) {
    this.tracer = tracer;
  }

  public Map<String, String> run() throws InterruptedException {
    ActiveSpan span = tracer.buildSpan("main").startActive();

    span.setBaggageItem("globalKey", "globalValue");

    SpanContext spanContext = span.context();

    waitABit();

    childOperation(spanContext);

    waitABit();

    span.close();

    followOperation(spanContext);

    Map<String, String> map = new HashMap<>();
    TextMap carrier = new TextMapInjectAdapter(map);
    tracer.inject(spanContext, Format.Builtin.TEXT_MAP, carrier);
    out.println(map.toString());
    return map;
  }

  private void waitABit() throws InterruptedException {
    Thread.sleep((long) (Math.random() * 1000));
  }

  private void childOperation(SpanContext spanContext) throws InterruptedException {
    final ActiveSpan span =
        tracer.buildSpan("childOperation")
            //.asChildOf(spanContext)
            .withTag("tag1", "value1")
            .withTag("tag2", "value2")
            .startActive();
    //...
    waitABit();

    span.log("something happened");

    waitABit();

    span.close();
  }

  private void followOperation(SpanContext spanContext) throws InterruptedException {
    final ActiveSpan span =
        tracer.buildSpan("followOperation")
            .addReference(References.FOLLOWS_FROM, spanContext)
            .withTag("tag1", "value1")
            .withTag("tag2", "value2")
            .startActive();

    waitABit();

    span.close();
  }
}
