package org.hejki.sql.builder;

import org.hejki.sql.builder.delete.DeleteBuilder;
import org.hejki.sql.builder.insert.InsertBuilder;
import org.hejki.sql.builder.select.SelectBuilder;
import org.hejki.sql.builder.update.UpdateBuilder;
import org.springframework.data.domain.Pageable;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class SQL {
    protected final SQLBuilder builder;

    protected SQL(SQLBuilder builder) {
        this.builder = builder;
    }

    public static SelectBuilder select(String... columns) {
        return new SelectBuilder().select(columns);
    }

    public static UpdateBuilder update(String table) {
        return new UpdateBuilder(table);
    }

    public static InsertBuilder insertInto(String table) {
        return new InsertBuilder(table);
    }

    public static DeleteBuilder deleteFrom(String table) {
        return new DeleteBuilder(table);
    }

    public SqlWithParameters toSql() {
        return builder.toSql(null);
    }

    public SqlWithParameters toSql(Object filter) {
        return builder.toSql(filter);
    }

    public SqlWithParameters toSql(Object filter, Pageable pageable) {
        throw new IllegalArgumentException("Cannot call toSql with pageable parameters on SQL with no pageable support.");
    }

    public SqlWithParameters toSql(Object filter, int limit, int offset) {
        throw new IllegalArgumentException("Cannot call toSql with pageable parameters on SQL with no pageable support.");
    }
}