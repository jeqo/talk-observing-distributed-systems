package io.github.jeqo.demo.rest;

import io.github.jeqo.demo.infra.HelloTranslationRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Translation Service HTTP Endpoint
 */
@Path("translation")
public class HelloTranslationResource {

  private final HelloTranslationRepository repository;

  public HelloTranslationResource(HelloTranslationRepository repository) {
    this.repository = repository;
  }

  @GET
  @Path("hello/{lang}")
  public Response translate(@PathParam("lang") String lang) {
    final String helloTranslated = repository.translate(lang);
    return Response.ok(helloTranslated).build();
  }
}
