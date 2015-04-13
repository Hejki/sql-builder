package org.hejki.sql.builder
import spock.lang.Specification

class InsertBuilderTest extends Specification {

    def "insert"() {
        when:
        def result = SQL.insertInto("table")
                .columns("a", "b, c")
                .columns("d")
                .values("?, ?", "?", "?::text")
                .toSql()

        then:
        "INSERT INTO table(a, b, c, d) VALUES(?, ?, ?, ?::text)" == result.sql
        [] == result.parameters
    }
}
