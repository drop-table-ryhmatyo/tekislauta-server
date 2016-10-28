package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;
import fi.tekislauta.models.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;
import java.util.Map.Entry;

public class PostDao extends ValidatingDao<Post> implements DataAccessObject<Post, String> {
    private final Database db;
    private final BoardDao boardDao;

    public PostDao(Database db) {
        this.db = db;
        this.boardDao = new BoardDao(db);
    }

    @Override
    public Post find(String filter) throws DaoException {
        try {
            PreparedStatement statement = this.db.getConnection().prepareStatement("SELECT * FROM Post p WHERE  p.id= ?");
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
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public List<Post> findAll(String board) throws DaoException {
        if (boardDao.find(board) == null) {
            throw new DaoException("Cannot find board " + board);
        }

        try {
            PreparedStatement statement = this.db.getConnection().prepareStatement("SELECT * FROM Post WHERE board_abbreviation = ? AND topic_id IS NULL LIMIT 10");
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
            return postList;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    public List<Post> findByTopic(String board, String topic) throws DaoException {
        if (boardDao.find(board) == null) {
            throw new DaoException("Cannot find board " + board);
        }

        try {
            PreparedStatement statement = this.db.getConnection().prepareStatement("SELECT * FROM Post p WHERE p.board_abbreviation = ? AND (p.topic_id = ? OR (p.id = ? AND topic_id IS NULL))");
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

            return postList;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    public List<Post> findPageTopics(String board, String page) throws DaoException {
        if (boardDao.find(board) == null) {
            throw new DaoException("Cannot find board " + board);
        }
        if (page.isEmpty()) page = "1";
        int nPage;
        try {
            nPage = Integer.parseInt(page) <= 0 ? 0 : ((Integer.parseInt(page) - 1) * 10);
        } catch (Exception e) {
            throw new DaoException("1337", e);
        }

        try {
            PreparedStatement statement = this.db.getConnection().prepareStatement("SELECT * FROM Post p WHERE p.board_abbreviation = ? AND topic_id IS NULL LIMIT 10 OFFSET " + nPage);
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

            return postList;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public Post post(Post p) throws DaoException, ModelValidationException {
        validateOnInsert(p);

        try {
            PreparedStatement statement = this.db.getConnection().prepareStatement("INSERT INTO Post (board_abbreviation, topic_id, ip, post_time, subject, message) VALUES (?, ?, ?, ?, ?, ?)");

            if (boardDao.find(p.getBoard_abbreviation()) == null) {
                throw new DaoException("Cannot find board " + p.getBoard_abbreviation());
            }

            statement.setString(1, p.getBoard_abbreviation());
            if (p.getTopic_id() == null)
                statement.setNull(2, Types.INTEGER);
            else
                statement.setInt(2, p.getTopic_id());
            statement.setString(3, p.getIp());
            statement.setInt(4, p.getPost_time());
            statement.setString(5, p.getSubject());
            statement.setString(6, p.getMessage());

            statement.executeUpdate();
            return p;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public void delete(String params) throws DaoException {
        try {
            PreparedStatement statement = this.db.getConnection().prepareStatement("DELETE FROM Post WHERE id = ?");
            statement.setInt(1, Integer.parseInt(params));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void validateOnInsert(Post objectToInsert) throws ModelValidationException {
        if (objectToInsert.getPost_time() == null)
            throw new ModelValidationException(objectToInsert, "The post time of a post cannot be null!");
        
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
