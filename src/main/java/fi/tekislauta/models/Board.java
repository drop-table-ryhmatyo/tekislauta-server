package fi.tekislauta.models;

import java.util.Objects;

public class Board {

    private String name;
    private String abbreviation;
    private String description;

    public Board(int id, String name, String abbreviation, String description) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.description = description;
    }

    public Board() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(name, board.name) &&
            Objects.equals(abbreviation, board.abbreviation) &&
            Objects.equals(description, board.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, abbreviation, description);
    }
}
