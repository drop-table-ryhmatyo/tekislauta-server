package fi.tekislauta;

import static spark.Spark.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        port(80);
        get("/api", (req, res) -> {
            return "asd";
        });

        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:db/tekislauta.db");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }
}
