package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

/**
 *
 */
public class TranslationServiceApplication extends Application<Configuration> {
  public static void main(String[] args) throws Exception {
    new TranslationServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "hello-translation-service";
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    final HelloTranslationRepository repository = new HelloTranslationRepository();
    final HelloTranslationResource resource = new HelloTranslationResource(repository);
    environment.jersey().register(resource);
  }
}
