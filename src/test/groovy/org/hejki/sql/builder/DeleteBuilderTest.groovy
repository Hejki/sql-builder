package org.hejki.sql.builder

import spock.lang.Specification

import static org.hejki.sql.builder.Condition.eq

class DeleteBuilderTest extends Specification {

    def "delete"() {
        when:
        def result = SQL.deleteFrom("table")
                .where(eq("col", "val"))
                .setParameterConverter("col", { String o -> o + "2"})
                .setParameterPlaceholder("col", "?::text")
                .build()
                .toSql(null)

        then:
        "DELETE FROM table WHERE col = ?::text" == result.sql
        ["val2"] == result.parameters
    }
}
