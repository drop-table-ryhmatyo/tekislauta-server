package fi.tekislauta.models;

import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.security.MessageDigest;

public class Post {

    private Integer id;
    private String board_abbrevition;
    private Integer topic_id;
    private String ip;
    private long post_time;
    private String subject;
    private String message;

    public Post(int id, String board_id, Integer topic_id, String ip, int post_time, String subject, String message) {
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

    public long getPost_time() {
        return post_time;
    }

    public void setPost_time(long post_time) {
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
