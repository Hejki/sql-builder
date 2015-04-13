package org.hejki.sql.builder

import spock.lang.Specification

import static org.hejki.sql.builder.Condition.eq

class DelteBuilderTest extends Specification {

    def "delete"() {
        when:
        def result = SQL.deleteFrom("table")
                .where(eq("col", "val"))
                .toSql(null)

        then:
        "DELETE FROM table WHERE col = ?" == result.sql
        ["val"] == result.parameters
    }
}
