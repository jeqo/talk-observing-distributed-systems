/*
 * This file is generated by jOOQ.
*/
package io.github.jeqo.demo.infra.jooq;


import io.github.jeqo.demo.infra.jooq.tables.Tweets;
import io.github.jeqo.demo.infra.jooq.tables.Users;

import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling indexes of tables of the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index TWEETS_PKEY = Indexes0.TWEETS_PKEY;
    public static final Index USERS_PKEY = Indexes0.USERS_PKEY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 extends AbstractKeys {
        public static Index TWEETS_PKEY = createIndex("tweets_pkey", Tweets.TWEETS, new OrderField[] { Tweets.TWEETS.ID }, true);
        public static Index USERS_PKEY = createIndex("users_pkey", Users.USERS, new OrderField[] { Users.USERS.ID }, true);
    }
}
