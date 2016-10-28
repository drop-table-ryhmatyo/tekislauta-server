package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;

import java.sql.SQLException;
import java.util.List;

public interface DataAccessObject<TModel, TKey> {
    TModel find(Database db, TKey filter) throws Exception;
    List<TModel> findAll(Database db, TKey filter) throws Exception;
    TModel post(Database db, TModel o) throws Exception, ModelValidationException;
    TModel delete(Database db, TKey filter) throws SQLException, ModelValidationException;
}
