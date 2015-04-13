package org.hejki.sql.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.hejki.sql.builder.Condition.AND;
import static org.hejki.sql.builder.Condition.OR;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class WhereBuilderBase<T extends SQLBuilder> extends SQLBuilder<T> {
    private List<Condition> conditions = new ArrayList<>();

    protected WhereBuilderBase(SQLBuilder<T> parent, Condition condition) {
        super(parent);
        conditions.add(condition);
    }

    public WhereBuilderBase<T> and(Condition condition) {
        conditions.add(AND);
        conditions.add(condition);
        return this;
    }

    public WhereBuilderBase<T> or(Condition condition) {
        conditions.add(OR);
        conditions.add(condition);
        return this;
    }

    String createWhereSql(Object parametersObject, List<Object> parameters) {
        StringBuilder sql = new StringBuilder(conditions.size() * 8);
        Condition operator = null;

        for (Condition condition : conditions) {
            if (condition instanceof Condition.OpCondition) {
                operator = condition;
                continue;
            }

            if (condition instanceof Condition.ParamCondition) {
                String propertyName = condition.getParamName();
                try {
                    Field field = parametersObject.getClass().getDeclaredField(propertyName);
                    field.setAccessible(true);
                    Object value = field.get(parametersObject);
                    if (value != null) {
                        appendCondition(sql, operator, condition);
                        parameters.add(value);
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Cannot get value from property " + propertyName + " on object " + parametersObject, e);
                } catch (NoSuchFieldException e) {
                    // it's ok (maybe)
                }
                continue;
            }

            if (condition instanceof Condition.ValueCondition) {
                Object value = condition.getParamValue();
                if (value != null) {
                    appendCondition(sql, operator, condition);
                    parameters.add(value);
                }
                continue;
            }

            appendCondition(sql, operator, condition);
        }

        if (sql.length() > 0) {
            return " WHERE " + sql.toString();
        }
        return "";
    }

    private void appendCondition(StringBuilder sql, Condition operator, Condition condition) {
        if (null != operator) {
            sql.append(operator);
        }
        sql.append(condition);
    }
}
