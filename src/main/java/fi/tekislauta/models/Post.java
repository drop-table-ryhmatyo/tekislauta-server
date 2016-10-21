package fi.tekislauta.models;

import fi.tekislauta.db.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Post extends Result {

    private Integer id;
    private String board_abbrevition;
    private Integer topic_id;
    private String ip;
    private Integer post_time;
    private String subject;
    private String message;

    public Post(int id, String board_id, Integer topic_id, String ip, int post_time, String subject, String message) {
        super(null);
        this.id = id;
        this.board_abbrevition = board_id;
        this.topic_id = topic_id;
        this.ip = ip;
        this.post_time = post_time;
        this.subject = subject;
        this.message = message;
    }

    // Nullify all members for a clean JSON object
    public void setError(String e) {
        this.id = null;
        this.board_abbrevition = null;
        this.message = null;
        this.ip = null;
        this.subject = null;
        this.topic_id = null;
        this.post_time = null;
        super.setError(e);
    }

    public Post() {
        super(null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getTopic_id() {
        return topic_id;
    }

    public String getBoard_abbreviation() {
        return board_abbrevition;
    }

    public void setBoard_abbrevition(String board_id) {
        this.board_abbrevition = board_id;
    }

    public void setTopic_id(Integer topic_id) {
        this.topic_id = topic_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
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
}
