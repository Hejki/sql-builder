package org.hejki.sql.builder.select;

import org.hejki.sql.builder.Condition;
import org.hejki.sql.builder.WhereBuilderBase;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class WhereBuilder extends WhereBuilderBase<SelectBuilder> {
    public WhereBuilder(FromBuilder parent, Condition condition) {
        super(parent, condition);
    }

    public GroupByBuilder groupBy(String expression) {
        return new GroupByBuilder(this).groupBy(expression);
    }

    public HavingBuilder having(String condition) {
        return new HavingBuilder(this).having(condition);
    }

    public OrderByBuilder orderByMap(String property, String column) {
        return new OrderByBuilder(this).orderByMap(property, column);
    }

    public WhereBuilder and(Condition condition) {
        return (WhereBuilder) super.and(condition);
    }

    public WhereBuilder or(Condition condition) {
        return (WhereBuilder) super.or(condition);
    }
}
