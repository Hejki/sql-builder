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
}
