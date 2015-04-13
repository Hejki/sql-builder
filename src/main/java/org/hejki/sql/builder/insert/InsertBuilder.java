package org.hejki.sql.builder.insert;

import org.hejki.sql.builder.SQLBuilder;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class InsertBuilder extends SQLBuilder<InsertBuilder> {

    public InsertBuilder(String table) {
        super(null);
        addPart(SqlPart.INSERT, table);
    }

    public InsertBuilder columns(String... columns) {
        addPart(SqlPart.INSERT_COLUMNS, columns);
        return this;
    }

    public SQLBuilder values(String... values) {
        addPart(SqlPart.VALUES, values);
        addPart(SqlPart.VALUES_END);
        return this;
    }
}
