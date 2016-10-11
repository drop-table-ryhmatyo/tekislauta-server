package fi.tekislauta.models;

import fi.tekislauta.db.Database;

import java.sql.SQLException;
import java.util.Objects;

public interface Resolvable {

    Object resolve(Database db, String id) throws SQLException;
}
