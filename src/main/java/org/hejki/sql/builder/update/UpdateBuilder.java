package org.hejki.sql.builder.update;

import org.hejki.sql.builder.Condition;
import org.hejki.sql.builder.ValuesBuilderBase;

import java.util.List;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
public class UpdateBuilder extends ValuesBuilderBase<UpdateBuilder> {

    public UpdateBuilder(String table) {
        super(null);
        addPart(SqlPart.UPDATE, table);
        addPart(SqlPart.SET);
    }

    public WhereBuilder where(Condition condition) {
        return super.where(new WhereBuilder(this, condition));
    }

    protected void appendColumn(StringBuilder sql, String column, List<Object> parameters, Object value, StringBuilder endSql) {
        sql.append(column).append(" = ").append(getPlaceholder(column));
        parameters.add(value);
    }
}
