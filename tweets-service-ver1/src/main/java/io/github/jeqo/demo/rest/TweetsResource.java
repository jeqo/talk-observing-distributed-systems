package io.github.jeqo.demo.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jeqo.demo.domain.Tweet;
import io.github.jeqo.demo.domain.TweetsQuery;
import io.github.jeqo.demo.domain.TweetsService;
import io.opentracing.contrib.jaxrs2.server.Traced;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 *
 */
@Path("v1/tweets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TweetsResource {

  private final TweetsService tweetsService;

  @Context
  private Request request = null;

  public TweetsResource(TweetsService tweetsService) {
    this.tweetsService = tweetsService;
  }

  @POST
  @Traced(operationName = "add_tweet") //Tracing instrumentation
  @Timed //Metrics instrumentation
  public Response addTweet(TweetRepresentation representation) {
    try {
      final Tweet tweet = Tweet.buildFromRepresentation(representation);
      tweetsService.addTweet(tweet);
      return Response.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.serverError().entity(e.getMessage()).build();
    }
  }

  @GET
  @Traced(operationName = "find_tweets") //Tracing instrumentation
  @Timed //Metrics instrumentation
  public Response findTweets() {
    try {
      final List<TweetRepresentation> tweetRepresentations =
          tweetsService.findTweets(new TweetsQuery())
              .stream()
              .map(Tweet::printRepresentation)
              .collect(toList());
      final TweetsRepresentation tweetsRepresentation = new TweetsRepresentation();
      tweetsRepresentation.setTweets(tweetRepresentations);
      return Response.ok(tweetsRepresentation).build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.serverError().entity(e.getMessage()).build();
    }
  }
}
