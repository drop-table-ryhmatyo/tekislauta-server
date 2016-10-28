package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;

import java.sql.SQLException;

public interface DataAccessObject {
    Object find(Database db, String filter) throws Exception;
    Object findAll(Database db, String filter) throws Exception;
    Object post(Database db, Object o) throws Exception, ModelValidationException;
    Object delete(Database db, String filter) throws SQLException, ModelValidationException;
}
