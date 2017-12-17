package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.infra.HelloTranslationRepository;
import io.github.jeqo.demo.rest.HelloTranslationResource;

/**
 * Decoupled, independent Translation Service. To be Called by Hello World distributed Service
 */
public class TranslationServiceApp extends Application<Configuration> {
  public static void main(String[] args) throws Exception {
    new TranslationServiceApp().run(args);
  }

  @Override
  public String getName() {
    return "hello-translation-service";
  }

  public void run(Configuration configuration, Environment environment) {
    // Dependency Injection
    final HelloTranslationRepository repository = new HelloTranslationRepository();
    final HelloTranslationResource resource = new HelloTranslationResource(repository);

    // Register HTTP Resource
    environment.jersey().register(resource);
  }
}
