package fi.tekislauta.test;

import fi.tekislauta.data.Database;
import fi.tekislauta.data.dao.PostDao;
import fi.tekislauta.data.dao.ModelValidationException;
import fi.tekislauta.data.dao.DaoException;
import fi.tekislauta.models.Post;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PostDaoTest {
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
}
