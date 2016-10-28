package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;

import java.sql.SQLException;

public interface DatabaseObject {
    Object fetch(Database db, String filter) throws SQLException;
    Object fetchAll(Database db, String filter) throws SQLException;
    Object post(Database db, Object o) throws SQLException, ModelValidationException;
    Object delete(Database db, String filter) throws SQLException, ModelValidationException;
}
