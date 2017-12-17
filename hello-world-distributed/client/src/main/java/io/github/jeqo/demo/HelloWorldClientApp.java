package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.infra.HelloWorldClient;
import io.github.jeqo.demo.rest.GreetingService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 */
public class HelloWorldClientApp extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new HelloWorldClientApp().run(args);
  }

  @Override
  public void run(Configuration configuration, Environment environment) {
    // Instantiate Http Client
    final HttpClient httpClient = HttpClientBuilder.create().build();

    // Dependency Injection
    final HelloWorldClient helloWorldClient = new HelloWorldClient(httpClient);
    final GreetingService greetingService = new GreetingService(helloWorldClient);

    // Register Greeting Service
    environment.jersey().register(greetingService);
  }
}
