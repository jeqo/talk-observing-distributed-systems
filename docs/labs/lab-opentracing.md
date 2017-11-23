## Lab OpenTracing

```java
public class OpenTracingApp {

  public static void main(String[] args) throws InterruptedException {
    Process process = new Process();
    process.run();

    Thread.sleep(2000);
    out.println("Shouting down ...");
  }

}
```

```java
class Process {

  void run() {
    
  }

  private void waitABit() {
    try {
      Thread.sleep((long) (Math.random() * 1000));
    }catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
```

### Instantiate Tracer

* Check Tracer Builder

```java
class TracerBuilder {

  private static Tracer getNoopTracer() {
    return NoopTracerFactory.create();
  }

  static Tracer getJaegerTracer(String serviceName) {
    try {
      return new com.uber.jaeger.Configuration(
          serviceName,
          new com.uber.jaeger.Configuration.SamplerConfiguration("const", 1),
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
```

* Inject Tracer to `Process`

```java
public class OpenTracingApp {

  public static void main(String[] args) throws InterruptedException {
    Process process = new Process(TracerBuilder.getJaegerTracer("Process"));
    process.run();

    Thread.sleep(2000);
    out.println("Shouting down ...");
  }

}
```

```java
class Process {

  private final Tracer tracer;

  Process(Tracer tracer) {
    this.tracer = tracer;
  }
  
  void run() {
    
  }

  private void waitABit() {
    try {
      Thread.sleep((long) (Math.random() * 1000));
    }catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
```

### Spans

* Create Parent (Main) Span

```java
class Process {

  private final Tracer tracer;

  Process(Tracer tracer) {
    this.tracer = tracer;
  }

  void run() throws InterruptedException {
    ActiveSp1an span = tracer.buildSpan("main").startActive();

    waitABit();

    span.close();

  }

  private void waitABit() throws InterruptedException {
    Thread.sleep((long) (Math.random() * 1000));
  }
}
```

* Create a Child Span

```java
class Process {

  private final Tracer tracer;

  Process(Tracer tracer) {
    this.tracer = tracer;
  }

  void run() throws InterruptedException {
    ActiveSp1an span = tracer.buildSpan("main").startActive();

    waitABit();

    span.close();

  }

  private void waitABit() throws InterruptedException {
    Thread.sleep((long) (Math.random() * 1000));
  }

  private void childOperation() throws InterruptedException {
    final ActiveSpan span =
        tracer.buildSpan("childOperation")
              .startActive();
    //...
    waitABit();

    span.close();
  }
}
```

### Tags and Logs


```java
class Process {

  private final Tracer tracer;

  Process(Tracer tracer) {
    this.tracer = tracer;
  }

  void run() throws InterruptedException {
    ActiveSpan span = tracer.buildSpan("main")
             .withTag("tag1", "value1")
             .startActive();

    waitABit();

    childOperation();

    waitABit();

    span.close();
  }

  private void waitABit() throws InterruptedException {
    Thread.sleep((long) (Math.random() * 1000));
  }

  private void childOperation() throws InterruptedException {
    final ActiveSpan span =
        tracer.buildSpan("childOperation")
            //.asChildOf(spanContext)
            .withTag("tag2", "value2")
            .startActive();
    //...
    waitABit();

    span.log("something happened");

    waitABit();

    span.close();
  }
}
```


### Add Another Process

```java
public class OpenTracingApp {

  public static void main(String[] args) throws InterruptedException {
    Process process = new Process(TracerBuilder.getJaegerTracer("Process"));
    process.run();

    AnotherProcess anotherProcess = new AnotherProcess(TracerBuilder.getJaegerTracer("AnotherProcess"));
    anotherProcess.process();

    Thread.sleep(2000);
    out.println("Shouting down ...");
  }

}
```

```java
class AnotherProcess {
  private final Tracer tracer;

  AnotherProcess(Tracer tracer) {
    this.tracer = tracer;
  }

  void process() throws InterruptedException {
    final ActiveSpan span =
        tracer.buildSpan("main")
              .startActive();

    waitABit();

    span.close();
  }

  private void waitABit() throws InterruptedException {
    Thread.sleep((long) (Math.random() * 1000));
  }
}
```

### Propagate Context

* Inject/Extract
* Carrier/Format

```java
public class OpenTracingApp {

  public static void main(String[] args) throws InterruptedException {
    Process process = new Process(TracerBuilder.getJaegerTracer("Process"));
    Map<String, String> map = process.run();

    AnotherProcess anotherProcess = new AnotherProcess(TracerBuilder.getJaegerTracer("AnotherProcess"));
    anotherProcess.process(map);

    Thread.sleep(2000);
    out.println("Shouting down ...");
  }

}
```


```java
class Process {

  private final Tracer tracer;

  Process(Tracer tracer) {
    this.tracer = tracer;
  }

  Map<String, String> run() throws InterruptedException {
    ActiveSpan span = tracer.buildSpan("main").startActive();

    waitABit();

    childOperation();

    waitABit();

    span.close();

    //Inject Context into TextMap-Format Carrier
    Map<String, String> map = new HashMap<>();
    TextMap carrier = new TextMapInjectAdapter(map);
    tracer.inject(span.context(), Format.Builtin.TEXT_MAP, carrier);
    out.println(map.toString());
    return map;
  }
  //...
}
```

* Reference: Follows From

```java
class AnotherProcess {
  private final Tracer tracer;

  AnotherProcess(Tracer tracer) {
    this.tracer = tracer;
  }

  void process(Map<String, String> map) throws InterruptedException {
    //Extract SpanContext from TextMap-Format Carrier
    TextMap carrier = new TextMapExtractAdapter(map);
    SpanContext spanContext = tracer.extract(Format.Builtin.TEXT_MAP, carrier);


    final ActiveSpan span =
        tracer.buildSpan("main")
            .addReference(References.FOLLOWS_FROM, spanContext)
            .startActive();

    waitABit();

    span.close();
  }

  private void waitABit() throws InterruptedException {
    Thread.sleep((long) (Math.random() * 1000));
  }
}
```

* Baggage Items

```java
class Process {

  private final Tracer tracer;

  Process(Tracer tracer) {
    this.tracer = tracer;
  }

  Map<String, String> run() throws InterruptedException {
    ActiveSpan span = tracer.buildSpan("main").startActive();

    span.setBaggageItem("globalKey", "globalValue");

    SpanContext spanContext = span.context();

    waitABit();

    childOperation(spanContext);

    waitABit();

    span.close();

    followOperation(spanContext);

    //Inject Context into TextMap-Format Carrier
    Map<String, String> map = new HashMap<>();
    TextMap carrier = new TextMapInjectAdapter(map);
    tracer.inject(spanContext, Format.Builtin.TEXT_MAP, carrier);
    out.println(map.toString());
    return map;
  }
  //...
}
```

```java
class AnotherProcess {
  private final Tracer tracer;

  AnotherProcess(Tracer tracer) {
    this.tracer = tracer;
  }

  void process(Map<String, String> map) throws InterruptedException {
    //Extract SpanContext from TextMap-Format Carrier
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
```