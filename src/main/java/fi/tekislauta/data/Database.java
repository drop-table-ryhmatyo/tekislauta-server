package fi.tekislauta.data;

import java.io.*;
import java.sql.*;

public class Database {
    private static final String SCHEMA = "PRAGMA foreign_keys = ON;CREATE TABLE Board( name varchar(64) NOT NULL, abbreviation varchar(4) NOT NULL, description varchar(1024) NULL, PRIMARY KEY (abbreviation));CREATE TABLE Post( id integer, board_abbreviation varchar(4) NOT NULL, topic_id integer NULL, ip varchar(16) NOT NULL, post_time integer(4) NOT NULL DEFAULT (strftime('%s', 'now')), subject varchar(128) NULL, message text NOT NULL, PRIMARY KEY (id), FOREIGN KEY (board_abbreviation) REFERENCES Board(abbreviation), FOREIGN KEY (topic_id) REFERENCES Post(id));";
    private final String dbUrl;
    private Connection _dbConn = null;
    private Statement _dbStmt = null;

    public Database(String url) {
        this.dbUrl = url;
        connect();
    }

    void connect() {
        try {
            this._dbConn = DriverManager.getConnection(this.dbUrl);
            this._dbStmt = this._dbConn.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Check if required tables exist
        if (this._dbConn != null) {
            try {
                DatabaseMetaData dbm = this._dbConn.getMetaData();
                ResultSet boardRes = dbm.getTables(null, null, "Board", null);
                ResultSet postRes = dbm.getTables(null, null, "Post", null);

                if (!boardRes.next() || !postRes.next()) {
                    // Tables do not exist, so let's create them!
                    this._dbStmt.executeUpdate(SCHEMA); // Reading the SCHEMA_PATH file.
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    String readSchema() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(this.getClass().getResource("").getFile()));
        String res = "";
        String line;
        // Loop every line
        while ((line = reader.readLine()) != null) {
            res += line;
        }

        return res;
    }

    public Connection getConnection() {
        return this._dbConn;
    }
}


