package fi.tekislauta.db;

import fi.tekislauta.models.DatabaseObject;

import java.io.*;
import java.sql.*;

public class Database {
    private final String SCHEMA = "PRAGMA foreign_keys = ON;CREATE TABLE Board( name varchar(64) NOT NULL, abbreviation varchar(4) NOT NULL, description varchar(1024) NULL, PRIMARY KEY (abbreviation));CREATE TABLE Post( id integer, board_abbreviation varchar(4) NOT NULL, topic_id integer NULL, ip varchar(16) NOT NULL, post_time integer(4) NOT NULL DEFAULT (strftime('%s', 'now')), subject varchar(128) NULL, message text NOT NULL, PRIMARY KEY (id), FOREIGN KEY (board_abbreviation) REFERENCES Board(abbreviation), FOREIGN KEY (topic_id) REFERENCES Post(id));";
    private final String DB_PATH = "tekislauta.sql";
    private Connection _dbConn = null;
    private Statement _dbStmt = null;

    public Database() {
        connect();
    }

    void connect() {
        try {
            if (System.getenv("DATABASE_URL") != null)
                this._dbConn = DriverManager.getConnection(System.getenv("DATABASE_URL"));
            else
                this._dbConn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
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

    // TODO: Read schema from file
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


