package fi.tekislauta;

import fi.tekislauta.webserver.Webserver;

public class Main {
    public static void main(String[] args) {

        Webserver server = new Webserver(getHerokuAssignedPort());
        server.listen();
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 1234; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
