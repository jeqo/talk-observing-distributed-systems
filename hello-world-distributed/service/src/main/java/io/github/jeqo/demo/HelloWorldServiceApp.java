package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.infra.HelloTranslationClient;
import io.github.jeqo.demo.rest.HelloWorldResource;
import io.opentracing.NoopTracerFactory;
import io.opentracing.Tracer;
import io.opentracing.contrib.apache.http.client.TracingHttpClientBuilder;
import io.opentracing.contrib.jaxrs2.server.ServerTracingDynamicFeature;
import io.opentracing.util.GlobalTracer;
import org.apache.http.client.HttpClient;

import javax.ws.rs.container.DynamicFeature;

/**
 * Hello World Distributed version with Translation Service contained as a Service. In this version
 * the Client will have to call Hello World Service and call Translation Service at the end.
 */
public class HelloWorldServiceApp extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    final HelloWorldServiceApp app = new HelloWorldServiceApp();
    app.run(args);
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    // Preparing Tracer
    final Tracer tracer = getTracer();
    GlobalTracer.register(tracer); //Register on Global Scope

    final DynamicFeature tracing = new ServerTracingDynamicFeature.Builder(tracer).build();
    environment.jersey().register(tracing);

    // Preparing Http Client
    final String baseUrl = "http://hello-translation-service:8080/translation";
    final HttpClient httpClient = new TracingHttpClientBuilder().withTracer(tracer).build();

    // Preparing Translation Service Client
    final HelloTranslationClient translationClient = new HelloTranslationClient(httpClient, baseUrl);
    // Preparing Hello World Resource and inject Translation Client
    final HelloWorldResource resource = new HelloWorldResource(translationClient);

    // Register Hello World Resource
    environment.jersey().register(resource);
  }

  private Tracer getTracer() {
    try {
      return new com.uber.jaeger.Configuration(
          "hello-world-service",
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
