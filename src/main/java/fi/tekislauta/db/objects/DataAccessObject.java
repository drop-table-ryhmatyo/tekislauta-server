package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;

import java.sql.SQLException;
import java.util.List;

public interface DataAccessObject<TModel, TKey> {
    TModel find(TKey key) throws SQLException;
    List<TModel> findAll(String filter) throws DaoException, SQLException;
    TModel post(TModel model) throws DaoException, SQLException, ModelValidationException;
    void delete(TKey key) throws SQLException, ModelValidationException;
}
