package fi.tekislauta.db.objects;

/**
 * Created by Joona on 28.10.2016.
 */
public class ModelValidationException extends Exception {
    private Object validatedObject;

    public ModelValidationException(Object validatedObject, String message) {
        super(message);
        this.validatedObject = validatedObject;
    }

    public Object getValidatedObject() {
        return validatedObject;
    }
}
