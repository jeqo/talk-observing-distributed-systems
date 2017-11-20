/*
 * This file is generated by jOOQ.
*/
package io.github.jeqo.demo.infra.jooq.tables.records;


import io.github.jeqo.demo.infra.jooq.tables.Hashtags;

import java.math.BigInteger;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.TableRecordImpl;


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
public class HashtagsRecord extends TableRecordImpl<HashtagsRecord> implements Record2<String, BigInteger> {

    private static final long serialVersionUID = -2118178227;

    /**
     * Setter for <code>public.hashtags.text</code>.
     */
    public void setText(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.hashtags.text</code>.
     */
    public String getText() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.hashtags.tweet_id</code>.
     */
    public void setTweetId(BigInteger value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.hashtags.tweet_id</code>.
     */
    public BigInteger getTweetId() {
        return (BigInteger) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<String, BigInteger> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<String, BigInteger> valuesRow() {
        return (Row2) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return Hashtags.HASHTAGS.TEXT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigInteger> field2() {
        return Hashtags.HASHTAGS.TWEET_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component1() {
        return getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger component2() {
        return getTweetId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger value2() {
        return getTweetId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashtagsRecord value1(String value) {
        setText(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashtagsRecord value2(BigInteger value) {
        setTweetId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashtagsRecord values(String value1, BigInteger value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached HashtagsRecord
     */
    public HashtagsRecord() {
        super(Hashtags.HASHTAGS);
    }

    /**
     * Create a detached, initialised HashtagsRecord
     */
    public HashtagsRecord(String text, BigInteger tweetId) {
        super(Hashtags.HASHTAGS);

        set(0, text);
        set(1, tweetId);
    }
}
