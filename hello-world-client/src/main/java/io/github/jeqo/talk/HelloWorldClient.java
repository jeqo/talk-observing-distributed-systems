package io.github.jeqo.talk;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
class HelloWorldClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldClient.class);
  private static final String BASE_URL = "http://localhost:8080/hello-world";

  private final HttpClient httpClient;


  HelloWorldClient() {
    httpClient = HttpClientBuilder.create().build();
  }

  String sayHi(String name) {
    LOGGER.info("Calling Hello World Service - Operation `sayHi` - name=" + name);
    //return helloWorldService.sayHi(name);
    //throw new UnsupportedOperationException("not implemented yet");

    try {
      final HttpGet httpGet = new HttpGet(BASE_URL + "/" + name);
      HttpResponse response = httpClient.execute(httpGet);
      InputStream entityStream = response.getEntity().getContent();
      return IOUtils.toString(entityStream, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
      return "ERROR";
    }
  }

}
