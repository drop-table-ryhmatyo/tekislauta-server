package fi.tekislauta;

import static spark.Spark.*;
import fi.tekislauta.db.*;

public class Main {
    public static void main(String[] args) {
        port(8080);

        Database db = new Database();
        get("/api", (req, res) -> {
            return db.executeQuery("SELECT * FROM Board");
        });

        get("/api/boards/:id", (req,res) -> {
            return db.executeQuery("SELECT * FROM Board WHERE abbreviation='" + req.params("id") + "'");
        });

    }
}
