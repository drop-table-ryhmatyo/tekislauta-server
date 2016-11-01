PRAGMA foreign_keys = ON;

CREATE TABLE Board
(
    name varchar(64) NOT NULL,
    abbreviation varchar(4) NOT NULL,
    description varchar(1024) NULL,

    PRIMARY KEY (abbreviation)
);

CREATE TABLE Post
(
    id integer,
    board_abbreviation varchar(4) NOT NULL,
    topic_id integer NULL,

    ip varchar(16) NOT NULL,
    post_time integer(4) NOT NULL DEFAULT (strftime('%s', 'now')),
    subject varchar(128) NULL,
    message varchar(2048) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (board_abbreviation) REFERENCES Board(abbreviation),
    FOREIGN KEY (topic_id) REFERENCES Post(id)
);