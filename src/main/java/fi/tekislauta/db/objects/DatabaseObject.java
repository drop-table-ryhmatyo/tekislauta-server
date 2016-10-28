package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;

import java.sql.SQLException;

public interface DatabaseObject {
    Object fetch(Database db, String filter) throws Exception;
    Object fetchAll(Database db, String filter) throws Exception;
    Object post(Database db, Object o) throws Exception, ModelValidationException;
    Object delete(Database db, String filter) throws SQLException, ModelValidationException;
}
