package fi.tekislauta.data.dao;

import fi.tekislauta.data.Database;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.BoardResponse;
import fi.tekislauta.models.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo on 14.10.2016.
 */
public class BoardDao extends ValidatingDao<Board> implements DataAccessObject<Board, String> {
    private final Database database;
    private final Collector<Board> collector;

    public BoardDao(Database database) {
        this.database = database;
        this.collector = rs -> {
            Board b = new Board();
            b.setName(rs.getString("name"));
            b.setAbbreviation(rs.getString("abbreviation"));
            b.setDescription(rs.getString("description"));
            return b;
        };
    }

    @Override
    public Board find(String abbreviation) throws DaoException {
        try {
            PreparedStatement statement = this.database.getConnection().prepareStatement("SELECT * FROM Board WHERE abbreviation= ?");
            statement.setString(1, abbreviation);

            ResultSet rs = statement.executeQuery();

            if (!rs.next())
                return null;

            return this.collector.collect(rs);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<BoardResponse> getFrontpageBoardInfo() throws DaoException {
        try {
            PreparedStatement ps = this.database.getConnection().prepareStatement("select * from Board left join (select * from Post where topic_id is null order by post_time desc limit 2) as p where Board.abbreviation = p.board_abbreviation");
            ResultSet rs = ps.executeQuery();

            ArrayList<BoardResponse> res = new ArrayList<>();
            while(rs.next()) {
                BoardResponse br = new BoardResponse();
                Post p = new Post();
                p.setPost_time(rs.getInt("post_time"));
                p.setBoard_abbrevition(rs.getString("abbreviation"));
                p.setTopic_id(rs.getInt("topic_id"));
                p.setId(rs.getInt("id"));
                p.setIp(rs.getString("ip"));
                p.setMessage(rs.getString("message"));
                p.setSubject(rs.getString("subject"));
                p.hashIp();
                br.setTopic(p);
                br.setBoard(this.collector.collect(rs));
                res.add(br);
            }

            // Amazing solution...
            int s = res.size();
            for (Board b : this.findAll("")) {
                for (int i = 0; i < s; i++) {
                    BoardResponse br = res.get(i);
                    if (!br.getBoard().getAbbreviation().equals(b.getAbbreviation())) {
                        BoardResponse bres = new BoardResponse();
                        bres.setTopic(null);
                        bres.setBoard(b);
                        res.add(bres);
                    }
                }
            }

            return res;
        } catch(SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Board> findAll(String filter) throws DaoException {
        try {
            PreparedStatement statement = this.database.getConnection().prepareStatement("SELECT * FROM Board");

            ResultSet rs = statement.executeQuery();
            ArrayList<Board> res = new ArrayList<>();

            while (rs.next()) {
                Board b = this.collector.collect(rs);
                res.add(b);
            }
            
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Board post(Board b) throws DaoException, ValidationException {
        validateOnInsert(b);

        try {
            PreparedStatement statement = this.database.getConnection().prepareStatement("INSERT INTO Board (name, abbreviation, description) VALUES (?,?,?)");
            statement.setString(1, b.getName());
            statement.setString(2, b.getAbbreviation());
            statement.setString(3, b.getDescription());
            statement.executeUpdate();
            return b;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(String filter) throws DaoException {
        Connection con = this.database.getConnection();
        PreparedStatement deleteBoard = null;
        PreparedStatement deletePosts = null;

        try {
            con.setAutoCommit(false);

            deleteBoard = con.prepareStatement("DELETE FROM Board WHERE abbreviation = ?");
            deletePosts = con.prepareStatement("DELETE FROM Post WHERE board_abbreviation = ?");

            deleteBoard.setString(1, filter);
            deleteBoard.executeUpdate();

            deletePosts.setString(1, filter);
            deletePosts.executeUpdate();

            con.commit();
        } catch (SQLException ex) {
            try {
                if (con != null)
                    con.rollback();
            } catch (SQLException e) {
                // RIP
            } finally {
                throw new DaoException(ex);
            }
        } finally {
            try {
                if (deleteBoard != null)
                    deleteBoard.close();
                if (deletePosts != null)
                    deletePosts.close();
                con.setAutoCommit(true);
            } catch (SQLException e) {
                // RIP
            }
        }
    }

    @Override
    protected void validateOnInsert(Board board) throws ValidationException {
        if (board == null)
            throw new ValidationException("Board cannot be null!");

        if (board.getAbbreviation().contains("/"))
            throw new ValidationException("Board abbreviation can not contain the char /");

        String abbr = board.getAbbreviation();
        if (abbr == null || abbr.trim().equals(""))
            throw new ValidationException("Abbreviation of a new board cannot be null or empty");

        String name = board.getName();
        if (name == null || name.trim().equals(""))
            throw new ValidationException("Name of a new board cannot be null or empty");

        // description can be null or empty
    }
}
