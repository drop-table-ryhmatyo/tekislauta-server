PRAGMA foreign_keys = ON;

CREATE TABLE Board
(
    id integer,
    name varchar(64) NOT NULL,
    abbreviation varchar(4) NOT NULL,
    description varchar(1024) NULL,

    PRIMARY KEY (id)
);

CREATE TABLE Post
(
    id integer,
    board_abbreviation varchar(4) NOT NULL,
    topic_id integer NULL,

    ip varchar(16) NOT NULL,
    post_time integer(4) NOT NULL DEFAULT (strftime('%s', 'now')),
    subject varchar(128) NULL,
    message text NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (board_abbreviation) REFERENCES Board(id),
    FOREIGN KEY (topic_id) REFERENCES Post(id)
);