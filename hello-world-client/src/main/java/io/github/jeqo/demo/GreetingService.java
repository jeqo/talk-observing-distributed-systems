package io.github.jeqo.demo;

import io.opentracing.ActiveSpan;
import io.opentracing.Span;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.Trace;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("greetings")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingService {

  private final HelloWorldClient helloWorldClient;
  private final DropWizardTracer dropWizardTracer;

  GreetingService(HelloWorldClient helloWorldClient,
                  DropWizardTracer dropWizardTracer) {
    this.helloWorldClient = helloWorldClient;
    this.dropWizardTracer = dropWizardTracer;
  }

  @GET
  @Trace
  @Path("{name}")
  public Response greeting(@Context Request request,
                           @PathParam("name") String name,
                           @QueryParam("lang") String lang) {
    final Span span = dropWizardTracer.getSpan(request);

    try (ActiveSpan ignored =
             dropWizardTracer.getTracer()
                 .buildSpan("greeting")
                 .withTag("name", name)
                 .withTag("lang", lang)
                 .asChildOf(span)
                 .startActive()) {
      final String greeting = helloWorldClient.sayHi(name, lang);
      final GreetingRepresentation representation = new GreetingRepresentation(greeting);
      return Response.ok(representation).build();
    }
  }
}
