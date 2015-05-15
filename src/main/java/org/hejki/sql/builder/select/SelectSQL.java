package org.hejki.sql.builder.select;

import org.hejki.sql.builder.SQL;
import org.hejki.sql.builder.SqlWithParameters;
import org.springframework.data.domain.Pageable;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal
 */
final class SelectSQL extends SQL {

    SelectSQL(SelectBuilder builder) {
        super(builder);
    }

    public SqlWithParameters toSql(Object filter, Pageable pageable) {
        return ((SelectBuilder) builder).toSql(filter, pageable);
    }

    public SqlWithParameters toSql(Object filter, int limit, int offset) {
        return ((SelectBuilder) builder).toSql(filter, limit, offset);
    }
}