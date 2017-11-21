package io.github.jeqo.demo.rest;

import brave.opentracing.BraveSpan;
import io.github.jeqo.demo.domain.Tweet;
import io.github.jeqo.demo.domain.TweetsQuery;
import io.github.jeqo.demo.domain.TweetsService;
import io.opentracing.ActiveSpan;
import io.opentracing.Span;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.Trace;
import io.opentracing.tag.Tags;

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
  private final DropWizardTracer dropWizardTracer;

  public TweetsResource(TweetsService tweetsService, DropWizardTracer dropWizardTracer) {
    this.tweetsService = tweetsService;
    this.dropWizardTracer = dropWizardTracer;
  }

  @POST
  @Trace
  public Response addTweet(@Context Request request,
                           TweetRepresentation representation) {
    final Span span = dropWizardTracer.getSpan(request);

    final ActiveSpan activeSpan = dropWizardTracer.getTracer()
        .buildSpan("addTweet")
        .asChildOf(span)
        .withTag("user", representation.getUser().getScreenName())
        .startActive();
    try {
      final Tweet tweet = Tweet.buildFromRepresentation(representation);
      tweetsService.addTweet(tweet);
      final AckRepresentation entity = new AckRepresentation();
      return Response.ok(entity).build();
    } catch (Exception e) {
      activeSpan.setTag(Tags.ERROR.getKey(), true);
      activeSpan.setTag("exception", e.getMessage());
      final AckRepresentation entity = new AckRepresentation(e.getMessage());
      return Response.serverError().entity(entity).build();
    } finally {
      activeSpan.close();
    }
  }

  @GET
  @Trace
  public Response findTweets(@Context Request request) {
    final Span span = dropWizardTracer.getSpan(request);
    final ActiveSpan activeSpan = dropWizardTracer.getTracer()
        .buildSpan("findTweets")
        .asChildOf(span)
        .startActive();
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
      activeSpan.setTag(Tags.ERROR.getKey(), true);
      activeSpan.setTag("exception", e.getMessage());
      final AckRepresentation entity = new AckRepresentation(e.getMessage());
      return Response.serverError().entity(entity).build();
    } finally {
      activeSpan.close();
    }
  }
}
