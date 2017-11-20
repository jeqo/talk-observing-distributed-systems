package io.github.jeqo.demo.rest;

import io.github.jeqo.demo.domain.Tweet;
import io.github.jeqo.demo.domain.TweetsQuery;
import io.github.jeqo.demo.domain.TweetsService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
  public Response addTweet(TweetRepresentation representation) {
    final Tweet tweet = Tweet.buildFromRepresentation(representation);
    tweetsService.addTweet(tweet);
    return Response.ok().build();
  }

}
