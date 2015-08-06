package org.hejki.sql.builder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    private PreparedStatement fill(PreparedStatement stmt) throws SQLException {
        int i = 1;

        for (Object parameter : getParameterList()) {
            stmt.setObject(i, parameter);
            i++;
        }

        return stmt;
    }

    public PreparedStatement prepareStatement(Connection connection) throws SQLException {
        return fill(connection.prepareStatement(sql()));
    }

    public PreparedStatement prepareStatement(Connection connection, int resultSetType,int resultSetConcurrency) throws SQLException {
        return fill(connection.prepareStatement(sql(), resultSetType, resultSetConcurrency));
    }

    public PreparedStatement prepareStatement(Connection connection, int resultSetType,int resultSetConcurrency, int resultSetHoldability)throws SQLException {
        return fill(connection.prepareStatement(sql(), resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    public PreparedStatement prepareStatement(Connection connection, int columnIndexes[]) throws SQLException {
        return fill(connection.prepareStatement(sql(), columnIndexes));
    }

    public PreparedStatement prepareStatement(Connection connection, String... columnNames)throws SQLException {
        return fill(connection.prepareStatement(sql(), columnNames));
    }
}
