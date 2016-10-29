package fi.tekislauta.models;

/**
 * Created by Hugo on 14.10.2016.
 */
public class Result {
    private static final String SUCCESS = "Success";
    private static final String ERROR = "Error";
    private static final String UNAUTHORIZED = "Unauthorized";

    private String status = null;
    private Object data = null;

    protected Result() {
        // No public constructor declared so nobody can create `new`
        // instances without using our static methods below. Or, by inheriting.
    }

    public static Result success(Object data) {
        return create(SUCCESS, data);
    }

    public static Result error(Object data) {
        return create(ERROR, data);
    }

    public static Result unauthorized(Object data) {
        return create(UNAUTHORIZED, data);
    }

    private static Result create(String status, Object data) {
        Result r = new Result();
        r.status = status;
        r.data = data;
        return r;
    }

    public Object getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }
}
