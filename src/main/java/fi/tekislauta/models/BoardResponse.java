package fi.tekislauta.models;

/**
 * Created by Hugo on 1.11.2016.
 */
public class BoardResponse {
    private Board board;
    private Post latestTopic;

    public Post getLatestTopic() {
        return latestTopic;
    }

    public void setLatestTopic(Post latestTopic) {
        this.latestTopic = latestTopic;
    }


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board boardAbbreviation) {
        this.board = boardAbbreviation;
    }

}
