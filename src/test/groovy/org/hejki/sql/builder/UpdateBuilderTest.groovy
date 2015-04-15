package org.hejki.sql.builder
import spock.lang.Specification

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
                .toSql(obj)

        then:
        "UPDATE table SET a = ?::text, f = ?, c = ?, aP = ?, bP = ?, cP = ? WHERE wP like ?" == result.sql
        ['b2', null, 'd', 'yes', null, 'yes', 'yes'] == result.parameters
    }
}
