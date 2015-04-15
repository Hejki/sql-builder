package org.hejki.sql.builder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class SQLBuilder<T extends SQLBuilder> {
    private boolean immutable = false;
    private StringBuilder sql = new StringBuilder(128);
    private WhereBuilderBase where;
    private SqlPart activePart = null;
    private SQLBuilder<T> parent;
    private Map<String, Function> conversionFunctions = new HashMap<>();
    private Map<String, String> parameterPlaceholders = new HashMap<>();

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
        WHERE("{1}", ""),
        GROUP_BY(" GROUP BY ", ", "),
        HAVING(" HAVING ", ", "),
        ORDER_BY(" ORDER BY ", ", "),
        INSERT("INSERT INTO ", ""),
        VALUES("({0})", ", "),
        UPDATE("UPDATE ", ""),
        SET(" SET {0}", ", "),
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

    protected Object convertParameterValue(String column, Object value) {
        if (null != parent) {
            return parent.convertParameterValue(column, value);
        }

        if (conversionFunctions.containsKey(column)) {
            return conversionFunctions.get(column).apply(value);
        }
        return value;
    }

    protected String getPlaceholder(String column) {
        if (null != parent) {
            return parent.getPlaceholder(column);
        }

        return parameterPlaceholders.getOrDefault(column, "?");
    }

    public<O, C> T setParameterConverter(String column, Function<O, C> conversionFunction) {
        if (null != parent) {
            return parent.setParameterConverter(column, conversionFunction);
        }

        conversionFunctions.put(column, conversionFunction);
        return (T) this;
    }

    public T setParameterPlaceholder(String column, String placeholder) {
        if (null != parent) {
            return parent.setParameterPlaceholder(column, placeholder);
        }

        parameterPlaceholders.put(column, placeholder);
        return (T) this;
    }

    public SqlWithParameters toSql() {
        return toSql(null);
    }

    public SqlWithParameters toSql(Object filter) {
        if (null != parent) {
            return parent.toSql(filter);
        }

        List<Object> parameters = new ArrayList<>();
        String set = resolvePlaceholder(SqlPart.VALUES, filter, parameters);
        String where = resolvePlaceholder(SqlPart.WHERE, filter, parameters);

        String sql = MessageFormat.format(this.sql.toString(), set, where);
        return new SqlWithParameters(sql, parameters);
    }

    protected String resolvePlaceholder(SqlPart part, Object filter, List<Object> parameters) {
        if (where != null && SqlPart.WHERE == part) {
            return where.createWhereSql(filter, parameters);
        }
        return "";
    }
}

