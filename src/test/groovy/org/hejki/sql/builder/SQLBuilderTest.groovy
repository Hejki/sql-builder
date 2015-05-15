package org.hejki.sql.builder

import org.hejki.sql.builder.delete.DeleteBuilder
import spock.lang.Specification

import static org.hejki.sql.builder.Condition.eq

class SQLBuilderTest extends Specification {

    def "call where method after completion"() {
        DeleteBuilder builder = SQL.deleteFrom("table")

        when:
        builder.build()
        builder.where(eq("x", "d"))

        then:
        def e = thrown(IllegalStateException)
        e.message == 'SQL builder cannot be modified after call build() method.'
    }

    def "call where method after completion on child builder"() {
        WhereBuilderBase where = SQL.select("table").from("as").where(eq("col", "val"))

        when:
        where.parent().build()
        where.and(eq("x", "d"))

        then:
        def e = thrown(IllegalStateException)
        e.message == 'SQL builder cannot be modified after call build() method.'
    }

    def "call set method after completion"() {
        SQLBuilder builder = SQL.select("*").from("table").crossJoin("", "")

        when:
        builder.build()
        builder.parent().from("")

        then:
        def e = thrown(IllegalStateException)
        e.message == 'SQL builder cannot be modified after call build() method.'
    }
}
