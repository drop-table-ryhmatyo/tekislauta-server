package fi.tekislauta.models;

/**
 * Created by Hugo on 14.10.2016.
 */
public class Result {
    private String status = "Success";
    public Object data = null;

    public Result() {}

    public Object getData() {
        return data;
    }

    public void setData(Object o) {
        this.data = o;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
