package org.hejki.sql.builder.insert;

import org.hejki.sql.builder.ValuesBuilderBase;

import java.util.List;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class InsertBuilder extends ValuesBuilderBase<InsertBuilder> {

    public InsertBuilder(String table) {
        super(null);
        addPart(SqlPart.INSERT, table);
        addPart(SqlPart.VALUES);
    }

    @Override
    protected void appendColumn(StringBuilder sql, String column, List<Object> parameters, Object value, StringBuilder endSql) {
        sql.append(column);
        parameters.add(value);

        if (endSql.length() == 0) {
            endSql.append(") VALUES (");
        }
        endSql.append(getPlaceholder(column));
    }
}
