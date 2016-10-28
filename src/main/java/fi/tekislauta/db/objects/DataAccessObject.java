package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;

import java.sql.SQLException;
import java.util.List;

public interface DataAccessObject<TModel, TKey> {
    TModel find(TKey key) throws Exception;
    List<TModel> findAll(String filter) throws Exception;
    TModel post(TModel model) throws Exception, ModelValidationException;
    void delete(TKey key) throws SQLException, ModelValidationException;
}
