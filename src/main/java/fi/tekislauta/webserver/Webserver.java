package fi.tekislauta.webserver;

import com.google.gson.Gson;
import com.google.gson.stream.MalformedJsonException;
import fi.tekislauta.db.Database;
import fi.tekislauta.db.objects.BoardDao;
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
    private final Database db;
    private final BoardDao boardDao;
    private final PostDao postDao;

    private final String USER = System.getenv("USER");
    private final String PW = System.getenv("PW");

    public Webserver(int port) {
        this.port = port;
        this.gson = new Gson();
        this.db = new Database();
        this.boardDao = new BoardDao();
        this.postDao = new PostDao();
    }

    public void listen() {
        port(this.port);

        // spark starts listening when first method listener is added, I think ~cx
        get("/api/boards/", (req, res) -> {
            Result r = new Result();
            try {
                res.header("Content-Type", "application/json; charset=utf-8");
                res.header("Access-Control-Allow-Origin", "*");
                r.setData(boardDao.findAll(db, ""));
            } catch (Exception e) {
                r.setStatus("Server error: " + e.getMessage());
            }
            return gson.toJson(r);
        });

        get("/api/boards/:abbreviation", (req, res) -> {
            Result r = new Result();
            try {
                res.header("Content-Type", "application/json; charset=utf-8");
                res.header("Access-Control-Allow-Origin", "*");
                r.setData(boardDao.find(db, req.params("abbreviation")));
            } catch (Exception e) {
                r.setStatus("Server error: " + e.getMessage());
            }
            return gson.toJson(r);
        });

        get("/api/boards/:board/posts/", (req, res) -> {
            Result r = new Result();
            try {
                res.header("Content-Type", "application/json; charset=utf-8");
                res.header("Access-Control-Allow-Origin", "*");
                r.setData(postDao.findAll(db, req.params("board")));
            } catch (Exception e) {
                r.setStatus("Server error: " + e.getMessage());
            }
            return gson.toJson(r);
        });

        get("/api/boards/:board/posts/:page", (req, res) -> {
            Result r = new Result();
            try {
                res.header("Content-Type", "application/json; charset=utf-8");
                res.header("Access-Control-Allow-Origin", "*");
                r.setData(postDao.findPageTopics(db, req.params("board"), req.params("page")));
            } catch (Exception e) {
                r.setStatus("Server error: " + e.getMessage());
            }
            return gson.toJson(r);
        });

        get("/api/jerry", (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            return "\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40 good shit go౦ԁ sHit\uD83D\uDC4C thats ✔ some good\uD83D\uDC4C\uD83D\uDC4Cshit right\uD83D\uDC4C\uD83D\uDC4Cthere\uD83D\uDC4C\uD83D\uDC4C\uD83D\uDC4C right✔there ✔✔if i do ƽaү so my self \uD83D\uDCAF i say so \uD83D\uDCAF thats what im talking about right there right there (chorus: ʳᶦᵍʰᵗ ᵗʰᵉʳᵉ) mMMMMᎷМ\uD83D\uDCAF \uD83D\uDC4C\uD83D\uDC4C \uD83D\uDC4CНO0ОଠOOOOOОଠଠOoooᵒᵒᵒᵒᵒᵒᵒᵒᵒ\uD83D\uDC4C \uD83D\uDC4C\uD83D\uDC4C \uD83D\uDC4C \uD83D\uDCAF \uD83D\uDC4C \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC4C\uD83D\uDC4CGood shit";
        });

        get("/api/boards/:board/posts/topics/:topic", (req, res) -> {
            Result r = new Result();
            try {
                res.header("Access-Control-Allow-Origin", "*");
                res.header("Content-Type", "application/json; charset=utf-8");
                r.setData(postDao.findByTopic(db, req.params("board"), req.params("topic")));
            } catch (Exception e) {
                r.setStatus("Server error: " + e.getMessage());
            }
            return gson.toJson(r);
        });

        get("/api/posts/:id", (req, res) -> {
            Result r = new Result();
            try {
                res.header("Access-Control-Allow-Origin", "*");
                res.header("Content-Type", "application/json; charset=utf-8");
                r.setData(postDao.find(db, req.params("id")));
            } catch (Exception e) {
                r.setStatus("Server error: " + e.getMessage());
            }
            return gson.toJson(r);
        });

        post("api/boards/:board/posts/", (req, res) -> {
            // This endpoint creates new topics. Responses go to POST api/boards/:board/posts/topics/:topic
            Result r = new Result();
            try {
                res.header("Access-Control-Allow-Origin", "*");
                res.header("Content-Type", "application/json; charset=utf-8");

                Map json = gson.fromJson(req.body(), Map.class);
                if (json.get("topic_id") != null) {
                    // This endpoint only creates new topics. No replies. Nada. noty.
                    res.status(400);
                    r.setStatus("Error");
                    r.setData("This endpoint creates new topics. Topic replies go to POST api/boards/:board/posts/topics/:topic");
                    return r;
                }
                Post p = new Post();
                p.setBoard_abbrevition(req.params("board"));
                p.setIp(req.ip());
                p.setSubject(((String) json.get("subject")).trim());
                p.setMessage(((String) json.get("message")).trim());
                p.setPost_time(getUnixTimestamp());

                r.setData(gson.toJson(postDao.post(db, p)));
            } catch (MalformedJsonException | ModelValidationException e) {
                res.status(400);
                r.setStatus("Error");
                r.setData("Bad request: " + e.getMessage());
            } catch (Exception e) {
                res.status(500);
                r.setStatus("Error");
                r.setStatus("Server error: " + e.getMessage());
                dumpRequestException(req, e);
            }

            return r;
        }, gson::toJson);

        post("api/boards/:board/posts/topics/:topic", (req, res) -> {
            Result r = new Result();
            try {
                res.header("Access-Control-Allow-Origin", "*");
                res.header("Content-Type", "application/json; charset=utf-8");
                Map json = gson.fromJson(req.body(), Map.class);
                Post p = new Post();
                p.setBoard_abbrevition(req.params("board"));
                p.setTopic_id(Integer.parseInt(req.params("topic")));
                p.setIp(req.ip());
                p.setSubject((String) json.get("subject"));
                p.setMessage((String) json.get("message"));
                p.setPost_time(getUnixTimestamp());
                r.setData(postDao.post(db, p));
            } catch (Exception e) {
                r.setStatus("Server error: " + e.getMessage());
            }

            return gson.toJson(r);
        });

        post("/api/boards/", (req, res) -> {
            Result r = new Result();
            try {
                res.header("Access-Control-Allow-Origin", "*");
                res.header("Content-Type", "application/json; charset=utf-8");
                Map json = gson.fromJson(req.body(), Map.class);
                Board b = new Board();
                b.setName((String) json.get("name"));
                b.setAbbreviation((String) json.get("abbreviation"));
                b.setDescription((String) json.get("description"));
                r.setData(boardDao.post(db, b));
            } catch (Exception e) {
                r.setStatus("Server error: " + e.getMessage());
            }
            return gson.toJson(r);
        });

        delete("api/posts/:id", (req, res) -> {
            Result r = new Result();
            try {
                if (!isAuthrorized(req.headers("Authorization"))) throw new Exception("Unauthorized");
                res.header("Access-Control-Allow-Origin", "*");
                res.header("Content-Type", "application/json; charset=utf-8");
                postDao.delete(db, req.params("id"));
            } catch (Exception e) {
                r.setStatus("Server error: " + e.getMessage());
            }
            return gson.toJson(r);
        });

        delete("api/boards/:id", (req, res) -> {
            Result r = new Result();
            try {
                if (!isAuthrorized(req.headers("Authorization"))) throw new Exception("Unauthorized");
                res.header("Access-Control-Allow-Origin", "*");
                res.header("Content-Type", "application/json; charset=utf-8");
                boardDao.delete(db, req.params("id"));
            } catch (Exception e) {
                r.setStatus("Server error: " + e.getMessage());
            }
            return gson.toJson(r);
        });
    }

    boolean isAuthrorized(String hdr) {
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

