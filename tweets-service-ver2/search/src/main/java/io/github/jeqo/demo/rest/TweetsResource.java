package io.github.jeqo.demo.rest;

import io.github.jeqo.demo.domain.TweetRepository;
import io.opentracing.ActiveSpan;
import io.opentracing.Span;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.Trace;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

  private final TweetRepository tweetRepository;
  private final DropWizardTracer dropWizardTracer;

  public TweetsResource(TweetRepository tweetRepository, DropWizardTracer dropWizardTracer) {
    this.tweetRepository = tweetRepository;
    this.dropWizardTracer = dropWizardTracer;
  }

  @GET
  @Trace
  public Response findTweets(@Context Request request) {
    final Span span = dropWizardTracer.getSpan(request);
    try (final ActiveSpan ignored =
             dropWizardTracer.getTracer()
                 .buildSpan("findTweets")
                 .asChildOf(span)
                 .startActive()) {
      final String tweetsRepresentation = tweetRepository.find();
      return Response.ok(tweetsRepresentation).build();
    }
  }
}
