package fi.tekislauta.data.dao;

/**
 * Created by Jalle on 29.10.2016.
 */
public class DaoException extends Exception {

    public DaoException(String msg) {
        super(msg);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    public DaoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
