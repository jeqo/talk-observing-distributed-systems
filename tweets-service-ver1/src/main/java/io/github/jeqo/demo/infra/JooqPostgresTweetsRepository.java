package io.github.jeqo.demo.infra;

import io.github.jeqo.demo.domain.Tweet;
import io.github.jeqo.demo.domain.TweetsRepository;
import io.github.jeqo.demo.domain.User;
import io.opentracing.ActiveSpan;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.jeqo.demo.infra.jooq.Tables.HASHTAGS;
import static io.github.jeqo.demo.infra.jooq.tables.Tweets.TWEETS;
import static io.github.jeqo.demo.infra.jooq.tables.Users.USERS;

/**
 *
 */
public class JooqPostgresTweetsRepository implements TweetsRepository {

  private final String jdbcUrl;
  private final String jdbcUsername;
  private final String jdbcPassword;

  public JooqPostgresTweetsRepository(String jdbcUrl, String jdbcUsername, String jdbcPassword) {
    this.jdbcUrl = jdbcUrl;
    this.jdbcUsername = jdbcUsername;
    this.jdbcPassword = jdbcPassword;
  }

  @Override
  public void put(Tweet tweet) {
    // Custom Span to group the execution of SQL statements
    try (ActiveSpan span =
             GlobalTracer.get()
                 .buildSpan("put")
                 .withTag(Tags.COMPONENT.getKey(), "TweetRepository")
                 .startActive()) {
      // JDBC executions
      try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword)) {
        final User user = tweet.user();
        final DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        final BigInteger userId = BigInteger.valueOf(user.id());
        dslContext
            .insertInto(USERS)
            .columns(USERS.ID, USERS.NAME, USERS.SCREEN_NAME, USERS.LOCATION, USERS.VERIFIED)
            .values(userId, user.name(), user.screenName(), user.location(), user.verified())
            .onDuplicateKeyUpdate()
            .set(USERS.NAME, user.name())
            .set(USERS.LOCATION, user.location())
            .execute();

        final BigInteger tweetId = BigInteger.valueOf(tweet.id());
        dslContext
            .insertInto(TWEETS)
            .columns(TWEETS.ID, TWEETS.CREATED_AT, TWEETS.USER_ID, TWEETS.TEXT, TWEETS.IS_RETWEET)
            .values(tweetId, tweet.createdAt(), userId, tweet.text(), tweet.isRetweet())
            .execute();

        tweet.hashtagSet()
            .forEach(hashtag ->
                dslContext
                    .insertInto(HASHTAGS)
                    .columns(HASHTAGS.TEXT, HASHTAGS.TWEET_ID)
                    .values(hashtag.text(), tweetId)
                    .execute());
        // End of JDBC executions
      } catch (Exception e) {
        // enriching custom Span with logs
        span.setTag(Tags.ERROR.getKey(), true);
        Map<String, Object> fields = new HashMap<>();
        fields.put("error.kind", e.getClass().getName());
        fields.put("error.object", e);
        fields.put("event", "error");
        fields.put("message", e.getMessage());
        fields.put("stack", ExceptionUtils.getStackTrace(e));
        span.log(fields);
        e.printStackTrace();
      }
    }
  }

  @Override
  public List<Tweet> find() {
    // Custom Span to group the execution of SQL statements
    try (ActiveSpan span =
             GlobalTracer.get()
                 .buildSpan("find")
                 .withTag(Tags.COMPONENT.getKey(), "TweetRepository")
                 .startActive()) {
      // Starting JDBC executions
      try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword)) {
        final DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        final Result<Record> records =
            dslContext.select()
                .from(TWEETS)
                .join(USERS)
                .on(TWEETS.USER_ID.eq(USERS.ID))
                .orderBy(TWEETS.ID.desc())
                .limit(500)
                .fetch();
        return
            records.stream()
                .map(Tweet::buildFromRecord)
                .peek(tweet -> {
                  final BigInteger tweetId = BigInteger.valueOf(tweet.id());
                  dslContext.select()
                      .from(HASHTAGS)
                      .where(HASHTAGS.TWEET_ID.eq(tweetId))
                      .fetch()
                      .stream()
                      .map(Tweet.Hashtag::buildFromRecord)
                      .forEach(tweet::addHashtag);
                })
                .collect(Collectors.toList());
        //End of JDBC executions
      } catch (SQLException e) {
        //Enriching custom Span
        span.setTag(Tags.ERROR.getKey(), true);
        Map<String, Object> fields = new HashMap<>();
        fields.put("error.kind", e.getClass().getName());
        fields.put("error.object", e);
        fields.put("event", "error");
        fields.put("message", e.getMessage());
        fields.put("stack", ExceptionUtils.getStackTrace(e));
        span.log(fields);
        e.printStackTrace();
        return new ArrayList<>();
      }
    }
  }
}
