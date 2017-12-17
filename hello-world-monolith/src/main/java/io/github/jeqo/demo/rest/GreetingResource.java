package io.github.jeqo.demo.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jeqo.demo.domain.TranslationService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * HTTP Endpoint
 */
@Path("hello")
public class GreetingResource {

  private final TranslationService translationService;

  public GreetingResource(TranslationService translationService) {
    this.translationService = translationService;
  }

  @GET
  @Path("{name}")
  @Timed
  public Response hello(@PathParam("name") String name,
                        @QueryParam("lang") String lang) {
    final String hello = translationService.translate(lang);
    final String entity = hello + " " + name + "\n";
    return Response.ok(entity).build();
  }
}
