package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TweetRepository;
import io.github.jeqo.demo.infra.ElasticsearchTweetRepository;
import io.github.jeqo.demo.rest.TweetsResource;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 *
 */
public class SearchServiceApplication extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new SearchServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "tweets-search-service";
  }

  public void run(Configuration configuration, Environment environment) throws Exception {
    final HttpHost httpHost = new HttpHost("tweets-elasticsearch", 9200);
    final RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
    final RestClient restClient = restClientBuilder.build();
    final RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClient);
    final TweetRepository tweetRepository = new ElasticsearchTweetRepository(restHighLevelClient);
    final TweetsResource tweetsResource = new TweetsResource(tweetRepository);
    environment.jersey().register(tweetsResource);
  }
}
