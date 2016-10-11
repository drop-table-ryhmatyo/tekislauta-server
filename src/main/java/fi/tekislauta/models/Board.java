package fi.tekislauta.models;

import java.util.*;

public class Board {

    private int id;
    private String name;
    private String abbreviation;
    private String description;
    private List<Post> posts;

    public Board(int id, String name, String abbreviation, String description) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.description = description;
        this.posts = new ArrayList<>();

    }

    public void addPost(Post post) {

        this.posts.add(post);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
