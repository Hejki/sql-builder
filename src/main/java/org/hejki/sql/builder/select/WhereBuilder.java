package org.hejki.sql.builder.select;

import org.hejki.sql.builder.Condition;
import org.hejki.sql.builder.WhereBuilderBase;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class WhereBuilder extends WhereBuilderBase<SelectBuilder> {
    public WhereBuilder(FromBuilder parent, Condition... conditions) {
        super(parent, conditions);
    }

    public GroupByBuilder groupBy(String expression) {
        return new GroupByBuilder(this).groupBy(expression);
    }

    public HavingBuilder having(String condition) {
        return new HavingBuilder(this).having(condition);
    }

    public OrderByBuilder orderBy(String column) {
        return new OrderByBuilder(this).orderBy(column);
    }

    public OrderByBuilder orderBy(String column, boolean ascending) {
        return new OrderByBuilder(this).orderBy(column, ascending);
    }

    public OrderByBuilder orderByMap(String property, String column) {
        return new OrderByBuilder(this).orderByMap(property, column);
    }

    public WhereBuilder and(Condition condition, Condition... conditions) {
        return (WhereBuilder) super.and(condition, conditions);
    }

    public WhereBuilder or(Condition condition) {
        return (WhereBuilder) super.or(condition);
    }
}
