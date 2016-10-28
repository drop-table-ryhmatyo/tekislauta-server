package fi.tekislauta.db.objects;

import fi.tekislauta.db.Database;

import java.sql.SQLException;
import java.util.List;

public interface DataAccessObject<TModel, TKey> {
    TModel find(TKey key) throws DaoException;
    List<TModel> findAll(String filter) throws DaoException;
    TModel post(TModel model) throws DaoException, ModelValidationException;
    void delete(TKey key) throws DaoException, ModelValidationException;
}
