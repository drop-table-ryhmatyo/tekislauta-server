package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;
import fi.tekislauta.models.DatabaseObject;
import fi.tekislauta.models.Post;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;

public class PostDao implements DatabaseObject{

    public PostDao() {


    }

    @Override
    public Object fetch(Database db, String filter) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Board b, Post p WHERE b.id = p.board_ip AND p.id= ?");
        statement.setString(1, filter);

        ResultSet rs = statement.executeQuery();

        Post p = new Post();
        if (!rs.next()) {
            p.setError("OH NO! Cannot find post " + filter + " :(");
            return p;
        }

        return p;
    }

    @Override
    public List<Object> fetchAll(Database db) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Board b, Post p WHERE b.id = p.board_ip");
        ResultSet rs = statement.executeQuery();

        ArrayList<Post> postList = new ArrayList<>();

        while(rs.next()) {
            Post p = new Post();

            p.setId(rs.getInt("id"));
            p.setTopic_id((Integer)rs.getObject("topic_id") );
            p.setIp(rs.getInt("ip"));
            p.setPost_time(rs.getInt("post_time"));
            p.setSubject(rs.getString("subject"));
            p.setMessage(rs.getString("message"));

            postList.add(p);
        }

        return (List)postList;
    }

    @Override
    public Object post(Database db, Object o) throws SQLException {
        return null;
    }

    @Override
    public void delete(Database db, String params) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement("DELETE FROM Post WHERE id = ?");
        statement.setObject(1, params);
        ResultSet rs = statement.executeQuery();


    }
}
