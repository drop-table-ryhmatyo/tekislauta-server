# tekislauta API
Our API is free for everyone to use and abuse! You can for example exploit our lazy back-end solutions, make a bot that deletes a post instantly when it's posted, or use the API like a sophisticated human being. 

# Entrypoint
`http://tekislauta-api.herokuapp.com/api`

# Endpoints

`GET /boards/` Returns an array of all boards.

```
POST /boards/
Post body:
{
    "name":"Science",
    "abbreviation":"sci",
    "description":"Nerds!"
}
```
Creates a new board.

`GET /boards/:abbreviation` Gets a board by abbreviation.

`DELETE /boards/:abbreviation` Deletes a board by abbreviation.

`GET /boards/posts/` Gets the first page of topics for board.

`GET /boards/posts/:page` Gets posts at specified page.

```
POST /boards/:board_abbreviation/posts/
POst body:
{
    "subject":"Hello world",
    "message":"I'm using tilt controlls!"
}
```
Creates new topic to board `board_abbreviation`

```
POST /boards/:board_abbreviation/posts/topics/:post_id
Post body:
{
    "subject":"",
    "message":"Welcome"
}
```
Creates a reply to `topic_id` which is a post it where `topic_id` is null. 
calling POST to `/boards/:board_abbreviation/posts/` creates a topic, POST to `/boards/:board_abbreviation/posts/topics/:post_id` creates a reply to a topic.

`DELETE /posts/:post_id` Deletes a post.

