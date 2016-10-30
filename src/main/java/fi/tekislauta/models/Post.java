package fi.tekislauta.models;

import java.util.Objects;
import java.util.zip.CRC32;
import java.security.MessageDigest;

public class Post {

    private Integer id;
    private String board_abbrevition;
    private Integer topic_id;
    private String ip;
    private Integer post_time;
    private String subject;
    private String message;

    public Post(int id, String board_id, Integer topic_id, String ip, Integer post_time, String subject, String message) {
        this.id = id;
        this.board_abbrevition = board_id;
        this.topic_id = topic_id;
        this.ip = ip;
        this.post_time = post_time;
        this.subject = subject;
        this.message = message;
    }

    public Post() {}

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
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(ip.getBytes());
            CRC32 crc32 = new CRC32();
            crc32.update(md.digest());
            this.ip = Long.toHexString(crc32.getValue()).toUpperCase();
        } catch (Exception e) {
            this.ip = "";
        }
    }

    public Integer getPost_time() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
            Objects.equals(board_abbrevition, post.board_abbrevition) &&
            Objects.equals(topic_id, post.topic_id) &&
            Objects.equals(ip, post.ip) &&
            Objects.equals(post_time, post.post_time) &&
            Objects.equals(subject, post.subject) &&
            Objects.equals(message, post.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, board_abbrevition, topic_id, ip, post_time, subject, message);
    }
}
