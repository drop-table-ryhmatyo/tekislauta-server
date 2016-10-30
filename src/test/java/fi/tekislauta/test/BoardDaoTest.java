package fi.tekislauta.test;

import fi.tekislauta.data.Database;
import fi.tekislauta.data.dao.BoardDao;
import fi.tekislauta.data.dao.PostDao;
import fi.tekislauta.data.dao.ModelValidationException;
import fi.tekislauta.data.dao.DaoException;
import fi.tekislauta.models.Board;
import fi.tekislauta.models.Post;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
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

    @Test(expected=ModelValidationException.class)
    public void nullBoardThrowsOnPost() throws Exception {
        BoardDao dao = new BoardDao(this.db);
        dao.post(null);
    }

    @Test(expected=ModelValidationException.class)
    public void emptyBoardObjectThrowsOnPost() throws Exception {
        BoardDao dao = new BoardDao(this.db);
        dao.post(new Board());
    }

    @Test
    public void deletedBoardIsDeleted() throws Exception {
        Board b = new Board();
        b.setAbbreviation("tv");
        b.setName("Television");
        BoardDao dao = new BoardDao(this.db);
        dao.post(b);

    }
}
