package fi.tekislauta.data;

import java.io.*;
import java.sql.*;

public class Database {
    private final String dbUrl;
    private final String SCHEMA_PATH = "/db/Schema.sql";
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
                    try {
                        this._dbStmt.executeUpdate(readSchema()); // Reading the SCHEMA_PATH file.
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    String readSchema() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(SCHEMA_PATH)));
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

