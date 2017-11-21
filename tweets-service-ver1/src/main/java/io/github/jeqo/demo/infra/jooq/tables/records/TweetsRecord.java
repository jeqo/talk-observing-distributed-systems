/*
 * This file is generated by jOOQ.
*/
package io.github.jeqo.demo.infra.jooq.tables.records;


import io.github.jeqo.demo.infra.jooq.tables.Tweets;

import java.math.BigInteger;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TweetsRecord extends UpdatableRecordImpl<TweetsRecord> implements Record5<BigInteger, String, BigInteger, String, Boolean> {

    private static final long serialVersionUID = 1168805959;

    /**
     * Setter for <code>public.tweets.id</code>.
     */
    public void setId(BigInteger value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.tweets.id</code>.
     */
    public BigInteger getId() {
        return (BigInteger) get(0);
    }

    /**
     * Setter for <code>public.tweets.created_at</code>.
     */
    public void setCreatedAt(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.tweets.created_at</code>.
     */
    public String getCreatedAt() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.tweets.user_id</code>.
     */
    public void setUserId(BigInteger value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.tweets.user_id</code>.
     */
    public BigInteger getUserId() {
        return (BigInteger) get(2);
    }

    /**
     * Setter for <code>public.tweets.text</code>.
     */
    public void setText(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.tweets.text</code>.
     */
    public String getText() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.tweets.is_retweet</code>.
     */
    public void setIsRetweet(Boolean value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.tweets.is_retweet</code>.
     */
    public Boolean getIsRetweet() {
        return (Boolean) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<BigInteger> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<BigInteger, String, BigInteger, String, Boolean> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<BigInteger, String, BigInteger, String, Boolean> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigInteger> field1() {
        return Tweets.TWEETS.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Tweets.TWEETS.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigInteger> field3() {
        return Tweets.TWEETS.USER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Tweets.TWEETS.TEXT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field5() {
        return Tweets.TWEETS.IS_RETWEET;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger component3() {
        return getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean component5() {
        return getIsRetweet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger value3() {
        return getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value5() {
        return getIsRetweet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TweetsRecord value1(BigInteger value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TweetsRecord value2(String value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TweetsRecord value3(BigInteger value) {
        setUserId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TweetsRecord value4(String value) {
        setText(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TweetsRecord value5(Boolean value) {
        setIsRetweet(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TweetsRecord values(BigInteger value1, String value2, BigInteger value3, String value4, Boolean value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TweetsRecord
     */
    public TweetsRecord() {
        super(Tweets.TWEETS);
    }

    /**
     * Create a detached, initialised TweetsRecord
     */
    public TweetsRecord(BigInteger id, String createdAt, BigInteger userId, String text, Boolean isRetweet) {
        super(Tweets.TWEETS);

        set(0, id);
        set(1, createdAt);
        set(2, userId);
        set(3, text);
        set(4, isRetweet);
    }
}