package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.infra.HelloTranslationRepository;
import io.github.jeqo.demo.rest.HelloTranslationResource;
import io.opentracing.NoopTracerFactory;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

/**
 * Decoupled, independent Translation Service. To be Called by Hello World distributed Service
 */
public class TranslationServiceApplication extends Application<Configuration> {
  public static void main(String[] args) throws Exception {
    new TranslationServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "hello-translation-service";
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    // Tracing Instrumentation
    final Tracer tracer = getTracer();
    GlobalTracer.register(tracer);

    // Prepare Translation Repository
    final HelloTranslationRepository repository = new HelloTranslationRepository();
    // Prepare HTTP Resource and inject Repository
    final HelloTranslationResource resource = new HelloTranslationResource(repository);

    // Register HTTP Resource
    environment.jersey().register(resource);
  }

  private Tracer getTracer() {
    try {
      return new com.uber.jaeger.Configuration(
          "hello-translation-service",
          new com.uber.jaeger.Configuration.SamplerConfiguration("const", 1), //100%
          new com.uber.jaeger.Configuration.ReporterConfiguration(
              true,
              "tracing-jaeger-agent",
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
