package org.hejki.sql.builder.update;

import org.hejki.sql.builder.Condition;
import org.hejki.sql.builder.WhereBuilderBase;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class WhereBuilder extends WhereBuilderBase<UpdateBuilder> {
    public WhereBuilder(UpdateBuilder parent, Condition condition) {
        super(parent, condition);
    }
}
