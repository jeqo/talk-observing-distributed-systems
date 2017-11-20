package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.opentracing.Tracer;
import io.opentracing.contrib.apache.http.client.TracingHttpClientBuilder;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.ServerTracingFeature;
import io.opentracing.mock.MockTracer;
import org.apache.http.client.HttpClient;

/**
 *
 */
public class HelloWorldServerApp extends Application<Configuration> {
  public static void main(String[] args) throws Exception {
    final HelloWorldServerApp app = new HelloWorldServerApp();
    app.run(args);
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    final Tracer tracer = getTracer();

    final DropWizardTracer dropWizardTracer = new DropWizardTracer(tracer);
    final ServerTracingFeature serverTracingFeature =
        new ServerTracingFeature.Builder(dropWizardTracer)
            .withTraceAnnotations()
            .build();
    environment.jersey().register(serverTracingFeature);

    final String baseUrl = "http://hello-translation-service:8080/translation";
    HttpClient httpClient = new TracingHttpClientBuilder().withTracer(tracer).build();

    final HelloTranslationClient translationClient = new HelloTranslationClient(httpClient, baseUrl);
    final HelloWorldService component = new HelloWorldService(translationClient, dropWizardTracer);
    environment.jersey().register(component);
  }

  private Tracer getTracer() {
    try {
      return new com.uber.jaeger.Configuration(
          "hello-world-service",
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
