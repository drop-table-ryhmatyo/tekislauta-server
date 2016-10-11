package fi.tekislauta.models;

<<<<<<< HEAD
import fi.tekislauta.db.Database;

import java.security.Timestamp;

public class Post implements Resolvable{
=======
public class Post {
>>>>>>> 7df1f1abe42d855a6df21de63833cad8090d4ac1

    private int id;
    private Integer topic_id;
    private int ip;
    private int post_time;
    private String subject;
    private String message;

    public Post(int id, Integer topic_id, int ip, int post_time, String subject, String message) {
        this.id = id;
        this.topic_id = topic_id;
        this.ip = ip;
        this.post_time = post_time;
        this.subject = subject;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getIp() {
        return ip;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }

    public int getPost_time() {
        return post_time;
    }

    public void setPost_time(Integer post_time) {
        this.post_time = post_time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Object resolve(Database db, String id) {

        return null;
    }
}
