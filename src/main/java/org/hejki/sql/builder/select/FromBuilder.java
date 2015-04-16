package org.hejki.sql.builder.select;

import org.hejki.sql.builder.Condition;
import org.hejki.sql.builder.SQLBuilder;

public class FromBuilder extends SQLBuilder<SelectBuilder> {
    FromBuilder(SelectBuilder select) {
        super(select);
    }

    public FromBuilder from(String... tables) {
        addPart(SqlPart.FROM, tables);
        return this;
    }

    public WhereBuilder where(Condition condition) {
        return super.where(new WhereBuilder(this, condition));
    }

    public FromBuilder join(String table, String on) {
        addPart(SqlPart.JOIN, table);
        addPart(SqlPart.JOIN_ON, on);
        return this;
    }

    public FromBuilder leftOuterJoin(String table, String on) {
        addPart(SqlPart.LEFT_OUTER_JOIN, table);
        addPart(SqlPart.JOIN_ON, on);
        return this;
    }

    public FromBuilder rightOuterJoin(String table, String on) {
        addPart(SqlPart.RIGHT_OUTER_JOIN, table);
        addPart(SqlPart.JOIN_ON, on);
        return this;
    }

    public FromBuilder fullOuterJoin(String table, String on) {
        addPart(SqlPart.FULL_OUTER_JOIN, table);
        addPart(SqlPart.JOIN_ON, on);
        return this;
    }

    public FromBuilder crossJoin(String table, String on) {
        addPart(SqlPart.CROSS_JOIN, table);
        addPart(SqlPart.JOIN_ON, on);
        return this;
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
}
