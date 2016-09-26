-- Copyright © 2016 drop-table
-- This work is part of the tekislauta project, licensed under the MIT license. See the LICENSE file for details.

PRAGMA foreign_keys = ON;

CREATE TABLE Board
(
    id integer PRIMARY KEY,
    name varchar(64) NOT NULL,        -- e.g. "Random"
    abbreviation varchar(4) NOT NULL, -- e.g. "b"
    description varchar(1028) NULL -- e.g. "The stories and information posted here are artistic works of..."
);

CREATE TABLE Post
(
    id integer PRIMARY KEY,
    board_id integer NOT NULL REFERENCES Board(id),
    topic_id integer NULL REFERENCES Post(id), -- a post is a thread starter if topic_id is NULL
    ip blob NOT NULL,
    posted datetime NOT NULL,
    subject varchar(64) NULL,
    message text NOT NULL -- either an image or a message...at this point we only support messages only ~ 26-09-2016 cxcorp
);