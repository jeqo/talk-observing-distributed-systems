package io.github.jeqo.talk;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

/**
 *
 */
public class HelloWorldServerApp extends Application<Configuration> {
  public static void main(String[] args) throws Exception {
    final HelloWorldServerApp app = new HelloWorldServerApp();
    app.run(args);
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    environment.jersey().register(new HelloWorldService());
  }
}
