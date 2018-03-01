package io.github.jeqo.demo.domain;

import java.util.List;

/**
 * Tweet Repository
 */
public interface TweetsRepository {
  void put(Tweet tweet);

  List<Tweet> find();
}
