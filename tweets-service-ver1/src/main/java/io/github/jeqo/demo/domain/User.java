package io.github.jeqo.demo.domain;

import io.github.jeqo.demo.rest.model.TweetRepresentation;
import org.jooq.Record;

import static io.github.jeqo.demo.infra.jooq.tables.Users.USERS;

/**
 * User Domain Model
 */
public class User {
  private final Long id;
  private final String name;
  private final String screenName;
  private final String location;
  private final boolean verified;

  private User(Long id, String name, String screenName, String location, boolean verified) {
    this.id = id;
    this.name = name;
    this.screenName = screenName;
    this.location = location;
    this.verified = verified;
  }

  static User build(TweetRepresentation.UserRepresentation user) {
    return
        new User(
            user.getId(),
            user.getName(),
            user.getScreenName(),
            user.getLocation(),
            user.isVerified());
  }

  TweetRepresentation.UserRepresentation printRepresentation() {
    final TweetRepresentation.UserRepresentation userRepresentation = new TweetRepresentation.UserRepresentation();
    userRepresentation.setId(id);
    userRepresentation.setName(name);
    userRepresentation.setScreenName(screenName);
    userRepresentation.setLocation(location);
    userRepresentation.setVerified(verified);
    return userRepresentation;
  }

  public Long id() {
    return id;
  }

  public String name() {
    return name;
  }

  public String screenName() {
    return screenName;
  }

  public String location() {
    return location;
  }

  public boolean verified() {
    return verified;
  }

  static User buildFromRecord(Record record) {
    return
        new User(
            record.get(USERS.ID).longValue(),
            record.get(USERS.NAME),
            record.get(USERS.SCREEN_NAME),
            record.get(USERS.LOCATION),
            record.get(USERS.VERIFIED));
  }
}
