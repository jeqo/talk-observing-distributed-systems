package io.github.jeqo.demo.rest;

import io.github.jeqo.demo.infra.HelloTranslationClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 *
 */
@Path("hello")
public class HelloWorldResource {

  private final HelloTranslationClient translationClient;

  public HelloWorldResource(HelloTranslationClient translationClient) {
    this.translationClient = translationClient;
  }

  @GET
  @Path("{name}")
  public String hello(@PathParam("name") String name,
                      @QueryParam("lang") String lang) {
    final String hello = translationClient.translateHello(lang);
    return hello + " " + name;
  }
}
