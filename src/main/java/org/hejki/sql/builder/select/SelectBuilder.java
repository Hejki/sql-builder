package org.hejki.sql.builder.select;

import org.hejki.sql.builder.SQL;
import org.hejki.sql.builder.SQLBuilder;
import org.hejki.sql.builder.SqlWithParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger log = LoggerFactory.getLogger(SelectBuilder.class);
    private static final String LIMIT_OFFSET_SQL = " LIMIT ? OFFSET ?";
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

    SqlWithParameters toSql(Object filter, Pageable pageable) {
        if (null == pageable) {
            return toSql(filter);
        }

        StringBuilder orderBy = null;
        SqlWithParameters baseSql = toSql(filter, pageable.getPageSize(), pageable.getOffset());

        if (null != pageable.getSort()) {
            if (baseSql.sql().contains("ORDER BY")) {
                log.warn("Cannot use sort from Pageable for define ORDER BY because sorting was specified by orderBy methods. " +
                        "Use only orderBy methods or pageable sort with orderByMap methods but not both.");
                return baseSql;
            }

            for (Sort.Order order : pageable.getSort()) {
                String property = order.getProperty();
                String column = orderByMap.get(property);

                if (null == column) {
                    throw new IllegalStateException("Cannot found order mapping for property " + property);
                }

                if (null == orderBy) {
                    orderBy = new StringBuilder(32);
                    orderBy.append(" ORDER BY ");
                } else {
                    orderBy.append(", ");
                }
                orderBy.append(column).append(" ").append(order.getDirection());
            }
        }

        if (null != orderBy) {
            String sql = baseSql.sql().replace(LIMIT_OFFSET_SQL, orderBy.toString() + LIMIT_OFFSET_SQL);
            return new SqlWithParameters(sql, baseSql.getParameterList());
        }
        return baseSql;
    }

    SqlWithParameters toSql(Object filter, int limit, int offset) {
        SqlWithParameters baseSql = toSql(filter);
        List<Object> parameters = baseSql.getParameterList();
        StringBuilder sql = new StringBuilder(baseSql.sql().length() + 32);

        sql.append(baseSql.sql());
        sql.append(LIMIT_OFFSET_SQL);
        parameters.add(limit);
        parameters.add(offset);

        return new SqlWithParameters(sql.toString(), parameters);
    }

    @Override
    protected SQL innerBuild() {
        return new SelectSQL(this);
    }
}

