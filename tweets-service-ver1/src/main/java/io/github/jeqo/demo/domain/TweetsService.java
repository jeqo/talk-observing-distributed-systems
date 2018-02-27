package io.github.jeqo.demo.domain;

import java.util.List;

/**
 * Tweets Service
 */
public class TweetsService {

  private final TweetsRepository tweetsRepository;

  public TweetsService(TweetsRepository tweetsRepository) {
    this.tweetsRepository = tweetsRepository;
  }

  public void addTweet(Tweet tweet) {
    tweetsRepository.put(tweet);
  }

  public List<Tweet> findTweets() {
    return tweetsRepository.find();
  }
}
