package fi.tekislauta.test;

import fi.tekislauta.data.Database;
import fi.tekislauta.data.dao.BoardDao;
import fi.tekislauta.data.dao.PostDao;
import fi.tekislauta.data.dao.ValidationException;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.Post;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class BoardDaoTest {
    // use an SQLite memory db for tests
    private static final String DATABASE_URL = "jdbc:sqlite::memory:";
    private Database db;
    private PostDao dao;

    @Before
    public void setup() {
        this.db = new Database(DATABASE_URL);
        this.dao = new PostDao(db);

        try {
            Statement stmt = db.getConnection().createStatement();
            stmt.executeUpdate("DELETE FROM Board;");
            stmt.executeUpdate("DELETE FROM Post;");
        } catch(SQLException ex) {

        }
    }

    @After
    public void teardown() {
    }

    @Test
    public void boardReturnedOnPost() throws Exception {
        String abbr = "b";
        String desc = "what a fancy description";
        String name = "Random";
        Board b = new Board();
        b.setAbbreviation(abbr);
        b.setDescription(desc);
        b.setName(name);

        BoardDao dao = new BoardDao(this.db);
        Board returned = dao.post(b);
        assertEquals(abbr, returned.getAbbreviation());
        assertEquals(desc, returned.getDescription());
        assertEquals(name, returned.getName());
    }

    @Test
    public void createdBoardCanBeFound() throws Exception {
        String abbr = "g", name = "Technology";
        Board b = new Board();
        b.setAbbreviation(abbr);
        b.setName(name);

        BoardDao dao = new BoardDao(this.db);
        dao.post(b);
        Board found = dao.find(abbr);

        assertNotNull(found);
        assertEquals(abbr, found.getAbbreviation());
        assertEquals(name, found.getName());
    }

    @Test
    public void createdBoardFoundAmongAll() throws Exception {
        String abbr = "b", name = "Random";
        Board b = new Board();
        b.setAbbreviation(abbr);
        b.setName(name);

        BoardDao dao = new BoardDao(this.db);
        dao.post(b);

        List<Board> boards = dao.findAll("");
        for (Board board : boards) {
            if (abbr.equals(board.getAbbreviation())
                && name.equals(board.getName())) {
                return;
            }
        }
        fail("Board not found!");
    }

    @Test(expected=ValidationException.class)
    public void nullBoardThrowsOnPost() throws Exception {
        BoardDao dao = new BoardDao(this.db);
        dao.post(null);
    }

    @Test(expected=ValidationException.class)
    public void emptyBoardObjectThrowsOnPost() throws Exception {
        BoardDao dao = new BoardDao(this.db);
        dao.post(new Board());
    }

    @Test
    public void deletedBoardIsDeleted() throws Exception {
        String abbr = "tv", name = "Television";
        Board b = new Board();
        b.setAbbreviation(abbr);
        b.setName(name);

        BoardDao dao = new BoardDao(this.db);
        dao.post(b);
        // can be found before?
        Board foundBefore = dao.find(abbr);
        assertEquals(b, foundBefore);
        dao.delete(abbr);
        // should not be found after
        Board foundAfter = dao.find(abbr);
        assertNull(foundAfter);
    }

    @Test
    public void boardDeletionDeletesPostsAsWell() throws Exception {
        /*String abbr = "biz", name = "Business";
        Board board = new Board(name, abbr, null);

        BoardDao boardDao = new BoardDao(this.db);
        boardDao.post(board);

        PostDao postDao = new PostDao(this.db);
        Post post = new Post();
        post.setBoard_abbrevition(abbr);
        post.setIp("123.123.123.123");
        post.setMessage("ayy");
        post.setSubject("lmao");
        post.setPost_time(123);
        Post createdPost = postDao.post(post);

        // post is there at this point?
        Post foundBefore = postDao.find(Integer.toString(createdPost.getId()));
        assertEquals("Inserted post was not found before deletion!", createdPost, foundBefore);

        boardDao.delete(abbr);
        // post should be deleted now
        Post foundAfter = postDao.find(Integer.toString(createdPost.getId()));
        assertNull(foundAfter);*/
    }
}
