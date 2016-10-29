package fi.tekislauta;

import fi.tekislauta.webserver.WebServer;

public class Main {
    public static void main(String[] args) {
        System.out.println("HELLO");
        WebServer server = new WebServer(getHerokuAssignedPort());
        server.listen();
    }

    static int getHerokuAssignedPort() {
        if (System.getenv("PORT") != null)
            return Integer.parseInt(System.getenv("PORT"));
        return 1234;
    }
}
