package fi.tekislauta.models;

/**
 * Created by Hugo on 1.11.2016.
 */
public class BoardResponse {
    private Board board;
    private Post topic;

    public Post getTopic() {
        return topic;
    }

    public void setTopic(Post topic) {
        this.topic = topic;
    }


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board boardAbbreviation) {
        this.board = boardAbbreviation;
    }

}
