package io.github.jeqo.demo.rest;

import java.util.List;

/**
 *
 */
public class TweetsRepresentation {
  private List<TweetRepresentation> tweets;

  public List<TweetRepresentation> getTweets() {
    return tweets;
  }

  public void setTweets(List<TweetRepresentation> tweets) {
    this.tweets = tweets;
  }
}
