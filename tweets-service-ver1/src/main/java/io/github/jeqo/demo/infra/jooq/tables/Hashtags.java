/*
 * This file is generated by jOOQ.
*/
package io.github.jeqo.demo.infra.jooq.tables;


import io.github.jeqo.demo.infra.jooq.Keys;
import io.github.jeqo.demo.infra.jooq.Public;
import io.github.jeqo.demo.infra.jooq.tables.records.HashtagsRecord;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


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
public class Hashtags extends TableImpl<HashtagsRecord> {

    private static final long serialVersionUID = -542304460;

    /**
     * The reference instance of <code>public.hashtags</code>
     */
    public static final Hashtags HASHTAGS = new Hashtags();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<HashtagsRecord> getRecordType() {
        return HashtagsRecord.class;
    }

    /**
     * The column <code>public.hashtags.text</code>.
     */
    public final TableField<HashtagsRecord, String> TEXT = createField("text", org.jooq.impl.SQLDataType.VARCHAR(300), this, "");

    /**
     * The column <code>public.hashtags.tweet_id</code>.
     */
    public final TableField<HashtagsRecord, BigInteger> TWEET_ID = createField("tweet_id", org.jooq.impl.SQLDataType.DECIMAL_INTEGER.precision(20).nullable(false), this, "");

    /**
     * Create a <code>public.hashtags</code> table reference
     */
    public Hashtags() {
        this(DSL.name("hashtags"), null);
    }

    /**
     * Create an aliased <code>public.hashtags</code> table reference
     */
    public Hashtags(String alias) {
        this(DSL.name(alias), HASHTAGS);
    }

    /**
     * Create an aliased <code>public.hashtags</code> table reference
     */
    public Hashtags(Name alias) {
        this(alias, HASHTAGS);
    }

    private Hashtags(Name alias, Table<HashtagsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Hashtags(Name alias, Table<HashtagsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<HashtagsRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<HashtagsRecord, ?>>asList(Keys.HASHTAGS__HASHTAGS_TWEET_ID_FKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hashtags as(String alias) {
        return new Hashtags(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hashtags as(Name alias) {
        return new Hashtags(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Hashtags rename(String name) {
        return new Hashtags(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Hashtags rename(Name name) {
        return new Hashtags(name, null);
    }
}
