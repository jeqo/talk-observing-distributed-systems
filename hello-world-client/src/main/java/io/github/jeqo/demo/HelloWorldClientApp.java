package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

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
    final HelloWorldClient helloWorldClient = new HelloWorldClient();
    final GreetingService greetingService = new GreetingService(helloWorldClient);
    environment.jersey().register(greetingService);
  }
}
