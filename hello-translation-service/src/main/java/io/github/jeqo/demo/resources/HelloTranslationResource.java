package io.github.jeqo.demo.resources;

import io.github.jeqo.demo.infra.HelloTranslationRepository;
import io.opentracing.contrib.dropwizard.Trace;

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
  @Trace
  public Response translate(@PathParam("lang") String lang) {
    final String helloTranslated = repository.translate(lang);
    return Response.ok(helloTranslated).build();
  }
}
