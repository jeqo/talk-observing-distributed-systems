package io.github.jeqo.demo.domain;

import java.util.List;

/**
 *
 */
public interface TweetsRepository {
  void put(Tweet tweet);

  List<Tweet> find(FindAllTweets query);
}
