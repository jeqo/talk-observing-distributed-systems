package io.github.jeqo.demo.rest;

import io.github.jeqo.demo.infra.HelloTranslationClient;
import io.opentracing.ActiveSpan;
import io.opentracing.Span;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.Trace;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

/**
 *
 */
@Path("hello-world")
public class HelloWorldResource {

  private final HelloTranslationClient translationClient;
  private final DropWizardTracer dropWizardTracer;

  public HelloWorldResource(HelloTranslationClient translationClient, DropWizardTracer dropWizardTracer) {
    this.translationClient = translationClient;
    this.dropWizardTracer = dropWizardTracer;
  }

  @GET
  @Trace
  @Path("{name}")
  public String sayHi(@Context Request request,
                      @PathParam("name") String name,
                      @QueryParam("lang") String lang) {
    Span span = dropWizardTracer.getSpan(request);
    try (ActiveSpan ignored =
             dropWizardTracer.getTracer()
                 .buildSpan("sayHi")
                 .asChildOf(span)
                 .withTag("name", name)
                 .withTag("lang", lang)
                 .startActive()) {
      final String hello = translationClient.translateHello(lang);
      return hello + " " + name;
    }
  }
}
