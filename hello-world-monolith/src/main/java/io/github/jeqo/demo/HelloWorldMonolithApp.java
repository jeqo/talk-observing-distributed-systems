package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TranslationService;
import io.github.jeqo.demo.rest.GreetingResource;

/**
 * Hello Monolith Application to explain applications that all services are part of the same
 * application.
 */
public class HelloWorldMonolithApp extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new HelloWorldMonolithApp().run(args);
  }

  @Override
  public void run(Configuration configuration, Environment environment) throws Exception {
    // Preparing Translation Service
    final TranslationService translationService = new TranslationService();
    // Preparing Greeting Service and inject Translation
    final GreetingResource greetingService = new GreetingResource(translationService);

    // Register Greeting Service
    environment.jersey().register(greetingService);
  }
}
