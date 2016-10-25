package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;
import fi.tekislauta.models.DatabaseObject;
import fi.tekislauta.models.Post;
import fi.tekislauta.models.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;

public class PostDao implements DatabaseObject{

    public PostDao() {


    }

    @Override
    public Object fetch(Database db, String filter) throws SQLException {
        Result r = new Result();
        try {
            PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Post p WHERE  p.id= ?");
            statement.setString(1, filter);

            ResultSet rs = statement.executeQuery();

            Post p = new Post();
            if (!rs.next()) {
                r.setStatus("OH NO! Cannot find post " + filter + " :(");
                return r;
            }

            p.setId(rs.getInt("id"));
            p.setTopic_id((Integer) rs.getObject("topic_id"));
            p.setIp(rs.getString("ip"));
            p.setPost_time(rs.getInt("post_time"));
            p.setSubject(rs.getString("subject"));
            p.setMessage(rs.getString("message"));

            r.setData(p);

            return r;
        } catch (Exception e) {
            r.setStatus("Server error");
            return r;
        }
    }

    @Override
    public Object fetchAll(Database db, String board) throws SQLException {
        Result r = new Result();
        try {
            PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Post WHERE board_abbreviation = ? AND topic_id IS NULL");
            statement.setString(1, board);
            ResultSet rs = statement.executeQuery();

            ArrayList<Post> postList = new ArrayList<>();

            while (rs.next()) {
                Post p = new Post();
                p.setId(rs.getInt("id"));
                p.setTopic_id((Integer) rs.getObject("topic_id"));
                p.setIp(rs.getString("ip"));
                p.setPost_time(rs.getInt("post_time"));
                p.setSubject(rs.getString("subject"));
                p.setMessage(rs.getString("message"));
                postList.add(p);
            }
            r.setData((List) postList);
            return r;
        } catch (Exception e) {
            r.setStatus("Server error");
            return r;
        }
    }

    public Object fetchByTopic(Database db, String board, String topic) throws SQLException {
        Result r = new Result();
        try {
            PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Post p WHERE p.board_abbreviation = ? AND (p.topic_id = ? OR p.id = ?)");
            statement.setString(1, board);
            statement.setInt(2, Integer.parseInt(topic));
            statement.setInt(3, Integer.parseInt(topic));
            ResultSet rs = statement.executeQuery();

            ArrayList<Post> postList = new ArrayList<>();

            while (rs.next()) {
                Post p = new Post();

                p.setId(rs.getInt("id"));
                p.setTopic_id((Integer) rs.getObject("topic_id"));
                p.setIp(rs.getString("ip"));
                p.setPost_time(rs.getInt("post_time"));
                p.setSubject(rs.getString("subject"));
                p.setMessage(rs.getString("message"));

                postList.add(p);
            }

            r.setData((List) postList);

            return r;
        } catch (Exception e) {
            r.setStatus("Server error");
            return r;
        }
    }

    @Override
    public Object post(Database db, Object o) throws SQLException {
        Result r = new Result();
        try {
            PreparedStatement statement = db.getConnection().prepareStatement("INSERT INTO Post (board_abbreviation, topic_id, ip, subject, message) VALUES (?, ?, ?, ?, ?)");
            Post p = (Post) o;
            statement.setString(1, p.getBoard_abbreviation());
            if (p.getTopic_id() == null)
                statement.setNull(2, Types.INTEGER);
            else
                statement.setInt(2, p.getTopic_id());
            statement.setString(3, p.getIp());
            statement.setString(4, p.getSubject());
            statement.setString(5, p.getMessage());


            int rs = statement.executeUpdate();
            r.setData(p);
            return r;
        } catch(SQLException e) {
            r.setStatus("Server Error");
            return r;
        }
    }

    @Override
    public Object delete(Database db, String params) throws SQLException {
        Result r = new Result();
        try {
            PreparedStatement statement = db.getConnection().prepareStatement("DELETE FROM Post WHERE id = ?");
            statement.setInt(1, Integer.parseInt(params));
            return r;
        } catch (Exception e) {
            r.setStatus("Server error");
            return r;
        }
    }
}
