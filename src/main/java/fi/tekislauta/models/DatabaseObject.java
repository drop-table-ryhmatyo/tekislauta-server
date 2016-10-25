package fi.tekislauta.models;

import fi.tekislauta.db.Database;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Objects;
import java.util.List;

public interface DatabaseObject {
    Object fetch(Database db, String filter) throws SQLException;
    Object fetchAll(Database db, String filter) throws SQLException;
    Object post(Database db, Object o) throws SQLException;
    Object delete(Database db, String filter) throws SQLException;
}
