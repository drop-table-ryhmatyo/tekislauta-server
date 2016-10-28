package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;
import fi.tekislauta.models.Board;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Hugo on 14.10.2016.
 */
public class BoardDao implements DatabaseObject {

    public BoardDao() {
    }

    @Override
    public Object fetch(Database db, String abbreviation) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Board WHERE abbreviation= ?");
        statement.setString(1, abbreviation);

        ResultSet rs = statement.executeQuery();
        Board b = new Board();

        b.setName(rs.getString("name"));
        b.setAbbreviation(rs.getString("abbreviation"));
        b.setDescription(rs.getString("description"));


        return b;
    }

    @Override
    public Object fetchAll(Database db, String filter) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Board");

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
    public Object post(Database db, Object o) throws SQLException {

        PreparedStatement statement = db.getConnection().prepareStatement(
                "INSERT INTO Board (name, abbreviation, description) VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        );
        Board b = (Board) o;
        statement.setString(1, b.getName());
        statement.setString(2, b.getAbbreviation());
        statement.setString(3, b.getDescription());
        statement.executeUpdate();
        return b;
    }

    @Override
    public Object delete(Database db, String filter) throws SQLException {

        Connection con = db.getConnection();
        con.setAutoCommit(false);

        PreparedStatement deleteBoard;
        PreparedStatement deletePosts;

        deleteBoard = con.prepareStatement("DELETE FROM Board WHERE abbreviation = ?");
        deletePosts = con.prepareStatement("DELETE FROM Post WHERE board_abbreviation = ?");

        deleteBoard.setString(1, filter);
        deleteBoard.executeUpdate();

        deletePosts.setString(1, filter);
        deletePosts.executeUpdate();

        return null;
    }

}
