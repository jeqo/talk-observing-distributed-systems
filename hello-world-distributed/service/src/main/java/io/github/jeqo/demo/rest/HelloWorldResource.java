package io.github.jeqo.demo.rest;

import io.github.jeqo.demo.infra.HelloTranslationClient;
import io.opentracing.contrib.jaxrs2.server.Traced;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 *
 */
@Path("hello-world")
public class HelloWorldResource {

  private final HelloTranslationClient translationClient;

  public HelloWorldResource(HelloTranslationClient translationClient) {
    this.translationClient = translationClient;
  }

  @GET
  @Traced(operationName = "say_hi")
  @Path("{name}")
  public String sayHi(@PathParam("name") String name,
                      @QueryParam("lang") String lang) {
    final String hello = translationClient.translateHello(lang);
    return hello + " " + name;
  }
}
