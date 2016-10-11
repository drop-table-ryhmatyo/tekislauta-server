package fi.tekislauta.webserver;

import com.google.gson.Gson;
import fi.tekislauta.db.Database;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.Post;
import fi.tekislauta.models.Resolvable;

import static spark.Spark.*;

import static spark.Spark.get;

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
        get("/api/boards/:abbreviation", (req, res) -> {
            res.header("Content-Type","application/json; charset=utf-8");
            Board b = new Board();
            db.resolve(b, req.params("abbreviation"));
            return gson.toJson(b);
        });
    }
}
