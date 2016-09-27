/**
 * Created by Hugo on 27.9.2016.
 */

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        port(80);
        get("/api", (req, res) -> {
            res.header("content-type","application/json");
            return "Hello World";
        });
    }
}
