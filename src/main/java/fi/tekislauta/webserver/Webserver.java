package fi.tekislauta.webserver;

import com.google.gson.Gson;
import fi.tekislauta.db.Database;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.Post;
import static spark.Spark.*;

import static spark.Spark.get;

public class Webserver {


    public Webserver() {

        port(1234);

        Gson gson = new Gson();
        Database db = new Database();

        get("/api/boards/:abbreviation", (req, res) -> {
            res.header("Content-Type","application/json; charset=utf-8");
            Board b = new Board();
            db.resolve(b,req.params("abbreviation"));
            return gson.toJson(b);
        });

    }
}
