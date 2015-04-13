package org.hejki.sql.builder.delete;

import org.hejki.sql.builder.Condition;
import org.hejki.sql.builder.SQLBuilder;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class DeleteBuilder extends SQLBuilder<DeleteBuilder> {

    public DeleteBuilder(String table) {
        super(null);
        addPart(SqlPart.DELETE, table);
    }

    public WhereBuilder where(Condition condition) {
        return super.where(new WhereBuilder(this, condition));
    }
}
