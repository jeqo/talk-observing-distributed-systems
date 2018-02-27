package io.github.jeqo.demo.domain;

/**
 * Tweets Services
 */
public class TweetsService {

  private final TweetEventRepository tweetsRepository;

  public TweetsService(TweetEventRepository tweetsRepository) {
    this.tweetsRepository = tweetsRepository;
  }

  public void addTweet(Tweet tweet) {
    tweetsRepository.put(tweet);
  }
}
