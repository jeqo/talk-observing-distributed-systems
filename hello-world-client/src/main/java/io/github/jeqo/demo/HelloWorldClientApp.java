package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.opentracing.Tracer;
import io.opentracing.contrib.apache.http.client.TracingHttpClientBuilder;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.ServerTracingFeature;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;
import org.apache.http.client.HttpClient;

/**
 *
 */
public class HelloWorldClientApp extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    final HelloWorldClientApp app = new HelloWorldClientApp();
    app.run(args);
  }

  @Override
  public void run(Configuration configuration, Environment environment) throws Exception {

    final Tracer tracer = getTracer();
    GlobalTracer.register(tracer);

    final DropWizardTracer dropWizardTracer = new DropWizardTracer(tracer);
    final ServerTracingFeature serverTracingFeature =
        new ServerTracingFeature.Builder(dropWizardTracer)
            .withTraceAnnotations()
            .build();
    environment.jersey().register(serverTracingFeature);

    final HttpClient httpClient = new TracingHttpClientBuilder().build();

    final HelloWorldClient helloWorldClient = new HelloWorldClient(httpClient);
    final GreetingService greetingService = new GreetingService(helloWorldClient, dropWizardTracer);
    environment.jersey().register(greetingService);

  }

  private Tracer getTracer() {
    try {
      return new com.uber.jaeger.Configuration(
          "hello-world-client",
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
