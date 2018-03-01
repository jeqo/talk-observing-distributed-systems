package io.github.jeqo.demo.rest.model;

import java.util.List;

/**
 * Tweets JSON Model
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
