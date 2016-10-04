-- Copyright © 2016 drop-table
-- This work is part of the tekislauta project, licensed under the MIT license. See the LICENSE file for details.

PRAGMA foreign_keys = ON;

CREATE TABLE Board
(
    id integer,
    name varchar(64) NOT NULL,        -- e.g. "Random"
    abbreviation varchar(4) NOT NULL, -- e.g. "b"
    description varchar(1024) NULL, -- e.g. "The stories and information posted here are artistic works of..."

   PRIMARY KEY (id)
);

CREATE TABLE Post
(
    id integer,
    board_id integer NOT NULL,
    topic_id integer NULL, -- a post is a thread starter if topic_id is NULL
    
    ip blob NOT NULL,
    post_time integer(4) NOT NULL DEFAULT (strftime('%s', 'now')), -- unix time
    subject varchar(128) NULL,
    message text NOT NULL, -- either an image or a message...at this point we only support messages only ~ 26-09-2016 cxcorp

    PRIMARY KEY (id),
    FOREIGN KEY (board_id) REFERENCES Board(id),
    FOREIGN KEY (topic_id) REFERENCES Post(id)
);
