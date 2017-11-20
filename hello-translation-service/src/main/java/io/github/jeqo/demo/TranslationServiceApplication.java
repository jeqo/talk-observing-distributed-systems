package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.opentracing.Tracer;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.ServerTracingFeature;
import io.opentracing.mock.MockTracer;

/**
 *
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
    final Tracer tracer = getTracer();

    final DropWizardTracer dropWizardTracer = new DropWizardTracer(tracer);
    final ServerTracingFeature serverTracingFeature =
        new ServerTracingFeature.Builder(dropWizardTracer)
            .withTraceAnnotations()
            .build();
    environment.jersey().register(serverTracingFeature);

    final HelloTranslationRepository repository = new HelloTranslationRepository();
    final HelloTranslationResource resource = new HelloTranslationResource(repository);
    environment.jersey().register(resource);
  }

  private Tracer getTracer() {
    try {
      return new com.uber.jaeger.Configuration(
          "hello-translation-service",
          new com.uber.jaeger.Configuration.SamplerConfiguration("const", 1),
          new com.uber.jaeger.Configuration.ReporterConfiguration(
              true,
              "tracing-jaeger",
              6831,
              1000,   // flush interval in milliseconds
              10000)  /*max buffered Spans*/)
          .getTracer();
    } catch (Exception e) {
      e.printStackTrace();
      return new MockTracer();
    }
  }
}
