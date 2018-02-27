package io.github.jeqo.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.jeqo.demo.rest.TweetRepresentation;

/**
 * User Domain Model
 */
public class User {
  private final Long id;
  private final String name;
  private final String screenName;
  private final String location;
  private final boolean verified;

  @JsonCreator
  public User(@JsonProperty("id") Long id,
              @JsonProperty("name") String name,
              @JsonProperty("screen_name") String screenName,
              @JsonProperty("location") String location,
              @JsonProperty("verified") boolean verified) {
    this.id = id;
    this.name = name;
    this.screenName = screenName;
    this.location = location;
    this.verified = verified;
  }

  public static User build(TweetRepresentation.UserRepresentation user) {
    return
        new User(
            user.getId(),
            user.getName(),
            user.getScreenName(),
            user.getLocation(),
            user.isVerified());
  }

  public TweetRepresentation.UserRepresentation printRepresentation() {
    final TweetRepresentation.UserRepresentation userRepresentation = new TweetRepresentation.UserRepresentation();
    userRepresentation.setId(id);
    userRepresentation.setName(name);
    userRepresentation.setScreenName(screenName);
    userRepresentation.setLocation(location);
    userRepresentation.setVerified(verified);
    return userRepresentation;
  }

  @JsonProperty("id")
  public Long id() {
    return id;
  }

  @JsonProperty("name")
  public String name() {
    return name;
  }

  @JsonProperty("screen_name")
  public String screenName() {
    return screenName;
  }

  @JsonProperty("location")
  public String location() {
    return location;
  }

  @JsonProperty("verified")
  public boolean verified() {
    return verified;
  }

}
