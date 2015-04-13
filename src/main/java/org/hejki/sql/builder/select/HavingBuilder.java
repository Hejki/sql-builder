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

    public OrderByBuilder orderByMap(String property, String column) {
        return new OrderByBuilder(this).orderByMap(property, column);
    }
}
