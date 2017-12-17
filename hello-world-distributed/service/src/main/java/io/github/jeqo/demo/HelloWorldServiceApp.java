package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.infra.HelloTranslationClient;
import io.github.jeqo.demo.rest.HelloWorldResource;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Hello World Distributed version with Translation Service contained as a Service. In this version
 * the Client will have to call Hello World Service and call Translation Service at the end.
 */
public class HelloWorldServiceApp extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    final HelloWorldServiceApp app = new HelloWorldServiceApp();
    app.run(args);
  }

  public void run(Configuration configuration, Environment environment) {
    // Preparing Http Client
    final HttpClient httpClient = HttpClientBuilder.create().build();

    // Dependency Injection
    final HelloTranslationClient translationClient = new HelloTranslationClient(httpClient);
    final HelloWorldResource resource = new HelloWorldResource(translationClient);

    // Register Hello World Resource
    environment.jersey().register(resource);
  }
}
