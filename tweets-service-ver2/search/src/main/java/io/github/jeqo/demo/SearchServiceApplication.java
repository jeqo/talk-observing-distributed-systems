package io.github.jeqo.demo;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.jeqo.demo.domain.TweetRepository;
import io.github.jeqo.demo.infra.ElasticsearchTweetRepository;
import io.github.jeqo.demo.rest.TweetsResource;
import io.opentracing.Tracer;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.ServerTracingFeature;
import io.opentracing.contrib.elasticsearch.TracingHttpClientConfigCallback;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;
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
    final Tracer tracer = getTracer();
    GlobalTracer.register(tracer);
    final DropWizardTracer dropWizardTracer = new DropWizardTracer(tracer);
    final ServerTracingFeature serverTracingFeature =
        new ServerTracingFeature.Builder(dropWizardTracer)
            .withTraceAnnotations()
            .build();
    environment.jersey().register(serverTracingFeature);

    final HttpHost httpHost = new HttpHost("tweets-elasticsearch", 9200);
    final RestClientBuilder restClientBuilder =
        RestClient.builder(httpHost).setHttpClientConfigCallback(new TracingHttpClientConfigCallback(tracer));
    final RestClient restClient = restClientBuilder.build();
    final RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClient);
    final TweetRepository tweetRepository = new ElasticsearchTweetRepository(restHighLevelClient);
    final TweetsResource tweetsResource = new TweetsResource(tweetRepository, dropWizardTracer);
    environment.jersey().register(tweetsResource);
  }


  private Tracer getTracer() {
    try {
      return new com.uber.jaeger.Configuration(
          "tweets-search-v2",
          new com.uber.jaeger.Configuration.SamplerConfiguration("const", 1),
          new com.uber.jaeger.Configuration.ReporterConfiguration(
              true,
              "tracing-jaeger",
              6831,
              1000,   // flush interval in milliseconds
              10000)  /*max buffered Spans*/)
          .getTracer();
    } catch (Exception e) {
      e.printStackTrace();
      return new MockTracer();
    }
  }
}
