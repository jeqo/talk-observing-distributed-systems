package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.infra.HelloWorldClient;
import io.github.jeqo.demo.rest.GreetingService;
import io.opentracing.NoopTracerFactory;
import io.opentracing.Tracer;
import io.opentracing.contrib.apache.http.client.TracingHttpClientBuilder;
import io.opentracing.util.GlobalTracer;
import org.apache.http.client.HttpClient;

/**
 *
 */
public class HelloWorldClientApp extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new HelloWorldClientApp().run(args);
  }

  @Override
  public void run(Configuration configuration, Environment environment) throws Exception {
    //
    final Tracer tracer = getTracer();
    GlobalTracer.register(tracer);

    //
    final HttpClient httpClient = new TracingHttpClientBuilder().build();

    final HelloWorldClient helloWorldClient = new HelloWorldClient(httpClient);
    final GreetingService greetingService = new GreetingService(helloWorldClient);

    //
    environment.jersey().register(greetingService);
  }

  private Tracer getTracer() {
    try {
      return new com.uber.jaeger.Configuration(
          "hello-world-client",
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
