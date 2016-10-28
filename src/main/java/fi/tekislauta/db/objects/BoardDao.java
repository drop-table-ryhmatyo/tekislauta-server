package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;
import fi.tekislauta.models.Board;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo on 14.10.2016.
 */
public class BoardDao extends ValidatingDao<Board> implements DataAccessObject<Board, String> {
    private final Database database;

    public BoardDao(Database database) {
        this.database = database;
    }

    @Override
    public Board find(String abbreviation) throws SQLException {
        PreparedStatement statement = this.database.getConnection().prepareStatement("SELECT * FROM Board WHERE abbreviation= ?");
        statement.setString(1, abbreviation);

        ResultSet rs = statement.executeQuery();

        if (!rs.next())
            return null;

        Board b = new Board();

        b.setName(rs.getString("name"));
        b.setAbbreviation(rs.getString("abbreviation"));
        b.setDescription(rs.getString("description"));


        return b;
    }

    @Override
    public List<Board> findAll(String filter) throws SQLException {
        PreparedStatement statement = this.database.getConnection().prepareStatement("SELECT * FROM Board");

        ResultSet rs = statement.executeQuery();
        ArrayList<Board> res = new ArrayList();

        while (rs.next()) {
            Board b = new Board();
            b.setName(rs.getString("name"));
            b.setAbbreviation(rs.getString("abbreviation"));
            b.setDescription(rs.getString("description"));
            res.add(b);
        }

        return res;
    }

    @Override
    public Board post(Board b) throws SQLException, ModelValidationException {
        validateOnInsert(b);

        PreparedStatement statement = this.database.getConnection().prepareStatement(
                "INSERT INTO Board (name, abbreviation, description) VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        );
        statement.setString(1, b.getName());
        statement.setString(2, b.getAbbreviation());
        statement.setString(3, b.getDescription());
        statement.executeUpdate();
        return b;
    }

    @Override
    public void delete(String filter) throws SQLException {
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
            if (con != null)
                con.rollback();
        } finally {
            if (deleteBoard != null)
                deleteBoard.close();
            if (deletePosts != null)
                deletePosts.close();
            con.setAutoCommit(true);
        }
    }

    @Override
    protected void validateOnInsert(Board board) throws ModelValidationException {
        String abbr = board.getAbbreviation();
        if (abbr == null || abbr.trim().equals(""))
            throw new ModelValidationException(board, "Abbreviation of a new board cannot be null or empty");

        String name = board.getName();
        if (name == null || name.trim().equals(""))
            throw new ModelValidationException(board, "Name of a new board cannot be null or empty");

        // description can be null or empty
    }
}
