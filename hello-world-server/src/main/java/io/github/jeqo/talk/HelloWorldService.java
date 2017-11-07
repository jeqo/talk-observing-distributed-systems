package io.github.jeqo.talk;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 */
@Path("hello-world")
public class HelloWorldService {

  @GET
  @Path("{name}")
  public String sayHi(@PathParam("name") String name) {
    return "Hello " + name;
  }
}
