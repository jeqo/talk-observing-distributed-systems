package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TweetsRepository;
import io.github.jeqo.demo.domain.TweetsService;
import io.github.jeqo.demo.infra.JooqPostgresTweetsRepository;
import io.github.jeqo.demo.rest.TweetsResource;

/**
 *
 */
public class TweetsServiceVer1Application extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new TweetsServiceVer1Application().run(args);
  }

  @Override
  public String getName() {
    return "tweets-service-ver1";
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    final String jdbcUrl = "jdbc:postgresql://tweets-db/postgres";
    final String jdbcUsername = "postgres";
    final String jdbcPassword = "example";
    final TweetsRepository tweetsRepository = new JooqPostgresTweetsRepository(jdbcUrl, jdbcUsername, jdbcPassword);
    final TweetsService tweetsService = new TweetsService(tweetsRepository);
    final TweetsResource tweetsResource = new TweetsResource(tweetsService);
    environment.jersey().register(tweetsResource);
  }
}
