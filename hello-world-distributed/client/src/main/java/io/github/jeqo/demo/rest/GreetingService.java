package io.github.jeqo.demo.rest;

import io.github.jeqo.demo.infra.HelloWorldClient;
import io.opentracing.contrib.jaxrs2.server.Traced;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("greetings")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingService {

  private final HelloWorldClient helloWorldClient;

  public GreetingService(HelloWorldClient helloWorldClient) {
    this.helloWorldClient = helloWorldClient;
  }

  @GET
  @Traced(operationName = "greeting")
  @Path("{name}")
  public Response greeting(@PathParam("name") String name,
                           @QueryParam("lang") String lang) {
    final String greeting = helloWorldClient.sayHi(name, lang);
    final GreetingRepresentation representation = new GreetingRepresentation(greeting);
    return Response.ok(representation).build();
  }
}
