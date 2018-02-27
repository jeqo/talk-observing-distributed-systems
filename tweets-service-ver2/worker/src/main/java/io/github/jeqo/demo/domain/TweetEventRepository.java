package io.github.jeqo.demo.domain;

/**
 * Twitter Event Repository
 */
public interface TweetEventRepository {
  void put(Tweet tweet);
}
