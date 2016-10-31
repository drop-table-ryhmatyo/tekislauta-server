package fi.tekislauta.data.dao;

/**
 * Created by Joona on 28.10.2016.
 */
public class ValidationException extends Exception {
    private Object validatedObject;

    public ValidationException(String message) {
        super(message);
    }
}
