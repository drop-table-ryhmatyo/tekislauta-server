package fi.tekislauta.models;

/**
 * Created by Hugo on 14.10.2016.
 */
public class Result {
    private String error;

    public Result(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
