package fi.tekislauta.webserver;

import com.google.gson.Gson;
import fi.tekislauta.db.Database;
import fi.tekislauta.db.objects.BoardDao;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.DatabaseObject;

import static spark.Spark.*;

import static spark.Spark.get;

import java.util.Map;

public class Webserver {

    private final int port;
    private final Gson gson;
    private final Database db;

    public Webserver(int port) {
        this.port = port;
        this.gson = new Gson();
        this.db = new Database();
    }

    public void listen() {
        port(this.port);

        // spark starts listening when first method listener is added, I think ~cx
        get("/api/boards/", (req, res) -> {
            res.header("Content-Type","application/json; charset=utf-8");
            return gson.toJson(new BoardDao().fetchAll(db));
        });

        get("/api/boards/:abbreviation", (req, res) -> {
            res.header("Content-Type","application/json; charset=utf-8");
            return gson.toJson(new BoardDao().fetch(db, req.params("abbreviation")));
        });

        get("/api/boards/posts/:abbreviation", (req, res) -> {
            res.header("Content-Type","application/json; charset=utf-8");
            return "";
        });

        post("/api/boards/", (req, res) -> {
            res.header("Content-Type","application/json; charset=utf-8");
            Map json = gson.fromJson(req.body(), Map.class);
            Board b = new Board();
            b.setName((String)json.get("name"));
            b.setAbbreviation((String)json.get("abbreviation"));
            b.setDescription((String)json.get("description"));
            return gson.toJson(new BoardDao().post(db, b));
        });
    }
}
