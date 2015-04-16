package org.hejki.sql.builder.select;

import org.hejki.sql.builder.SQLBuilder;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class OrderByBuilder extends SQLBuilder<SelectBuilder> {
    OrderByBuilder(SQLBuilder<SelectBuilder> parent) {
        super(parent);
    }

    public OrderByBuilder orderByMap(String property, String column) {
        complete().orderByMap(property, column);
        return this;
    }

    public OrderByBuilder orderBy(String column) {
        addPart(SqlPart.ORDER_BY, column);
        return this;
    }

    public OrderByBuilder orderBy(String column, boolean ascending) {
        addPart(SqlPart.ORDER_BY, column + (ascending ? " ASC" : " DESC"));
        return this;
    }
}
