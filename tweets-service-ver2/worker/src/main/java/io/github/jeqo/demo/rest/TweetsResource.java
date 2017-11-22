package io.github.jeqo.demo.rest;

import io.github.jeqo.demo.domain.Tweet;
import io.github.jeqo.demo.domain.TweetsService;
import io.opentracing.contrib.jaxrs2.server.Traced;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("v2/tweets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TweetsResource {

  private final TweetsService tweetsService;

  public TweetsResource(TweetsService tweetsService) {
    this.tweetsService = tweetsService;
  }

  @POST
  @Traced(operationName = "add_tweet")
  public Response addTweet(TweetRepresentation representation) {
      final Tweet tweet = Tweet.buildFromRepresentation(representation);
      tweetsService.addTweet(tweet);
      return Response.ok().build();
  }

}
