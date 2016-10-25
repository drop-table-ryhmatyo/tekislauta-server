package fi.tekislauta.models;

import fi.tekislauta.db.Database;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Board extends Result {

    private String name;
    private String abbreviation;
    private String description;

    public Board(int id, String name, String abbreviation, String description) {
        super(null);
        this.name = name;
        this.abbreviation = abbreviation;
        this.description = description;
    }

    // Nullify all members for a clean JSON object
    public void setError(String e) {
        this.name = null;
        this.abbreviation = null;
        this.description = null;
        super.setError(e);
    }

    public Board() {
        super(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
