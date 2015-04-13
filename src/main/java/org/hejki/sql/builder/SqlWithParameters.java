package org.hejki.sql.builder;

import java.util.List;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class SqlWithParameters {
    private String sql;
    private List<Object> parameters;

    public SqlWithParameters(String sql, List<Object> parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    public String sql() {
        return sql;
    }

    public Object[] parameters() {
        return parameters.toArray();
    }

    public List<Object> getParameterList() {
        return parameters;
    }

    @Override
    public String toString() {
        return sql;
    }
}
