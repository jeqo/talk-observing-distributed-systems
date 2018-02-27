package io.github.jeqo.demo.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Tweet JSON Model
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TweetRepresentation {
  private Long id;
  @JsonProperty("created_at")
  private String createdAt;
  private UserRepresentation user;
  private String text;
  @JsonProperty("is_retweet")
  private boolean isRetweet;
  private EntitiesRepresentation entities;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public UserRepresentation getUser() {
    return user;
  }

  public void setUser(UserRepresentation user) {
    this.user = user;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public boolean isRetweet() {
    return isRetweet;
  }

  public void setRetweet(boolean retweet) {
    isRetweet = retweet;
  }

  public EntitiesRepresentation getEntities() {
    return entities;
  }

  public void setEntities(EntitiesRepresentation entities) {
    this.entities = entities;
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class UserRepresentation {
    private Long id;
    private String name;
    @JsonProperty("screen_name")
    private String screenName;
    private String location;
    private boolean verified;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getScreenName() {
      return screenName;
    }

    public void setScreenName(String screenName) {
      this.screenName = screenName;
    }

    public String getLocation() {
      return location;
    }

    public void setLocation(String location) {
      this.location = location;
    }

    public boolean isVerified() {
      return verified;
    }

    public void setVerified(boolean verified) {
      this.verified = verified;
    }
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class EntitiesRepresentation {
    private List<HashtagRepresentation> hashtags;

    public static class HashtagRepresentation {
      private String text;

      public String getText() {
        return text;
      }

      public void setText(String text) {
        this.text = text;
      }
    }

    public List<HashtagRepresentation> getHashtags() {
      return hashtags;
    }

    public void setHashtags(List<HashtagRepresentation> hashtags) {
      this.hashtags = hashtags;
    }
  }
}
