package fi.tekislauta;

import static spark.Spark.*;
import fi.tekislauta.db.*;
import com.google.gson.*;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.Post;

public class Main {
    public static void main(String[] args) {
        port(1234);

        Gson gson = new Gson();

        Database db = new Database();
        get("/api", (req, res) -> {
            return db.executeQuery("SELECT * FROM Board");
        });

        get("/api/boards/:id", (req,res) -> {
            return db.executeQuery("SELECT * FROM Board WHERE abbreviation='" + req.params("id") + "'");
        });

        get("/api/posts/:id", (req,res) -> {
            return "Hello world!";
        });

        get("/api/test", (req, res) -> {
            res.header("Content-Type","application/json");
            Board b = new Board(1, "Koulu", "Kumpula", "uliopisto");
            b.addPost(new Post(1,1,12345,123,"Joonan kissa", "Joonan kissat ovat ruotsalaisia"));


            b.addPost(new Post(2,2,12345,123,"Joonan kissa", "Joonan kissat ovat ruotsalaisia"));

            return gson.toJson(b);

        });
    }
}
