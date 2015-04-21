package org.hejki.sql.builder;

import org.hejki.sql.builder.util.PropertyUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static org.hejki.sql.builder.Condition.*;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class WhereBuilderBase<T extends SQLBuilder> extends SQLBuilder<T> {
    private List<Condition> conditions = new ArrayList<>();

    protected WhereBuilderBase(SQLBuilder<T> parent, Condition... conditions) {
        super(parent);

        if (conditions.length == 1) {
            and(conditions[0]);
        } else if (conditions.length > 0) {
            and(conditions[0], Arrays.copyOfRange(conditions, 1, conditions.length));
        }
    }

    public WhereBuilderBase<T> and(Condition condition, Condition... additionalConditions) {
        return add(AND, condition, additionalConditions);
    }

    public WhereBuilderBase<T> or(Condition condition, Condition... additionalConditions) {
        return add(OR, condition, additionalConditions);
    }

    private WhereBuilderBase<T> add(Condition operator, Condition condition, Condition... additionalConditions) {
        conditions.add(operator);

        if (additionalConditions.length > 0) {
            conditions.add(BEGIN_GROUP);
            conditions.add(condition);

            Condition lastAddCondition = null;
            for (Condition c : additionalConditions) {
                if (false == lastAddCondition instanceof OpCondition) {
                    conditions.add(operator);
                }
                conditions.add(c);
                lastAddCondition = c;
            }
            conditions.add(END_GROUP);
        } else {
            conditions.add(condition);
        }

        return this;
    }

    String createWhereSql(Object parametersObject, List<Object> parameters) {
        StringBuilder sql = new StringBuilder(conditions.size() * 8);
        Condition operator = null;
        Stack<StringBuilder> group = new Stack<>();
        Stack<Condition> groupOperator = new Stack<>();

        for (Condition condition : conditions) {
            if (condition instanceof GroupOpCondition) {
                if (condition == BEGIN_GROUP) {
                    group.push(sql);
                    groupOperator.push(operator);
                    sql = new StringBuilder();
                    continue;
                }

                StringBuilder groupSql = sql;
                sql = group.pop();
                operator = groupOperator.pop();

                if (groupSql.length() > 0) {
                    if (null != operator && sql.length() > 0) {
                        sql.append(operator);
                    }
                    sql.append("(").append(groupSql.toString()).append(")");
                }
                continue;
            }

            if (condition instanceof OpCondition) {
                operator = condition;
                continue;
            }

            if (condition instanceof PropertyCondition) {
                final Condition finalOperator = operator;
                final StringBuilder finalSql = sql;

                PropertyUtils.property(condition.getPropertyName(), parametersObject, value -> {
                    if (value != null) {
                        appendCondition(finalSql, finalOperator, condition);
                        parameters.add(convertParameterValue(condition.getColumnName(), value));
                    }
                });
                continue;
            }

            if (condition instanceof ValueCondition) {
                Object value = condition.getParameterValue();
                if (value != null) {
                    appendCondition(sql, operator, condition);
                    parameters.add(convertParameterValue(condition.getColumnName(), value));
                }
                continue;
            }

//            if (condition instanceof WhereGroupBuilder.ConditionGroup) {
//                ((WhereGroupBuilder.ConditionGroup) condition).appendWhereSql(sql, parametersObject, parameters);
//                continue;
//            }

            appendCondition(sql, operator, condition);
        }

        if (sql.length() > 0) {
            return " WHERE " + sql.toString();
        }
        return "";
    }

    private void appendCondition(StringBuilder sql, Condition operator, Condition condition) {
        if (null != operator && sql.length() > 0) {
            sql.append(operator);
        }
        sql.append(condition.createExpression(this));
    }
}
