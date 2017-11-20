package io.github.jeqo.demo.infra;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Collections;

/**
 *
 */
public class ElasticsearchTweetRepository {
  private final RestClient restClient;

  public ElasticsearchTweetRepository(RestClient restClient) {
    this.restClient = restClient;
  }

  public void index(String key, String value) {
    try {
      final HttpEntity entity = new NStringEntity(value, ContentType.APPLICATION_JSON);
      final String endpoint = String.format("%s/%s", "/tweets/tweet", key);
      final Response response = restClient.performRequest("PUT", endpoint, Collections.<String, String>emptyMap(), entity);
      if (response.getStatusLine().getStatusCode() != 200
          && response.getStatusLine().getStatusCode() != 201) {
        throw new IllegalStateException(response.getStatusLine().getReasonPhrase());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
