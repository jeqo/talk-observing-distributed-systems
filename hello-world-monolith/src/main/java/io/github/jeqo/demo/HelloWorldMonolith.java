package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;

/**
 *
 */
public class HelloWorldMonolith extends Application<Configuration> {
  public static void main(String[] args) throws Exception {
    new HelloWorldMonolith().run(args);
  }

  @Override
  public void run(Configuration configuration, Environment environment) throws Exception {
    final GreetingService greetingService = new GreetingService();
    environment.jersey().register(greetingService);

    final CollectorRegistry collectorRegistry = new CollectorRegistry();
    collectorRegistry.register(new DropwizardExports(environment.metrics()));
    environment.admin()
        .addServlet("metrics", new MetricsServlet(collectorRegistry))
        .addMapping("/metrics");
  }
}
