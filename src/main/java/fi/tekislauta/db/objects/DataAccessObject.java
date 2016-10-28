package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;

import java.sql.SQLException;
import java.util.List;

public interface DataAccessObject<TModel, TKey> {
    TModel find(Database db, TKey key) throws Exception;
    List<TModel> findAll(Database db, String filter) throws Exception;
    TModel post(Database db, TModel model) throws Exception, ModelValidationException;
    void delete(Database db, TKey key) throws SQLException, ModelValidationException;
}
