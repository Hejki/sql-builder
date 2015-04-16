package org.hejki.sql.builder.select;

import org.hejki.sql.builder.SQLBuilder;

public class GroupByBuilder extends SQLBuilder<SelectBuilder> {

    GroupByBuilder(SQLBuilder parent) {
        super(parent);
    }

    public GroupByBuilder groupBy(String expression) {
        addPart(SqlPart.GROUP_BY, expression);
        return this;
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
}
