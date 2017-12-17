package io.github.jeqo.demo.infra;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 *
 */
public class HelloTranslationClient {
  private static final String baseUrl = "http://hello-translation-service:8080/translation";

  private final HttpClient httpClient;

  public HelloTranslationClient(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public String translateHello(String lang) {
    try {
      final String uri = baseUrl + "/hello/" + lang;
      final HttpGet httpGet = new HttpGet(uri);

      final HttpResponse httpResponse = httpClient.execute(httpGet);

      final InputStream content = httpResponse.getEntity().getContent();
      return IOUtils.toString(content, Charset.defaultCharset());
    } catch (IOException e) {
      e.printStackTrace();
      return "Hello"; //Fallback
    }
  }
}
