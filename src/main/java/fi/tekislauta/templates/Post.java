package fi.tekislauta.templates;

import java.security.Timestamp;

public class Post {

    private int id;
    private int board_id;
    private int topic_id;
    private int ip;
    private Timestamp post_time;
    private String subject;
    private String message;

    public Post(int id, int board_id, int topic_id, int ip, Timestamp post_time, String subject, String message) {
        this.id = id;
        this.board_id = board_id;
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

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
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

    public Timestamp getPost_time() {
        return post_time;
    }

    public void setPost_time(Timestamp post_time) {
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
