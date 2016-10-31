package fi.tekislauta.webserver;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import fi.tekislauta.data.Database;
import fi.tekislauta.data.dao.BoardDao;
import fi.tekislauta.data.dao.DaoException;
import fi.tekislauta.data.dao.ModelValidationException;
import fi.tekislauta.data.dao.PostDao;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.Post;
import fi.tekislauta.models.Result;
import spark.Request;

import static spark.Spark.*;

import java.io.PrintStream;
import java.util.List;
import java.util.Base64;

public class WebServer {

    private final int port;
    private final Gson gson;
    private final BoardDao boardDao;
    private final PostDao postDao;

    private final String USER = System.getenv("USER");
    private final String PW = System.getenv("PW");

    public WebServer(int port) {
        this.port = port;
        this.gson = new Gson();
        Database db = new Database(getDbUrl());
        this.boardDao = new BoardDao(db);
        this.postDao = new PostDao(db);
    }

    public void listen() {
        port(this.port);


        before((req, res) -> {
            res.header("Content-Type", "application/json; charset=utf-8");
        });

        exception(ModelValidationException.class, (exception, req, res) -> {
            res.status(400); // "Bad request"
            Result r = Result.error("Malformed request or missing data! " + exception.getMessage());
            res.body(gson.toJson(r));
        });

        exception(JsonSyntaxException.class, (ex, req, res) -> {
            res.status(400); // "Bad request"
            Result r = Result.error("Malformed request! Please check your JSON syntax!");
            res.body(gson.toJson(r));
        });

        exception(DaoException.class, (exception, req, res) -> {
            dumpRequestException(req, exception);
            res.status(500); // "Internal server error"
            Result r = Result.error("A server error has occurred.");
            res.body(gson.toJson(r));
        });


        // spark starts listening when first method listener is added, I think ~cx
        get("/api/boards/", (req, res) -> {
            return Result.success(boardDao.findAll(""));
        }, gson::toJson);

        get("/api/boards/:abbreviation", (req, res) -> {
            Board b = boardDao.find(req.params("abbreviation"));
            if (b == null)
                return Result.error("Board \"" + req.params("abbreviation") + "\" not found!");
            else
                return Result.success(b);
        }, gson::toJson);

        get("/api/boards/:board/posts/", (req, res) -> {
            return Result.success(postDao.findAll(req.params("board")));
        }, gson::toJson);

        get("/api/boards/:board/posts/:page", (req, res) -> {
            return Result.success(postDao.findPageTopics(req.params("board"), req.params("page")));
        }, gson::toJson);

        get("/api/jerry", (req, res) -> {
            return Result.success("\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40 good shit go౦ԁ sHit\uD83D\uDC4C thats ✔ some good\uD83D\uDC4C\uD83D\uDC4Cshit right\uD83D\uDC4C\uD83D\uDC4Cthere\uD83D\uDC4C\uD83D\uDC4C\uD83D\uDC4C right✔there ✔✔if i do ƽaү so my self \uD83D\uDCAF i say so \uD83D\uDCAF thats what im talking about right there right there (chorus: ʳᶦᵍʰᵗ ᵗʰᵉʳᵉ) mMMMMᎷМ\uD83D\uDCAF \uD83D\uDC4C\uD83D\uDC4C \uD83D\uDC4CНO0ОଠOOOOOОଠଠOoooᵒᵒᵒᵒᵒᵒᵒᵒᵒ\uD83D\uDC4C \uD83D\uDC4C\uD83D\uDC4C \uD83D\uDC4C \uD83D\uDCAF \uD83D\uDC4C \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC4C\uD83D\uDC4CGood shit");
        }, gson::toJson);

        get("/api/boards/:board/posts/topics/:topic", (req, res) -> {
            List<Post> posts = postDao.findByTopic(req.params("board"), req.params("topic"));
            return Result.success(posts);
        }, gson::toJson);

        get("/api/posts/:id", (req, res) -> {
            return Result.success(postDao.find(req.params("id")));
        }, gson::toJson);


        post("api/boards/:board/posts/", (req, res) -> {
            // This endpoint creates new topics. Responses go to POST api/boards/:board/posts/topics/:topic

            Post post = gson.fromJson(req.body(), Post.class); // should parse subject and message
            if (post.getTopic_id() != null) {
                // This endpoint only creates new topics. No replies. Nada. noty.
                res.status(400);
                return Result.error(
                    "This endpoint creates new topics." +
                    "Topic replies go to POST api/boards/:board/posts/topics/:topic"
                );
            }

            post.setBoard_abbrevition(req.params("board"));
            post.setIp(req.ip());
            post.setPost_time(getUnixTimestamp());
            return Result.success(postDao.post(post));
        }, gson::toJson);

        post("api/boards/:board/posts/topics/:topic", (req, res) -> {
            Post p = gson.fromJson(req.body(), Post.class); // message and optionally subject should be there now
            p.setBoard_abbrevition(req.params("board"));
            p.setTopic_id(tryParseInteger(req.params("topic")));
            p.setIp(req.ip());
            p.setPost_time(getUnixTimestamp());
            return Result.success(postDao.post(p));
        }, gson::toJson);

        post("/api/boards/", (req, res) -> {
            Board b = gson.fromJson(req.body(), Board.class);
            return Result.success(boardDao.post(b));
        }, gson::toJson);


        delete("api/posts/:id", (req, res) -> {
            String authHeader = req.headers("Authorization");
            if (authHeader == null || !isAuthorized(authHeader)) {
                res.status(401); // unauthorized
                return Result.unauthorized(null);
            }

            postDao.delete(req.params("id"));
            return Result.success(null);
        }, gson::toJson);

        delete("api/boards/:id", (req, res) -> {
            String authHeader = req.headers("Authorization");
            if (authHeader == null || !isAuthorized(authHeader)) {
                res.status(401); // unauthorized
                return Result.unauthorized(null);
            }

            boardDao.delete(req.params("id"));
            return Result.success(null);
        }, gson::toJson);
    }

    private static String getDbUrl() {
        final String defaultUrl = "jdbc:sqlite:db:tekislauta.db";
        String url = System.getenv("DATABASE_URL");
        return url == null ? defaultUrl : url;
    }

    private static int getUnixTimestamp() {
        return (int)(System.currentTimeMillis() / 1000); // we deal with seconds around here
    }

    private static void dumpRequestException(Request request, Exception e) {
        System.err.println("Error!");
        System.err.println("Requested path: " + request.pathInfo());
        System.err.printf("Request body:%n%s%n", request.body());
        System.err.print("Exception: ");
        e.printStackTrace(new PrintStream(System.err));
    }

    private static Integer tryParseInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean isAuthorized(String hdr) {
        byte[] decryptedHeader = Base64.getDecoder().decode(hdr.split(" ")[1]);
        String[] credentials = new String(decryptedHeader).split(":");
        return (credentials[0].equals(USER) && credentials[1].equals(PW));
    }
}

