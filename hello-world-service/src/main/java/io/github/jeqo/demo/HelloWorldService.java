package io.github.jeqo.demo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 *
 */
@Path("hello-world")
public class HelloWorldService {

  private final HelloTranslationClient translationClient;

  public HelloWorldService(HelloTranslationClient translationClient) {
    this.translationClient = translationClient;
  }

  @GET
  @Path("{name}")
  public String sayHi(@PathParam("name") String name, @QueryParam("lang") String lang) {
    final String hello = translationClient.translateHello(lang);
    return hello + " " + name;
  }
}
