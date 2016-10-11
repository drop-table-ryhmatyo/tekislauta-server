package fi.tekislauta.models;

import fi.tekislauta.db.Database;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Board implements Resolvable{

    private int id;
    private String name;
    private String abbreviation;
    private String description;
    private List<Post> threads;

    public Board(int id, String name, String abbreviation, String description) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.description = description;
        this.threads = new ArrayList<>();
    }

    public Board() {
        this.threads = new ArrayList<>();
    }

    public void addThread(Post post) {
        this.threads.add(post);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public Object resolve(Database db, String abbreviation) throws SQLException {

        PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Board WHERE name= ?");
        statement.setString(1, abbreviation);

        ResultSet rs = statement.executeQuery();

        if (!rs.next())
            return null;

        this.id = rs.getInt(1);
        this.name = rs.getString(2);
        this.abbreviation = abbreviation;
        this.description = rs.getString(4);

        PreparedStatement postStatement = db.getConnection().prepareStatement("SELECT * FROM Post WHERE board_id =  ?");
        postStatement.setInt(1, this.id);

        ResultSet postRes = postStatement.executeQuery();
        while (postRes.next()) {
                Post p = new Post(
                    postRes.getInt(1),
                    (Integer)postRes.getObject("topic_id"),
                    postRes.getInt(4),
                    postRes.getInt(5),
                    postRes.getString(6),
                    postRes.getString(7)
                );
                this.threads.add(p);
            }
        return this;
    }
}
