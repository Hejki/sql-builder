package org.hejki.sql.builder;

import java.text.MessageFormat;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class Condition {
    public static final Condition AND = new OpCondition(" AND ");
    public static final Condition OR = new OpCondition(" OR ");
    public static final Condition BEGIN_GROUP = new GroupOpCondition(true);
    public static final Condition END_GROUP = new GroupOpCondition(false);

    private String column;
    private String expression;
    private String propertyName;
    private Object parameterValue;

    Condition(String column, String propertyName, Object parameterValue, String expression) {
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
        return format(builder.getPlaceholder(column));
    }

    @Override
    public String toString() {
        return format("?");
    }

    private String format(String placeholder) {
        return MessageFormat.format(expression, column, placeholder);
    }

    public static Condition oneEqOne() {
        return new ValueCondition("", "", "1 = 1");
    }

    public static Condition custom(String expression, Object value) {
        return new ValueCondition(value, "", expression);
    }

    public static Condition custom(String column, String operator, Object value) {
        return new ValueCondition(value, column, "{0} " + operator + " {1}");
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
        return new ValueCondition(value, column, "{0} like {1}");
    }

    public static Condition customProperty(String expression, String propertyName) {
        return new PropertyCondition(propertyName, "", expression);
    }

    public static Condition customProperty(String column, String operator, String propertyName) {
        return new PropertyCondition(propertyName, column, "{0} " + operator + " {1}");
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

    static class GroupOpCondition extends Condition {
        private GroupOpCondition(boolean open) {
            super("", null, null, open ? "(" : ")");
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