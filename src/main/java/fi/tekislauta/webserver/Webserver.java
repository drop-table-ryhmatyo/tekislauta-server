package fi.tekislauta.webserver;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import fi.tekislauta.db.Database;
import fi.tekislauta.db.objects.BoardDao;
import fi.tekislauta.db.objects.DaoException;
import fi.tekislauta.db.objects.ModelValidationException;
import fi.tekislauta.db.objects.PostDao;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.Post;
import fi.tekislauta.models.Result;
import spark.Request;

import static spark.Spark.*;

import java.io.PrintStream;
import java.util.Map;
import java.util.Base64;

public class Webserver {

    private final int port;
    private final Gson gson;
    //private final Database db;
    private final BoardDao boardDao;
    private final PostDao postDao;

    private final String USER = System.getenv("USER");
    private final String PW = System.getenv("PW");

    public Webserver(int port) {
        this.port = port;
        this.gson = new Gson();
        Database db = new Database();
        this.boardDao = new BoardDao(db);
        this.postDao = new PostDao(db);
    }

    public void listen() {
        port(this.port);

        exception(ModelValidationException.class, (exception, req, res) -> {
            res.status(400); // "Bad request"
            Result r = new Result();
            r.setStatus("Error");
            r.setData("Malformed request or missing data! " + exception.getMessage());
            res.body(gson.toJson(r));
        });

        exception(JsonSyntaxException.class, (ex, req, res) -> {
            res.status(400); // "Bad request"
            Result r = new Result();
            r.setStatus("Error");
            r.setData("Malformed request! Please check your JSON syntax!");
        });

        exception(DaoException.class, (exception, req, res) -> {
            res.status(500); // "Internal server error"
            Result r = new Result();
            r.setStatus("Error");
            r.setData("A server error has occurred.");
            dumpRequestException(req, exception);
            res.body(gson.toJson(r));
        });

        // spark starts listening when first method listener is added, I think ~cx
        get("/api/boards/", (req, res) -> {
            res.header("Content-Type", "application/json; charset=utf-8");
            Result r = new Result();

            r.setData(boardDao.findAll(""));
            return gson.toJson(r);
        });

        get("/api/boards/:abbreviation", (req, res) -> {
            res.header("Content-Type", "application/json; charset=utf-8");
            Result r = new Result();

            r.setData(boardDao.find(req.params("abbreviation")));
            return gson.toJson(r);
        });

        get("/api/boards/:board/posts/", (req, res) -> {
            res.header("Content-Type", "application/json; charset=utf-8");
            Result r = new Result();

            r.setData(postDao.findAll(req.params("board")));
            return gson.toJson(r);
        });

        get("/api/boards/:board/posts/:page", (req, res) -> {
            res.header("Content-Type", "application/json; charset=utf-8");
            Result r = new Result();

            r.setData(postDao.findPageTopics(req.params("board"), req.params("page")));
            return gson.toJson(r);
        });

        get("/api/jerry", (req, res) -> {

            return "\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40 good shit go౦ԁ sHit\uD83D\uDC4C thats ✔ some good\uD83D\uDC4C\uD83D\uDC4Cshit right\uD83D\uDC4C\uD83D\uDC4Cthere\uD83D\uDC4C\uD83D\uDC4C\uD83D\uDC4C right✔there ✔✔if i do ƽaү so my self \uD83D\uDCAF i say so \uD83D\uDCAF thats what im talking about right there right there (chorus: ʳᶦᵍʰᵗ ᵗʰᵉʳᵉ) mMMMMᎷМ\uD83D\uDCAF \uD83D\uDC4C\uD83D\uDC4C \uD83D\uDC4CНO0ОଠOOOOOОଠଠOoooᵒᵒᵒᵒᵒᵒᵒᵒᵒ\uD83D\uDC4C \uD83D\uDC4C\uD83D\uDC4C \uD83D\uDC4C \uD83D\uDCAF \uD83D\uDC4C \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC4C\uD83D\uDC4CGood shit";
        });

        get("/api/boards/:board/posts/topics/:topic", (req, res) -> {
            res.header("Content-Type", "application/json; charset=utf-8");
            Result r = new Result();
            r.setData(postDao.findByTopic(req.params("board"), req.params("topic")));
            return gson.toJson(r);
        });

        get("/api/posts/:id", (req, res) -> {
            res.header("Content-Type", "application/json; charset=utf-8");
            Result r = new Result();
            r.setData(postDao.find(req.params("id")));
            return gson.toJson(r);
        });

        post("api/boards/:board/posts/", (req, res) -> {
            // This endpoint creates new topics. Responses go to POST api/boards/:board/posts/topics/:topic
            res.header("Content-Type", "application/json; charset=utf-8");

            Result result = new Result();
            Post post = gson.fromJson(req.body(), Post.class); // should parse subject and message
            if (post.getTopic_id() != null) {
                // This endpoint only creates new topics. No replies. Nada. noty.
                res.status(400);
                result.setStatus("Error");
                result.setData("This endpoint creates new topics. Topic replies go to POST api/boards/:board/posts/topics/:topic");
                return result;
            }

            post.setBoard_abbrevition(req.params("board"));
            post.setIp(req.ip());
            post.setPost_time(getUnixTimestamp());
            result.setData(postDao.post(post));

            return result;
        }, gson::toJson);

        post("api/boards/:board/posts/topics/:topic", (req, res) -> {
            res.header("Content-Type", "application/json; charset=utf-8");

            Result r = new Result();
            Map json = gson.fromJson(req.body(), Map.class);
            Post p = new Post();
            p.setBoard_abbrevition(req.params("board"));
            p.setTopic_id(Integer.parseInt(req.params("topic")));
            p.setIp(req.ip());
            p.setSubject((String) json.get("subject"));
            p.setMessage((String) json.get("message"));
            p.setPost_time(getUnixTimestamp());
            r.setData(postDao.post(p));

            return gson.toJson(r);
        });

        post("/api/boards/", (req, res) -> {
            res.header("Content-Type", "application/json; charset=utf-8");

            Result r = new Result();
            Map json = gson.fromJson(req.body(), Map.class);
            Board b = new Board();
            b.setName((String) json.get("name"));
            b.setAbbreviation((String) json.get("abbreviation"));
            b.setDescription((String) json.get("description"));
            r.setData(boardDao.post(b));
            return gson.toJson(r);
        });

        delete("api/posts/:id", (req, res) -> {
            res.header("Content-Type", "application/json; charset=utf-8");

            Result r = new Result();
            if (!isAuthrorized(req.headers("Authorization"))) {
                res.status(401); // unauthorized
                r.setStatus("Unauthorized");
            }
            postDao.delete(req.params("id"));
            return gson.toJson(r);
        });

        delete("api/boards/:id", (req, res) -> {
            res.header("Content-Type", "application/json; charset=utf-8");

            Result r = new Result();
            if (!isAuthrorized(req.headers("Authorization"))) {
                res.status(401); // unauthorized
                r.setStatus("Unauthorized");
            }
            boardDao.delete(req.params("id"));
            return gson.toJson(r);
        });
    }

    private boolean isAuthrorized(String hdr) {
        byte[] decryptedHeader = Base64.getDecoder().decode(hdr.split(" ")[1]);
        String[] credentials = new String(decryptedHeader).split(":");
        return (credentials[0].equals(USER) && credentials[1].equals(PW));
    }

    private int getUnixTimestamp() {
        return (int)(System.currentTimeMillis() / 1000); // we deal with seconds around here
    }

    private void dumpRequestException(Request request, Exception e) {
        System.err.println("Error!");
        System.err.printf("Request body:%n%s%n", request.body());
        System.err.print("Exception: ");
        e.printStackTrace(new PrintStream(System.err));
    }
}

