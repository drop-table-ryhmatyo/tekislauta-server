# tekislauta API
Our API is free for everyone to use and abuse! You can for example exploit our lazy back-end solutions, make a bot that deletes a post instantly when it's posted, or use the API like a sophisticated human being. 

# Entrypoint
`http://droptable.tk/api`

# Structure
```
{
  "status": "Success",
  "data": { ... }
}
```
All responses have a `status` property whose value is one of the following:
- `Success` (HTTP 200)
- `Error` (HTTP 400-500)
- `Unauthorized` (HTTP 401)

The response data can be accessed via the `data` property.

# Response models

## Board
The `Board` object contains the following properties:

Key | Description
---- | -----------
`name` | Name of the board.
`abbreviation` | Acts as the ID. Abbreviation of the board's name
`description` | A short description of the board.

### Example
```
{
  "name": "Random",
  "abbreviation": "b",
  "description": "Stories of fiction"
}
```

## Post
The `Post` object contains the following properties:

Key | Description
----|------------
`id` | **Number**. The unique ID of the post or thread.
`topic_id` | **Number**. If present, signifies that this post is a reply to the specified thread.
`ip` | A hash of the poster's IP address, used for identifying users from each other.
`post_time` | **Number**. 32-bit unix time stamp.
`subject` | Subject of the post.
`message` | Message of the post.

### Example
```
{
  "id": 5,
  "topic_id": 2,
  "ip": "31AC1058",
  "post_time": 1477931790,
  "subject": "Hello world!",
  "message": "What a lovely day!"
}
```

# Endpoints

Path | Verb | Description
-----|------|------------
`/boards/` | GET | Returns an array of all boards. The `Board` response model is used.
`/boards/` | POST | Creates a new board. The `Board` response model is used.
`/boards/:abbreviation` | GET | Gets a specific board. The `Board` response model is used.
`/boards/:abbreviation` | DELETE | Deletes a board. Requires basic authorization headers to be present.
`/boards/:abbreviation/posts/topics/:topic` | GET | Gets all replies to a thread. `topic` is the post ID of the starting message.
`/boards/:abbreviation/posts/topics/:topic` | POST | Creates a reply to a thread. The `Post` response model is used.
`/boards/:abbreviation/posts/` | GET | Gets threads of a board. The `Post` response model is used.
`/boards/:abbreviation/posts/` | POST | Creates a new thread on the specified board. The `Post` response model is used.
`/posts/:post` | DELETE | Deletes a post or thread. If the specified post is a thread, all replies to it are also deleted. Requires basic authorization headers to be present.


## GET `/boards/`
Returns an array of all boards. The `Board` response model is used.

### Example
#### Response
```json
{
  "status": "Success",
  "data": [
    {
      "name": "Television",
      "abbreviation": "tv"
    },
    {
      "name": "Random",
      "abbreviation": "b",
      "description": "Post whatever here."
    }
  ]
}
```

## POST `/boards/`
Creates a new board. The `Board` response model is used.

### Parameters
Key | Required | Description
---- | -------- | --------
`name` | required | Name of the board
`abbreviation` | required | Abbreviation of the board's name
`description` | optional | Short description of the board

### Example

#### Request
```
{
    "name": "Science",
    "abbreviation": "sci",
    "description": "Nerds!"
}
```
#### Response
```
{
  "status": "Success",
  "data": {
    "name": "Science",
    "abbreviation": "sci",
    "description": "Nerds!"
  }
}
```

## GET `/boards/:abbreviation`
Gets a specific board. The `Board` response model is used.

## DELETE `/boards/:abbreviation`
Deletes a board. Requires basic authorization headers to be present.

### Example

#### Request
```
DELETE /boards/b
```
#### Request headers
Key | Value
----|------
`Authorization` | `Basic YWRtaW46MTIzNA==`

#### Response
```
{
  "status": "Success"
}
```

## GET `/boards/:abbreviation/posts/`
Gets threads of a board. The `Post` response model is used.

### Parameters

Key | Required | Description
----|----------|-----------
`start` | optional | **Number**. If specified, a paginated response will be sent, containing threads after the specified starting offset. The amount of threads in a page is a constant 10.

### Example
#### Request
```
GET /boards/b/posts/
```
#### Response
```
{
  "status": "Success",
  "data": [
    {
      "id": 2,
      "ip": "31AC1058",
      "post_time": 1477931440,
      "subject": "Hello world!",
      "message": "Liirum laarum"
    },
    {
      "id": 3,
      "ip": "31AC1058",
      "post_time": 1477931491,
      "subject": "",
      "message": "How's it going everyone?"
    }
  ]
}
```

### Example, pagination
#### Request
```
GET /boards/b/posts/?start=10
```
#### Response
```
{
  "status": "Success",
  "data": {
    "total_count": 12,
    "posts": [
      {
        "id": 37,
        "ip": "31AC1058",
        "post_time": 1477944654,
        "subject": "",
        "message": "What do you guys think about the latest Samsung?"
      },
      {
        "id": 38,
        "ip": "31AC1058",
        "post_time": 1477944670,
        "subject": "",
        "message": "/b/iz general thread - get in here"
      }
    ]
  }
}
```

## POST `/boards/:abbreviation/posts/`
Creates a new thread on the specified board. The `Post` response model is used.

### Parameters
Key | Required | Description
----|----------|------------
`subject` | optional | Subject line of the thread.
`message` | required | Message of the first post in the thread.

### Example
#### Request
`POST /boards/b/posts/`
```
{
    "subject": "Hello world",
    "message": "I'm using tilt controlls!"
}
```
#### Response
```
{
  "status": "Success",
  "data": {
    "id": 39,
    "board_abbrevition": "b",
    "ip": "F1DB2283",
    "post_time": 1477953596,
    "subject": "Hello world",
    "message": "I'm using tilt controlls!"
  }
}
```

## GET `/boards/:abbreviation/posts/topics/:topic`
Gets all replies to a thread. `topic` is the post ID of the starting message.

## POST `/boards/:abbreviation/posts/topics/:topic`
Creates a reply to a thread. The `Post` response model is used.

### Parameters
Key | Required | Description
----|----------|------------
`subject` | optional | Subject line of the reply.
`message` | required | Message.

### Example
#### Request
`POST /boards/b/posts/4`
```
{
    "subject": "",
    "message": ">> 4\nNo, that's just plain wrong."
}
```
#### Response
```
{
  "status": "Success",
  "data": {
    "id": 39,
    "board_abbrevition": "b",
    "ip": "F1DB2283",
    "post_time": 1477953596,
    "subject": "Hello world",
    "message": "I'm using tilt controlls!"
  }
}
```

## DELETE `/posts/:post`
Deletes a post or thread. If the specified post is a thread, all replies to it are also deleted. Requires basic authorization headers to be present.

### Example

#### Request
```
DELETE /posts/3
```
#### Request headers
Key | Value
----|------
`Authorization` | `Basic YWRtaW46MTIzNA==`

#### Response
```
{
  "status": "Success"
}
```

