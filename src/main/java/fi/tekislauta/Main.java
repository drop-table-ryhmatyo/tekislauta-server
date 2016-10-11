package fi.tekislauta;

import static spark.Spark.*;
import fi.tekislauta.db.*;
import com.google.gson.*;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.Post;
import fi.tekislauta.webserver.Webserver;

public class Main {
    public static void main(String[] args) {

        new Webserver();
    }
}
