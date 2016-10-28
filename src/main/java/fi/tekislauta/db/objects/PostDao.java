package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;
import fi.tekislauta.models.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;
import java.util.Map.Entry;

public class PostDao extends ValidatingDao<Post> implements DatabaseObject {
    private BoardDao boardDao = new BoardDao();

    public PostDao() {}

    @Override
    public Object fetch(Database db, String filter) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Post p WHERE  p.id= ?");
        statement.setString(1, filter);

        ResultSet rs = statement.executeQuery();

        Post p = new Post();

        p.setId(rs.getInt("id"));
        p.setTopic_id((Integer) rs.getObject("topic_id"));
        p.setIp(rs.getString("ip"));
        p.setPost_time(rs.getInt("post_time"));
        p.setSubject(rs.getString("subject"));
        p.setMessage(rs.getString("message"));

        return p;
    }

    @Override
    public List<Object> fetchAll(Database db, String board) throws Exception {
        if (boardDao.fetch(db, board) == null) {
            throw new Exception("Cannot find board " + board);
        }
        PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Post WHERE board_abbreviation = ? AND topic_id IS NULL LIMIT 10");
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
        return (List) postList;
    }

    public List<Object> fetchByTopic(Database db, String board, String topic) throws Exception {
        if (boardDao.fetch(db, board) == null) {
            throw new Exception("Cannot find board " + board);
        }
        PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Post p WHERE p.board_abbreviation = ? AND (p.topic_id = ? OR (p.id = ? AND topic_id IS NULL)");
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

        return (List) postList;
    }

    public List<Object> fetchPageTopics(Database db, String board, String page) throws Exception {
        if (boardDao.fetch(db, board) == null) {
            throw new Exception("Cannot find board " + board);
        }
        if (page.isEmpty()) page = "1";
        int nPage;
        try {
            nPage = Integer.parseInt(page) <= 0 ? 0 : ((Integer.parseInt(page) - 1) * 10);
        } catch (Exception e) {
            throw new Exception("1337");
        }
        PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Post p WHERE p.board_abbreviation = ? AND topic_id IS NULL LIMIT 10 OFFSET " + nPage);
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

        return (List) postList;
    }

    @Override
    public Object post(Database db, Object o) throws Exception {
        PreparedStatement statement = db.getConnection().prepareStatement("INSERT INTO Post (board_abbreviation, topic_id, ip, post_time, subject, message) VALUES (?, ?, ?, ?, ?, ?)");
        Post p = (Post) o;

        if (boardDao.fetch(db, p.getBoard_abbreviation()) == null) {
            throw new Exception("Cannot find board " + p.getBoard_abbreviation());
        }

        statement.setString(1, p.getBoard_abbreviation());
        if (p.getTopic_id() == null)
            statement.setNull(2, Types.INTEGER);
        else
            statement.setInt(2, p.getTopic_id());
        statement.setString(3, p.getIp());
        statement.setString(5, p.getSubject());
        statement.setString(6, p.getMessage());

        statement.executeUpdate();
        return p;
    }

    @Override
    public Object delete(Database db, String params) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement("DELETE FROM Post WHERE id = ?");
        statement.setInt(1, Integer.parseInt(params));
        statement.executeUpdate();
        return null;

    }

    @Override
    protected void validateOnInsert(Post objectToInsert) throws ModelValidationException {
        Map<String, String> propsNotNullOrEmpty = new HashMap<>();
        propsNotNullOrEmpty.put("board abbreviation", objectToInsert.getBoard_abbreviation());
        propsNotNullOrEmpty.put("ip", objectToInsert.getIp());
        propsNotNullOrEmpty.put("message", objectToInsert.getMessage());

        // (board_abbreviation, topic_id, ip, post_time, subject, message)
        // topic_id CAN be null
        // post_time CAN be null?
        // subject CAN be null
        for (Entry<String, String> pair : propsNotNullOrEmpty.entrySet()) {
            if (pair.getValue() == null || pair.getValue().trim().isEmpty()) {
                throw new ModelValidationException(
                    objectToInsert,
                    "The " + pair.getKey() + " of a post cannot be null or empty!"
                );
            }
        }
    }
}
