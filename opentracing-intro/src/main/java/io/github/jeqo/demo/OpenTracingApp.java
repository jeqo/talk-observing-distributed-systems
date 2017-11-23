package io.github.jeqo.demo;

import io.opentracing.ActiveSpan;
import io.opentracing.NoopTracerFactory;
import io.opentracing.References;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapExtractAdapter;
import io.opentracing.propagation.TextMapInjectAdapter;
import io.opentracing.util.GlobalTracer;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

/**
 *
 */
public class OpenTracingApp {

  public static void main(String[] args) throws InterruptedException {
    final Tracer tracer = getTracer();//NoopTracerFactory.create();
    GlobalTracer.register(tracer);

    final ActiveSpan span = tracer.buildSpan("main").startActive();

    span.setBaggageItem("globalKey", "globalValue");

    SpanContext spanContext = span.context();

    waitABit();

    operation1(spanContext);

    waitABit();


    span.close();

    anotherOperation(spanContext);

    Map<String, String> map = new HashMap<>();
    TextMap carrier = new TextMapInjectAdapter(map);
    tracer.inject(spanContext, Format.Builtin.TEXT_MAP, carrier);
    out.println(map.toString());

    anotherProcess(map);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        Thread.sleep(1000);
        out.println("Shouting down ...");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }));
  }

  private static void anotherProcess(Map<String, String> map) throws InterruptedException {
    final Tracer tracer = getTracer();
    TextMap carrier = new TextMapExtractAdapter(map);
    SpanContext spanContext = tracer.extract(Format.Builtin.TEXT_MAP, carrier);


    final ActiveSpan span =
        tracer.buildSpan("main2")
            .addReference(References.FOLLOWS_FROM, spanContext)
            .startActive();

    spanContext.baggageItems()
        .forEach(entry -> span.log("Doing something with: " + entry.getKey() + "->" + entry.getValue()));

    waitABit();

    span.close();
  }

  private static void anotherOperation(SpanContext spanContext) throws InterruptedException {
    final ActiveSpan span =
        GlobalTracer.get().buildSpan("operation1")
            .addReference(References.FOLLOWS_FROM, spanContext)
            .withTag("tag1", "value1")
            .withTag("tag2", "value2")
            .startActive();

    waitABit();

    span.close();
  }

  private static void operation1(SpanContext spanContext) throws InterruptedException {
    final ActiveSpan span =
        GlobalTracer.get().buildSpan("operation1")
            .asChildOf(spanContext)
            .withTag("tag1", "value1")
            .withTag("tag2", "value2")
            .startActive();
    //...
    waitABit();

    span.log("something happened");

    waitABit();

    span.close();
  }

  private static void waitABit() throws InterruptedException {
    Thread.sleep((long)(Math.random() * 1000));
  }


  private static Tracer getTracer() {
    try {
      return new com.uber.jaeger.Configuration(
          "opentracing-app",
          new com.uber.jaeger.Configuration.SamplerConfiguration("const", 1), //100%
          new com.uber.jaeger.Configuration.ReporterConfiguration(
              true,
              "docker-vm",
              6831,
              1000,   // flush interval in milliseconds
              10000)  /*max buffered Spans*/)
          .getTracer();
    } catch (Exception e) {
      e.printStackTrace();
      return NoopTracerFactory.create();
    }
  }
}
