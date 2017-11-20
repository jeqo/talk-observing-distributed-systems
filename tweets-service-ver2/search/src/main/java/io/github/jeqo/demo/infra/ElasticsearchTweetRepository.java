package io.github.jeqo.demo.infra;

import io.github.jeqo.demo.domain.TweetRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

/**
 *
 */
public class ElasticsearchTweetRepository implements TweetRepository {

  private final RestHighLevelClient restHighLevelClient;

  public ElasticsearchTweetRepository(RestHighLevelClient restHighLevelClient) {
    this.restHighLevelClient = restHighLevelClient;
  }

  public String find() {
    try {
      final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      searchSourceBuilder.sort("_score");
      searchSourceBuilder.sort("_uid");

      final String queryText = "*";
      final SimpleQueryStringBuilder simpleQueryStringBuilder =
          new SimpleQueryStringBuilder(queryText);
      simpleQueryStringBuilder.defaultOperator(Operator.AND);
      searchSourceBuilder.query(simpleQueryStringBuilder);

      //LOGGER.info("Elasticsearch query: {}", searchSourceBuilder.toString());

      final SearchRequest searchRequest = new SearchRequest();
      searchRequest.indices("tweets");
      searchRequest.types("tweet");
      searchRequest.source(searchSourceBuilder);
      final SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
      return searchResponse.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return e.getMessage();
    }
  }
}
