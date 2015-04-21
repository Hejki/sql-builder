package org.hejki.sql.builder.delete;

import org.hejki.sql.builder.Condition;
import org.hejki.sql.builder.WhereBuilderBase;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class WhereBuilder extends WhereBuilderBase<DeleteBuilder> {
    public WhereBuilder(DeleteBuilder parent, Condition... conditions) {
        super(parent, conditions);
    }
}
