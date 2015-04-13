package org.hejki.sql.builder;

import org.hejki.sql.builder.delete.DeleteBuilder;
import org.hejki.sql.builder.insert.InsertBuilder;
import org.hejki.sql.builder.select.SelectBuilder;
import org.hejki.sql.builder.update.UpdateBuilder;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class SQL {
    public static SelectBuilder select(String... columns) {
        return new SelectBuilder().select(columns);
    }

    public static UpdateBuilder update() {
        return new UpdateBuilder();
    }

    public static InsertBuilder insertInto(String table) {
        return new InsertBuilder(table);
    }

    public static DeleteBuilder deleteFrom(String table) {
        return new DeleteBuilder(table);
    }

}