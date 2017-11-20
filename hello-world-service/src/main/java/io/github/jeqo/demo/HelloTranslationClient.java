package io.github.jeqo.demo;

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
class HelloTranslationClient {

  private final HttpClient httpClient;
  private final String baseUrl;

  HelloTranslationClient(HttpClient httpClient, String baseUrl) {
    this.httpClient = httpClient;
    this.baseUrl = baseUrl;
  }

  String translateHello(String lang) {
    try {
      final String uri = baseUrl + "/hello/" + lang;
      final HttpGet httpGet = new HttpGet(uri);
      final HttpResponse httpResponse = httpClient.execute(httpGet);
      final InputStream content = httpResponse.getEntity().getContent();
      return IOUtils.toString(content, Charset.defaultCharset());
    } catch (IOException e) {
      e.printStackTrace();
      //throw new RuntimeException(e);
      return "Hello";
    }
  }
}
