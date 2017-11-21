package io.github.jeqo.demo.rest;

import io.github.jeqo.demo.domain.Tweet;
import io.github.jeqo.demo.domain.TweetsService;
import io.opentracing.ActiveSpan;
import io.opentracing.Span;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.Trace;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("v2/tweets")
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
    try (ActiveSpan ignored =
             dropWizardTracer.getTracer()
                 .buildSpan("addTweet")
                 .withTag("user", representation.getUser().getScreenName())
                 .asChildOf(span)
                 .startActive()) {
      final Tweet tweet = Tweet.buildFromRepresentation(representation);
      tweetsService.addTweet(tweet);
      return Response.ok().build();
    }
  }

}
