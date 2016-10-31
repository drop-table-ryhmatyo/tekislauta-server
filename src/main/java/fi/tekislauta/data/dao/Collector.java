package fi.tekislauta.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Collector<T> {
    T collect(ResultSet rs) throws SQLException;
}
