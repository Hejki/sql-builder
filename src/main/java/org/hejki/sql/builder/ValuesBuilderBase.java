package org.hejki.sql.builder;

import org.hejki.sql.builder.util.PropertyUtils;

import java.util.*;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public abstract class ValuesBuilderBase<T extends ValuesBuilderBase> extends SQLBuilder<T> {
    public static final String VAL_SEPARATOR = ", ";
    private Map<String, Object> values = new LinkedHashMap<>();

    protected ValuesBuilderBase(SQLBuilder<T> parent) {
        super(parent);
    }

    public T setValue(String column, Object value) {
        values.put(column, value);
        return (T) this;
    }

    public T setValueIfNotNull(String column, Object value) {
        values.put(column, Optional.ofNullable(value));
        return (T) this;
    }

    public T setProperty(String column, String propertyName) {
        values.put(column, new PropertyWrapper(propertyName, true));
        return (T) this;
    }

    public T setPropertyIfNotNull(String column, String propertyName) {
        values.put(column, new PropertyWrapper(propertyName, false));
        return (T) this;
    }

    @Override
    protected String resolvePlaceholder(SqlPart part, Object filter, List<Object> parameters) {
        if (part != SqlPart.VALUES) {
            return super.resolvePlaceholder(part, filter, parameters);
        }

        StringBuilder begin = new StringBuilder(64);
        StringBuilder end = new StringBuilder(64);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String column = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Optional) {
                Optional optional = (Optional) value;
                if (optional.isPresent()) {
                    appendColumn0(begin, column, parameters, optional.get(), end);
                }
            } else if (value instanceof PropertyWrapper) {
                PropertyWrapper wrapper = ((PropertyWrapper) value);
                PropertyUtils.property(wrapper.name, filter, val -> {
                    if (val != null || wrapper.nullable) {
                        appendColumn0(begin, column, parameters, val, end);
                    }
                });
            } else {
                appendColumn0(begin, column, parameters, value, end);
            }
        }

        if (begin.length() > 0) {
            begin.setLength(begin.length() - VAL_SEPARATOR.length()); // remove last ", "
        }
        if (end.length() > 0) {
            end.setLength(end.length() - VAL_SEPARATOR.length()); // remove last ", "
        }

        return begin.append(end).toString();
    }

    private void appendColumn0(StringBuilder sql, String column, List<Object> parameters, Object value, StringBuilder endSql) {
        value = convertParameterValue(column, value);
        appendColumn(sql, column, parameters, value, endSql);
        sql.append(VAL_SEPARATOR);
        if (endSql.length() > 0) {
            endSql.append(VAL_SEPARATOR);
        }
    }

    protected abstract void appendColumn(StringBuilder sql, String column, List<Object> parameters, Object value, StringBuilder endSql);

    private static class PropertyWrapper {
        private String name;
        private boolean nullable;

        private PropertyWrapper(String name, boolean nullable) {
            this.name = name;
            this.nullable = nullable;
        }
    }
}
