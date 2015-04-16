package org.hejki.sql.builder.select;

import org.hejki.sql.builder.SQLBuilder;

public class HavingBuilder extends SQLBuilder<SelectBuilder> {

    HavingBuilder(SQLBuilder parent) {
        super(parent);
    }

    public HavingBuilder having(String condition) {
        addPart(SqlPart.HAVING, condition);
        return this;
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
