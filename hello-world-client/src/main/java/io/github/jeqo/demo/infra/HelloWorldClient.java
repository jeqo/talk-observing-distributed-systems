package io.github.jeqo.demo.infra;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class HelloWorldClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldClient.class);
  private static final String BASE_URL = "http://hello-world-service:8080/hello-world";

  private final HttpClient httpClient;


  public HelloWorldClient(HttpClient httpClient) {

    this.httpClient = httpClient;
  }

  public String sayHi(String name, String lang) {
    LOGGER.info("Calling Hello World Service - Operation `sayHi` - name=" + name);

    try {
      final HttpGet httpGet = new HttpGet(BASE_URL + "/" + name + "?lang=" + lang);
      HttpResponse response = httpClient.execute(httpGet);
      InputStream entityStream = response.getEntity().getContent();
      return IOUtils.toString(entityStream, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
      return "ERROR";
    }
  }

}
