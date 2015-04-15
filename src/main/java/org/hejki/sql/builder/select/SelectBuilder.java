package org.hejki.sql.builder.select;

import org.hejki.sql.builder.SQLBuilder;
import org.hejki.sql.builder.SqlWithParameters;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SELECT columns
 * FROM tables
 * WHERE conditions
 * GROUP BY expressions
 * HAVING conditions
 * ORDER BY order
 * -- paging support
 *
 * @author Petr Hejkal
 */
public class SelectBuilder extends SQLBuilder<SelectBuilder> {
    private Map<String, String> orderByMap = new HashMap<>();

    public SelectBuilder() {
        super(null);
    }

    public SelectBuilder select(String... columns) {
        addPart(SqlPart.SELECT, columns);
        return this;
    }

    public FromBuilder from(String... tables) {
        return new FromBuilder(this).from(tables);
    }

    void orderByMap(String property, String column) {
        orderByMap.put(property, column);
    }

    public SqlWithParameters toSql(Object filter, Pageable pageable) {
        SqlWithParameters baseSql = toSql(filter);
        if (null == pageable) {
            return baseSql;
        }

        List<Object> parameters = baseSql.getParameterList();
        StringBuilder sql = new StringBuilder(baseSql.sql().length() + 32);

        sql.append(baseSql.sql());

        boolean firstOrder = true;
        if (null != pageable.getSort()) {
            for (Sort.Order order : pageable.getSort()) {
                String property = order.getProperty();
                String column = orderByMap.getOrDefault(property, property);

                if (firstOrder) {
                    firstOrder = false;
                    sql.append(" ORDER BY ");
                } else {
                    sql.append(", ");
                }
                sql.append(column).append(" ").append(order.getDirection());
            }
        }

        sql.append(" LIMIT ? OFFSET ?");
        parameters.add(pageable.getPageSize());
        parameters.add(pageable.getOffset());

        return new SqlWithParameters(sql.toString(), parameters);
    }
}

