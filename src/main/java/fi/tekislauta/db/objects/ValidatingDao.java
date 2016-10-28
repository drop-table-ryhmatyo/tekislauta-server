package fi.tekislauta.db.objects;

/**
 * Created by Joona on 28.10.2016.
 */
public abstract class ValidatingDao<TObject> {
    protected abstract void validateOnInsert(TObject objectToInsert) throws ModelValidationException;
}
