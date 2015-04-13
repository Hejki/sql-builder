package org.hejki.sql.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLBuilder<T extends SQLBuilder> {
    private boolean immutable = false;
    private StringBuilder sql = new StringBuilder(128);
    private WhereBuilderBase where;
    private SqlPart activePart = null;
    private SQLBuilder<T> parent;

    protected SQLBuilder(SQLBuilder<T> parent) {
        this.parent = parent;
    }

    protected enum SqlPart {
        SELECT("SELECT ", ", "),
        FROM(" FROM ", ", "),
        JOIN(" JOIN ", ""),
        LEFT_OUTER_JOIN(" LEFT OUTER JOIN ", ""),
        RIGHT_OUTER_JOIN(" RIGHT OUTER JOIN ", ""),
        FULL_OUTER_JOIN(" FULL OUTER JOIN ", ""),
        CROSS_JOIN(" CROSS JOIN ", ""),
        JOIN_ON(" ON ", ""),
        WHERE("{{WHERE}}", ""),
        GROUP_BY(" GROUP BY ", ", "),
        HAVING(" HAVING ", ", "),
        ORDER_BY(" ORDER BY ", ", "),
        INSERT("INSERT INTO ", ""),
        INSERT_COLUMNS("(", ", "),
        VALUES(") VALUES(", ", "),
        VALUES_END(")", ""),
        UPDATE("UPDATE ", ""),
        SET(" SET ", ""),
        DELETE("DELETE FROM ", "");

        private final String keyword;
        private final String separator;

        SqlPart(String keyword, String separator) {
            this.keyword = keyword;
            this.separator = separator;
        }
    }

    public T complete() {
        if (null != parent) {
            return parent.complete();
        }
        immutable = true;
        return (T) this;
    }

    protected void addPart(SqlPart part, String... strings) {
        if (immutable) {
            throw new IllegalStateException("SQL builder cannot be modified after call complete() method.");
        }

        if (null != parent) {
            parent.addPart(part, strings);
            return;
        }

        if (activePart != part) {
            activePart = part;
            sql.append(activePart.keyword);
        } else {
            sql.append(activePart.separator);
        }

        sql.append(String.join(activePart.separator, strings));
    }

    protected <WHERE extends WhereBuilderBase<T>> WHERE where(WHERE whereBuilder) {
        if (null != parent) {
            return parent.where(whereBuilder);
        }

        addPart(SqlPart.WHERE);
        where = whereBuilder;
        return (WHERE) where;
    }

    public SqlWithParameters toSql() {
        return toSql(null);
    }

    public SqlWithParameters toSql(Object filter) {
        if (null != parent) {
            return parent.toSql(filter);
        }

        String sql = this.sql.toString();
        List<Object> parameters = Collections.emptyList();

        if (null != where) {
            parameters = new ArrayList<>();
            String whereSql = where.createWhereSql(filter, parameters);
            sql = sql.replace(SqlPart.WHERE.keyword, whereSql);
        }

        return new SqlWithParameters(sql, parameters);
    }
}

