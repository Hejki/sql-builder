package org.hejki.sql.builder;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class Condition {
    private static final String P = "?";
    static Condition AND = new OpCondition(" AND ");
    static Condition OR = new OpCondition(" OR ");

    private String value;
    private String paramName;
    private Object paramValue;

    private Condition(String paramName, Object paramValue, String value) {
        this.value = value;
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    String getParamName() {
        return paramName;
    }

    Object getParamValue() {
        return paramValue;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Condition eq(String column, Object value) {
        return new ValueCondition(value, column, "=", P);
    }

    public static Condition ne(String column, Object value) {
        return new ValueCondition(value, column, "!=", P);
    }

    public static Condition ge(String column, Object value) {
        return new ValueCondition(value, column, ">=", P);
    }

    public static Condition gt(String column, Object value) {
        return new ValueCondition(value, column, ">", P);
    }

    public static Condition le(String column, Object value) {
        return new ValueCondition(value, column, "<=", P);
    }

    public static Condition lt(String column, Object value) {
        return new ValueCondition(value, column, "<", P);
    }

    public static Condition ilike(String column, Object value) {
        return new ValueCondition(value, "lower(", column, ") like lower(", P, ")");
    }

    public static Condition like(String column, Object value) {
        return new ValueCondition(value, column, "like", P);
    }

    public static Condition eqParam(String column, String paramName) {
        return new ParamCondition(paramName, column, "=", P);
    }

    public static Condition neParam(String column, String paramName) {
        return new ParamCondition(paramName, column, "!=", P);
    }

    public static Condition geParam(String column, String paramName) {
        return new ParamCondition(paramName, column, ">=", P);
    }

    public static Condition gtParam(String column, String paramName) {
        return new ParamCondition(paramName, column, ">", P);
    }

    public static Condition leParam(String column, String paramName) {
        return new ParamCondition(paramName, column, "<=", P);
    }

    public static Condition ltParam(String column, String paramName) {
        return new ParamCondition(paramName, column, "<", P);
    }

    public static Condition ilikeParam(String column, String paramName) {
        return new ParamCondition(paramName, "lower(", column, ") like lower(", P, ")");
    }

    public static Condition likeParam(String column, String paramName) {
        return new ParamCondition(paramName, column, "like", P);
    }

    public static Condition isNull(String column) {
        return new SimpleCondition(column, "is null");
    }

    public static Condition isNotNull(String column) {
        return new SimpleCondition(column, "is not null");
    }

    static class OpCondition extends Condition {
        private OpCondition(String value) {
            super(null, null, value);
        }
    }

    static class ParamCondition extends Condition {
        private ParamCondition(String paramName, String... value) {
            super(paramName, null, String.join(" ", value));
        }
    }

    static class ValueCondition extends Condition {
        private ValueCondition(Object paramValue, String... value) {
            super(null, paramValue, String.join(" ", value));
        }
    }

    private static class SimpleCondition extends Condition {
        private SimpleCondition(String... value) {
            super(null, null, String.join(" ", value));
        }
    }
}