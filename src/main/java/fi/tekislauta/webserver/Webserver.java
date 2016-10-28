package fi.tekislauta.webserver;

import com.google.gson.Gson;
import fi.tekislauta.db.Database;
import fi.tekislauta.db.objects.BoardDao;
import fi.tekislauta.db.objects.PostDao;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.Post;
import fi.tekislauta.models.Result;

import static spark.Spark.*;

import java.util.Map;
import java.util.Base64;

public class Webserver {

    private final int port;
    private final Gson gson;
    private final Database db;
    private final BoardDao boardDao;
    private final PostDao postDao;

    private final String USER = "Klusse";
    private final String PW = "juuhelikkas";

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
                r.setData(boardDao.fetchAll(db, ""));
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
                r.setData(boardDao.fetch(db, req.params("abbreviation")));
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
                r.setData(postDao.fetchAll(db, req.params("board")));
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
                r.setData(postDao.fetchPageTopics(db, req.params("board"), req.params("page")));
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
                r.setData(postDao.fetchByTopic(db, req.params("board"), req.params("topic")));
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
                r.setData(postDao.fetch(db, req.params("id")));
            } catch (Exception e) {
                r.setStatus("Server error: " + e.getMessage());
            }
            return gson.toJson(r);
        });

        post("api/boards/:board/posts/", (req, res) -> {
            Result r = new Result();
            try {
                res.header("Access-Control-Allow-Origin", "*");
                res.header("Content-Type", "application/json; charset=utf-8");
                Map json = gson.fromJson(req.body(), Map.class);
                Post p = new Post();
                p.setBoard_abbrevition(req.params("board"));
                if (json.get("topic_id") != null)
                    p.setTopic_id(Integer.parseInt(json.get("topic_id").toString().split("\\.")[0]));
                else
                    p.setTopic_id(null);
                p.setIp(req.ip());
                if (json.get("message") == null) throw new Exception("No message");
                if (json.get("subject") == null) throw new Exception("No subject");
                String msg = ((String) json.get("message")).trim();
                String subj = ((String) json.get("subject")).trim();
                if (msg.isEmpty() || subj.isEmpty()) throw new Exception("Empty message or subject");
                p.setSubject(subj);
                p.setMessage(msg);
                p.setPost_time(System.currentTimeMillis());
                r.setData(gson.toJson(postDao.post(db, p)));
            } catch (Exception e) {
                r.setStatus("Server error " + e.getMessage());
            }

            return gson.toJson(r);
        });

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
                p.setPost_time(System.currentTimeMillis());
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
                r.setData(postDao.delete(db, req.params("id")));
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
                r.setData(boardDao.delete(db, req.params("id")));
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
}

