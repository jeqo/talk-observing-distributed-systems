package io.github.jeqo.demo;

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

  GreetingService(HelloWorldClient helloWorldClient) {
    this.helloWorldClient = helloWorldClient;
  }

  @GET
  @Path("{name}")
  public Response greeting(@PathParam("name") String name, @QueryParam("lang") String lang) {
    final String greeting = helloWorldClient.sayHi(name, lang);
    final GreetingRepresentation representation = new GreetingRepresentation(greeting);
    return Response.ok(representation).build();
  }
}
