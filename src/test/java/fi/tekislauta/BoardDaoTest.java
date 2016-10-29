package fi.tekislauta;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BoardDaoTest {
    private static final String DATABASE_FILE = "./test.db";
    private Database db;
    private PostDao dao;

    @Before
    public void setup() {
        deleteIfExists(DATABASE_FILE);
        this.db = new Database("jdbc:sqlite:" + DATABASE_FILE);
        this.dao = new PostDao(db);
    }

    @After
    public void teardown() {
        deleteIfExists(DATABASE_FILE);
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

    private static void deleteIfExists(String file) {
        File f = new File(file);
        if (f.exists())
            f.delete();
    }
}
