package io.github.jeqo.demo;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("translation")
public class HelloTranslationResource {

  private final HelloTranslationRepository repository;

  public HelloTranslationResource(HelloTranslationRepository repository) {
    this.repository = repository;
  }

  @Path("hello/{lang}")
  public Response translate(@PathParam("lang") String lang) {
    final String helloTranslated = repository.translate(lang);
    return Response.ok(helloTranslated).build();
  }
}
