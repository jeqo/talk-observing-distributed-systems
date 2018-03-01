package io.github.jeqo.demo.domain;

import io.github.jeqo.demo.rest.model.TweetRepresentation;
import org.jooq.Record;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.jeqo.demo.infra.jooq.Tables.HASHTAGS;
import static io.github.jeqo.demo.infra.jooq.tables.Tweets.TWEETS;

/**
 * Tweet Domain Model
 */
public class Tweet {
  private final Long id;
  private final String createdAt;
  private final User user;
  private final String text;
  private final boolean isRetweet;
  private final Set<Hashtag> hashtagSet;

  private Tweet(Long id,
                String createdAt,
                User user,
                String text,
                boolean isRetweet,
                Set<Hashtag> hashtagSet) {
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
    this.hashtagSet = new HashSet<>();
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
                .collect(Collectors.toSet()));
  }

  public Tweet addHashtag(Hashtag hashtag) {
    hashtagSet.add(hashtag);
    return this;
  }

  public static Tweet buildFromRecord(Record record) {
    final User user = User.buildFromRecord(record);
    return
        new Tweet(
            record.get(TWEETS.ID).longValue(),
            record.get(TWEETS.CREATED_AT),
            user,
            record.get(TWEETS.TEXT),
            record.get(TWEETS.IS_RETWEET));
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

  public User user() {
    return user;
  }

  public Long id() {
    return id;
  }

  public String createdAt() {
    return createdAt;
  }

  public String text() {
    return text;
  }

  public boolean isRetweet() {
    return isRetweet;
  }

  public Set<Hashtag> hashtagSet() {
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

    public static Hashtag buildFromRecord(Record record) {
      return new Hashtag(record.get(HASHTAGS.TEXT));
    }
  }
}
