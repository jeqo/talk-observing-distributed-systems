package io.github.jeqo.demo.ot;

import brave.Tracing;
import brave.opentracing.BraveTracer;
import io.opentracing.Tracer;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.kafka11.KafkaSender;

/**
 *
 */
public class TracerBuilder {

  static Tracer build(String serviceName) {
    return getJaegerTracer(serviceName);
  }

  private static Tracer getJaegerTracer(String serviceName) {
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

  }

  private static Tracer getZipkinTracer(String serviceName) {
    // Configure a reporter, which controls how often spans are sent
    //   (the dependency is io.zipkin.reporter2:zipkin-sender-okhttp3)
    //final OkHttpSender sender = OkHttpSender.create("http://docker-vm:9411/api/v2/spans");
    final KafkaSender sender = KafkaSender.create("docker-vm:9092").toBuilder().autoBuild();
    final AsyncReporter<Span> spanReporter = AsyncReporter.create(sender);

    // Now, create a Brave tracing component with the service name you want to see in Zipkin.
    //   (the dependency is io.zipkin.brave:brave)
    final Tracing braveTracing =
        Tracing.newBuilder()
            .localServiceName(serviceName)
            .spanReporter(spanReporter)
            .build();

    // use this to create an OpenTracing Tracer
    return BraveTracer.create(braveTracing);

  }
}
