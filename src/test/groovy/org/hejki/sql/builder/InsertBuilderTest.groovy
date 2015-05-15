package org.hejki.sql.builder
import spock.lang.Specification

import java.util.function.Function

class InsertBuilderTest extends Specification {
    enum Enu { A, B }
    static class Obj {
        Enu paramA
        def paramB
    }

    def "insert"() {
        def obj = new Obj(paramA: Enu.A)

        when:
        def result = SQL.insertInto("table")
                .setValue("a", "b")
                .setValue("f", null)
                .setValueIfNotNull("c", "d")
                .setValueIfNotNull("e", null)
                .setProperty("aP", "paramA")
                .setProperty("bP", "paramB")
                .setPropertyIfNotNull("cP", "paramA")
                .setPropertyIfNotNull("dP", "paramB")
                .setParameterPlaceholder("cP", "?::jsonb")
                .setParameterConverter("cP", new Function<Enu, String>() {
                    @Override
                    String apply(Enu o) {
                        return o.name()
                    }
                })
                .build()
                .toSql(obj)

        then:
        "INSERT INTO table(a, f, c, aP, bP, cP) VALUES (?, ?, ?, ?, ?, ?::jsonb)" == result.sql
        ['b', null, 'd', Enu.A, null, 'A'] == result.parameters
    }
}
