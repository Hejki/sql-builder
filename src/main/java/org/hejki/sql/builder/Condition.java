package org.hejki.sql.builder;

import java.text.MessageFormat;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class Condition {
    static Condition AND = new OpCondition(" AND ");
    static Condition OR = new OpCondition(" OR ");

    private String column;
    private String expression;
    private String propertyName;
    private Object parameterValue;

    private Condition(String column, String propertyName, Object parameterValue, String expression) {
        this.column = column;
        this.expression = expression;
        this.propertyName = propertyName;
        this.parameterValue = parameterValue;
    }

    String getColumnName() {
        return column;
    }

    String getPropertyName() {
        return propertyName;
    }

    Object getParameterValue() {
        return parameterValue;
    }

    public String createExpression(SQLBuilder builder) {
        return MessageFormat.format(expression, column, builder.getPlaceholder(column));
    }

    @Override
    public String toString() {
        return MessageFormat.format(expression, column, "?");
    }

    public static Condition eq(String column, Object value) {
        return new ValueCondition(value, column, "{0} = {1}");
    }

    public static Condition ne(String column, Object value) {
        return new ValueCondition(value, column, "{0} != {1}");
    }

    public static Condition ge(String column, Object value) {
        return new ValueCondition(value, column, "{0} >= {1}");
    }

    public static Condition gt(String column, Object value) {
        return new ValueCondition(value, column, "{0} > {1}");
    }

    public static Condition le(String column, Object value) {
        return new ValueCondition(value, column, "{0} <= {1}");
    }

    public static Condition lt(String column, Object value) {
        return new ValueCondition(value, column, "{0} < {1}");
    }

    public static Condition ilike(String column, Object value) {
        return new ValueCondition(value, column, "{0} ilike {1}");
    }

    public static Condition like(String column, Object value) {
        return new ValueCondition(value, column, "{0} ilike {1}");
    }

    public static Condition eqProperty(String column, String propertyName) {
        return new PropertyCondition(propertyName, column, "{0} = {1}");
    }

    public static Condition neProperty(String column, String propertyName) {
        return new PropertyCondition(propertyName, column, "{0} != {1}");
    }

    public static Condition geProperty(String column, String propertyName) {
        return new PropertyCondition(propertyName, column, "{0} >= {1}");
    }

    public static Condition gtProperty(String column, String propertyName) {
        return new PropertyCondition(propertyName, column, "{0} > {1}");
    }

    public static Condition leProperty(String column, String propertyName) {
        return new PropertyCondition(propertyName, column, "{0} <= {1}");
    }

    public static Condition ltProperty(String column, String propertyName) {
        return new PropertyCondition(propertyName, column, "{0} < {1}");
    }

    public static Condition ilikeProperty(String column, String propertyName) {
        return new PropertyCondition(propertyName, column, "{0} ilike {1}");
    }

    public static Condition likeProperty(String column, String propertyName) {
        return new PropertyCondition(propertyName, column, "{0} like {1}");
    }

    public static Condition isNull(String column) {
        return new SimpleCondition(column, "{0} is null");
    }

    public static Condition isNotNull(String column) {
        return new SimpleCondition(column, "{0} is not null");
    }

    static class OpCondition extends Condition {
        private OpCondition(String value) {
            super("", null, null, value);
        }
    }

    static class PropertyCondition extends Condition {
        private PropertyCondition(String propertyName, String column, String expression) {
            super(column, propertyName, null, expression);
        }
    }

    static class ValueCondition extends Condition {
        private ValueCondition(Object parameterValue, String column, String expression) {
            super(column, null, parameterValue, expression);
        }
    }

    private static class SimpleCondition extends Condition {
        private SimpleCondition(String column, String expression) {
            super(column, null, null, expression);
        }
    }
}