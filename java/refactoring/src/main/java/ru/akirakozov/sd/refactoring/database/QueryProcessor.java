package ru.akirakozov.sd.refactoring.database;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface QueryProcessor<R> {
    R process(ResultSet rs) throws SQLException;
}
