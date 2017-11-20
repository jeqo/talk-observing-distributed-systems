package io.github.jeqo.demo;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("hello")
public class GreetingService {

  @GET
  @Path("{name}")
  @Timed
  public Response hello(@PathParam("name") String name) {
    return Response.ok("Hello " + name).build();
  }
}
