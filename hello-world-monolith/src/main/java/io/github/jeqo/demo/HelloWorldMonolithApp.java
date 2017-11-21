package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TranslationService;
import io.github.jeqo.demo.resources.GreetingResource;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;

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

    // Add Metrics Instrumentation to count requests
    final CollectorRegistry collectorRegistry = new CollectorRegistry();
    collectorRegistry.register(new DropwizardExports(environment.metrics()));

    // Register Metrics Servlet
    environment.admin()
        .addServlet("metrics", new MetricsServlet(collectorRegistry))
        .addMapping("/metrics");
  }
}
