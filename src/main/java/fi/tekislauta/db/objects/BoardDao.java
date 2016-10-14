package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;
import fi.tekislauta.models.DatabaseObject;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.Result;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Hugo on 14.10.2016.
 */
public class BoardDao implements DatabaseObject {

    public BoardDao() {}

    @Override
    public Object fetch(Database db, String abbreviation) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Board WHERE name= ?");
        statement.setString(1, abbreviation);

        ResultSet rs = statement.executeQuery();
        Board b = new Board();
        if (!rs.next()) {
            b.setError("Cannot find board " + abbreviation + " :(");
            return null;
        }

        b.setId(rs.getInt(1));
        b.setName(rs.getString(2));
        b.setAbbreviation(abbreviation);
        b.setDescription(rs.getString(4));

        return b;
    }

    @Override
    public List<Object> fetchAll(Database db) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM Board");

        ResultSet rs = statement.executeQuery();

        ArrayList<Board> res = new ArrayList();

        while (rs.next()) {
            Board b = new Board();

            b.setId(rs.getInt("id"));
            b.setName(rs.getString("name"));
            b.setAbbreviation(rs.getString("abbreviation"));
            b.setDescription(rs.getString("description"));
            res.add(b);
        }

        return (List)res;
    }

    @Override
    public Object post(Database db, Object o) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement("INSERT INTO Board (name, abbreviation, description) VALUES (?,?,?) ");
        Board b = (Board)o;
        statement.setString(1, b.getName());
        statement.setString(2, b.getAbbreviation());
        statement.setString(3, b.getDescription());
        int rs = statement.executeUpdate();

        if (rs == Statement.EXECUTE_FAILED) {
            b.setError("Query execution failed. Not inserted.");
            return b;
        }
        return b;
    }

    @Override
    public void delete(Database db, String filter) throws SQLException {

    }

}
