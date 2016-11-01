package fi.tekislauta.data.dao;

import fi.tekislauta.data.Database;
import fi.tekislauta.models.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;
import java.util.Map.Entry;

public class PostDao extends ValidatingDao<Post> implements DataAccessObject<Post, String> {
    private final Database db;
    private final BoardDao boardDao;
    private final Collector<Post> collector;

    public PostDao(Database db) {
        this.db = db;
        this.boardDao = new BoardDao(db);
        this.collector = rs -> {
            Post p = new Post();
            p.setId(rs.getInt("id"));
            p.setTopic_id((Integer) rs.getObject("topic_id"));
            p.setIp(rs.getString("ip"));
            p.setPost_time(rs.getInt("post_time"));
            p.setSubject(rs.getString("subject"));
            p.setMessage(rs.getString("message"));
            p.hashIp();
            return p;
        };
    }

    @Override
    public Post find(String filter) throws DaoException {
        try {
            PreparedStatement statement = this.db.getConnection().prepareStatement("SELECT * FROM Post p WHERE  p.id= ?");
            statement.setString(1, filter);

            ResultSet rs = statement.executeQuery();
            if (!rs.next())
                return null;

            return this.collector.collect(rs);
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
            PreparedStatement statement = this.db.getConnection().prepareStatement("SELECT * FROM Post WHERE board_abbreviation = ? AND topic_id IS NULL");
            statement.setString(1, board);
            ResultSet rs = statement.executeQuery();

            ArrayList<Post> postList = new ArrayList<>();

            while (rs.next()) {
                Post p = this.collector.collect(rs);
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
                Post p = this.collector.collect(rs);
                postList.add(p);
            }

            return postList;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    public Map<String, Object> findAllPaginated(String board, int offset) throws DaoException, ValidationException {
        if (offset < 0) {
            throw new ValidationException("Negative offsets are not supported!");
        }

        if (boardDao.find(board) == null) {
            throw new ValidationException("Cannot find board " + board);
        }

        try {
            PreparedStatement postCountStatement = this.db.getConnection().prepareStatement("SELECT COUNT(*) FROM Post WHERE board_abbreviation = ? AND topic_id IS NULL");
            postCountStatement.setString(1, board);

            PreparedStatement postStatement = this.db.getConnection().prepareStatement("SELECT * FROM Post p WHERE p.board_abbreviation = ? AND topic_id IS NULL LIMIT 10 OFFSET ?");
            postStatement.setString(1, board);
            postStatement.setInt(2, offset);

            ResultSet rs = postStatement.executeQuery();
            ResultSet countRs = postCountStatement.executeQuery();

            ArrayList<Post> postList = new ArrayList<>();

            while (rs.next()) {
                Post p = this.collector.collect(rs);
                postList.add(p);
            }

            Map<String, Object> resMap = new HashMap<>();
            resMap.put("total_count", countRs.getInt(1));
            resMap.put("posts", postList);

            return resMap;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public Post post(Post p) throws DaoException, ValidationException {
        validateOnInsert(p);

        try {
            PreparedStatement statement = this.db.getConnection().prepareStatement(
                "INSERT INTO Post (board_abbreviation, topic_id, ip, post_time, subject, message) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );

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

            ResultSet rs = statement.getGeneratedKeys();
            rs.next(); // Statement.RETURN_GENERATED_KEYS returns the created id
            p.setId(rs.getInt(1));
            p.hashIp();
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
    protected void validateOnInsert(Post post) throws ValidationException {
        if (post == null)
            throw new ValidationException("Post cannot be null!");

        if (post.getPost_time() == null)
            throw new ValidationException("The post time of a post cannot be null!");
        
        Map<String, String> propsNotNullOrEmpty = new HashMap<>();
        propsNotNullOrEmpty.put("board abbreviation", post.getBoard_abbreviation());
        propsNotNullOrEmpty.put("ip", post.getIp());
        propsNotNullOrEmpty.put("message", post.getMessage());

        // (board_abbreviation, topic_id, ip, post_time, subject, message)
        // topic_id CAN be null
        // post_time CAN be null?
        // subject CAN be null
        for (Entry<String, String> pair : propsNotNullOrEmpty.entrySet()) {
            if (pair.getValue() == null || pair.getValue().trim().isEmpty()) {
                throw new ValidationException(
                    "The " + pair.getKey() + " of a post cannot be null or empty!"
                );
            }
        }

        try {
            if (boardDao.find(post.getBoard_abbreviation()) == null) {
                throw new ValidationException("Cannot find board " + post.getBoard_abbreviation());
            }
        } catch (DaoException e) {
            System.err.println("Caught an exception while validating post! ");
            e.printStackTrace();
        }

        String subject = post.getSubject();
        if (subject != null && subject.length() > 128)
            throw new ValidationException("Subject can be max 128 characters!");

        if (post.getMessage().length() > 2048)
            throw new ValidationException("Message can be max 2048 characters!");
    }
}
