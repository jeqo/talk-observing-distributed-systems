CREATE TABLE users (
    id numeric (20,0) PRIMARY KEY,
    name varchar (300) NOT NULL,
    screen_name varchar (300) NOT NULL,
    location varchar (300),
    verified boolean NOT NULL
);

CREATE TABLE tweets (
    id numeric (20,0) PRIMARY KEY,
    created_at varchar (30) NOT NULL,
    user_id numeric (20,0) NOT NULL,
    text varchar (300) NOT NULL,
    is_retweet boolean NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE hashtags (
    text varchar (300),
    tweet_id numeric (20,0) NOT NULL,
    FOREIGN KEY (tweet_id) REFERENCES tweets(id)
);