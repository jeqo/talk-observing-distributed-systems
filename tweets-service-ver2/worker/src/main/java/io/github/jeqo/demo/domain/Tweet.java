package io.github.jeqo.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.jeqo.demo.rest.TweetRepresentation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public class Tweet {
  private final Long id;
  private final String createdAt;
  private final User user;
  private final String text;
  private final boolean isRetweet;
  private final List<Hashtag> hashtagSet;

  @JsonCreator
  private Tweet(@JsonProperty("id") Long id,
                @JsonProperty("created_at") String createdAt,
                @JsonProperty("user") User user,
                @JsonProperty("text") String text,
                @JsonProperty("is_retweet") boolean isRetweet,
                @JsonProperty("hashtags") List<Hashtag> hashtagSet) {
    this.id = id;
    this.createdAt = createdAt;
    this.user = user;
    this.text = text;
    this.isRetweet = isRetweet;
    this.hashtagSet = hashtagSet;
  }

  private Tweet(Long id,
                String createdAt,
                User user,
                String text,
                boolean isRetweet) {
    this.id = id;
    this.createdAt = createdAt;
    this.user = user;
    this.text = text;
    this.isRetweet = isRetweet;
    this.hashtagSet = new ArrayList<>();
  }

  public static Tweet buildFromRepresentation(TweetRepresentation representation) {
    final User user = User.build(representation.getUser());
    return
        new Tweet(
            representation.getId(),
            representation.getCreatedAt(),
            user,
            representation.getText(),
            representation.isRetweet(),
            representation.getEntities()
                .getHashtags()
                .stream()
                .map(Hashtag::buildFromRepresentation)
                .collect(Collectors.toList()));
  }

  public Tweet addHashtag(Hashtag hashtag) {
    hashtagSet.add(hashtag);
    return this;
  }


  public TweetRepresentation printRepresentation() {
    final TweetRepresentation tweetRepresentation = new TweetRepresentation();
    tweetRepresentation.setId(id);
    tweetRepresentation.setCreatedAt(createdAt);
    tweetRepresentation.setUser(user.printRepresentation());
    tweetRepresentation.setRetweet(isRetweet);
    tweetRepresentation.setText(text);
    final TweetRepresentation.EntitiesRepresentation entities = new TweetRepresentation.EntitiesRepresentation();
    final List<TweetRepresentation.EntitiesRepresentation.HashtagRepresentation> hashtags =
        hashtagSet.stream().map(Hashtag::printRepresentation).collect(Collectors.toList());
    entities.setHashtags(hashtags);
    tweetRepresentation.setEntities(entities);
    return tweetRepresentation;
  }

  @JsonProperty("user")
  public User user() {
    return user;
  }

  @JsonProperty("id")
  public Long id() {
    return id;
  }

  @JsonProperty("created_at")
  public String createdAt() {
    return createdAt;
  }

  @JsonProperty("text")
  public String text() {
    return text;
  }

  @JsonProperty("is_retweet")
  public boolean isRetweet() {
    return isRetweet;
  }

  @JsonProperty("hashtags")
  public List<Hashtag> hashtagList() {
    return hashtagSet;
  }

  /**
   *
   */
  public static class Hashtag {
    private final String text;

    public Hashtag(String text) {
      this.text = text;
    }

    static Hashtag buildFromRepresentation(TweetRepresentation.EntitiesRepresentation.HashtagRepresentation hashtagRepresentation) {
      return new Hashtag(hashtagRepresentation.getText());
    }

    public TweetRepresentation.EntitiesRepresentation.HashtagRepresentation printRepresentation() {
      final TweetRepresentation.EntitiesRepresentation.HashtagRepresentation hashtagRepresentation =
          new TweetRepresentation.EntitiesRepresentation.HashtagRepresentation();
      hashtagRepresentation.setText(text);
      return hashtagRepresentation;
    }

    public String text() {
      return text;
    }

  }
}
