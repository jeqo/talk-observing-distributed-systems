package io.github.jeqo.demo.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class TweetCompleteRepresentation {
  private Long id;
  private String createdAt;
  private UserRepresentation user;
  private String text;
  private String lang;
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

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
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

  public static class UserRepresentation {
    private Long id;
    private String name;
    @JsonProperty("screen_name")
    private String screenName;
    private String location;
    private boolean verified;
    @JsonProperty("friends_count")
    private int friendsCount;
    @JsonProperty("followers_count")
    private int followersCount;
    @JsonProperty("statuses_count")
    private int statusesCount;

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

    public int getFriendsCount() {
      return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
      this.friendsCount = friendsCount;
    }

    public int getFollowersCount() {
      return followersCount;
    }

    public void setFollowersCount(int followersCount) {
      this.followersCount = followersCount;
    }

    public int getStatusesCount() {
      return statusesCount;
    }

    public void setStatusesCount(int statusesCount) {
      this.statusesCount = statusesCount;
    }
  }

  public static class EntitiesRepresentation {
    private List<HashtagRepresentation> hashtags;
    private List<MediumRepresentation> media;
    private List<UrlRepresentation> urls;
    private List<UserMentionRepresentation> userMentions;

    public static class HashtagRepresentation {
      private String text;

      public String getText() {
        return text;
      }

      public void setText(String text) {
        this.text = text;
      }
    }

    static class MediumRepresentation {

      private String displayUrl;
      private String expandedUrl;
      private Long id;
      private String type;
      private String url;

      public String getDisplayUrl() {
        return displayUrl;
      }

      public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
      }

      public String getExpandedUrl() {
        return expandedUrl;
      }

      public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
      }

      public Long getId() {
        return id;
      }

      public void setId(Long id) {
        this.id = id;
      }

      public String getType() {
        return type;
      }

      public void setType(String type) {
        this.type = type;
      }

      public String getUrl() {
        return url;
      }

      public void setUrl(String url) {
        this.url = url;
      }
    }

    static class UrlRepresentation {
      private String displayUrl;
      private String expandedUrl;
      private String url;

      public String getDisplayUrl() {
        return displayUrl;
      }

      public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
      }

      public String getExpandedUrl() {
        return expandedUrl;
      }

      public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
      }

      public String getUrl() {
        return url;
      }

      public void setUrl(String url) {
        this.url = url;
      }
    }

    private class UserMentionRepresentation {
      private Long id;
      private String name;
      @JsonProperty("screen_name")
      private String screenName;

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
    }

    public List<HashtagRepresentation> getHashtags() {
      return hashtags;
    }

    public void setHashtags(List<HashtagRepresentation> hashtags) {
      this.hashtags = hashtags;
    }

    public List<MediumRepresentation> getMedia() {
      return media;
    }

    public void setMedia(List<MediumRepresentation> media) {
      this.media = media;
    }

    public List<UrlRepresentation> getUrls() {
      return urls;
    }

    public void setUrls(List<UrlRepresentation> urls) {
      this.urls = urls;
    }

    public List<UserMentionRepresentation> getUserMentions() {
      return userMentions;
    }

    public void setUserMentions(List<UserMentionRepresentation> userMentions) {
      this.userMentions = userMentions;
    }
  }
}
