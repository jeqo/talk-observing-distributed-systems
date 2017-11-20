package io.github.jeqo.demo;

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

    final String baseUrl = "http://hello-translation-service:8080/translation";
    final HelloTranslationClient translationClient = new HelloTranslationClient(baseUrl);
    final HelloWorldService component = new HelloWorldService(translationClient);
    environment.jersey().register(component);
  }
}
