package org.hejki.sql.builder
import spock.lang.Specification

import static org.hejki.sql.builder.Condition.eq

class UpdateBuilderTest extends Specification {
    static class Obj {
        def paramA
        def paramB
    }

    def "update"() {
        def obj = new Obj(paramA: "yes")

        when:
        def result = SQL.update("table")
                .setValue("a", "b")
                .setValue("f", null)
                .setValueIfNotNull("c", "d")
                .setValueIfNotNull("e", null)
                .setProperty("aP", "paramA")
                .setProperty("bP", "paramB")
                .setPropertyIfNotNull("cP", "paramA")
                .setPropertyIfNotNull("dP", "paramB")
                .setParameterPlaceholder("a", "?::text")
                .setParameterPlaceholder("e", "nothing")
                .setValue("a", "b2")
                .where(Condition.likeProperty("wP", "paramA"))
                .and(Condition.ilikeProperty("wP", "paramB"))
                .build()
                .toSql(obj)

        then:
        "UPDATE table SET a = ?::text, f = ?, c = ?, aP = ?, bP = ?, cP = ? WHERE wP like ?" == result.sql
        ['b2', null, 'd', 'yes', null, 'yes', 'yes'] == result.parameters
    }

    def "use parent for sql build"() {
        when:
        def builder = SQL.update("table")
        builder.setValue("a", "b")
        builder.where(eq("a", "c"))

        def result = builder.build().toSql()

        then:
        "UPDATE table SET a = ? WHERE a = ?" == result.sql
        ["b", "c"] == result.parameters
    }
}
