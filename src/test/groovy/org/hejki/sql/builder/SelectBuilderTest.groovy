package org.hejki.sql.builder

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import spock.lang.Specification

import static org.hejki.sql.builder.Condition.*

class SelectBuilderTest extends Specification {

    def "select only"() {
        expect:
        "SELECT 1 FROM table" == SQL.select("1").from("table").toSql(null).sql
    }

    class Filter {
        private String str
        private Integer number
        private Object none
    }

    def "where condition"() {
        def filter = new Filter(str: "a", number: 1)

        when:
        def result = SQL.<Filter>select("*")
                .from("table")
                .where(eqProperty("col1", "str"))
                .and(neProperty("col2", "number"))
                .and(gtProperty("none", "none"))
                .or(lt("col3", 4))
                .or(ge("none", null))
                .setParameterConverter("col1", { String o -> o + "2"})
                .setParameterPlaceholder("col1", "?::text")
                .toSql(filter)

        then:
        "SELECT * FROM table WHERE col1 = ?::text AND col2 != ? OR col3 < ?" == result.sql
        ["a2", 1, 4] == result.parameters
    }

    def "paging"() {
        def page = new PageRequest(0, 10, new Sort(new Sort.Order(Sort.Direction.ASC, "aProp"), new Sort.Order(Sort.Direction.DESC, "bProp")))

        when:
        def result = SQL.select("a", "b")
                .from("table")
                .where(isNotNull("a"))
                .orderByMap("aProp", "a")
                .orderByMap("bProp", "b")
                .complete()
                .toSql(null, page)

        then:
        "SELECT a, b FROM table WHERE a is not null ORDER BY a ASC, b DESC LIMIT ? OFFSET ?" == result.sql
        [10, 0] == result.parameters
    }

    def "paging null"() {
        expect:
        "SELECT * FROM t" == SQL.select("*").from("t").complete().toSql(null, null).sql
    }

    def "paging by limit and offset"() {
        when:
        def sql = SQL.select("*").from("t").complete().toSql(null, 10, 0)

        then:
        "SELECT * FROM t LIMIT ? OFFSET ?" == sql.sql
        [10, 0] == sql.parameters
    }

    def "joins"() {
        when:
        def result = SQL.select("b as b")
                .from("table t", "tab a")
                .from("users u")
                .join("clients c", "c.id = u.client_id")
                .leftOuterJoin("groups g", "g.id = c.g")
                .rightOuterJoin("groups gg", "gg.id = c.gg")
                .fullOuterJoin("fo f", "f.d = r.f")
                .crossJoin("tab v", "v = r")
                .toSql(null)

        then:
        "SELECT b as b FROM table t, tab a, users u JOIN clients c ON c.id = u.client_id " +
                "LEFT OUTER JOIN groups g ON g.id = c.g RIGHT OUTER JOIN groups gg ON gg.id = c.gg " +
                "FULL OUTER JOIN fo f ON f.d = r.f CROSS JOIN tab v ON v = r" == result.sql
        [] == result.parameters
    }

    def "orderBy"() {
        expect:
        result == builder.toSql().sql

        where:
        result                                        | builder
        "SELECT * FROM t ORDER BY name"               | SQL.select("*").from("t").orderBy("name")
        "SELECT * FROM t ORDER BY name ASC"           | SQL.select("*").from("t").orderBy("name", true)
        "SELECT * FROM t ORDER BY name DESC"          | SQL.select("*").from("t").orderBy("name", false)
        "SELECT * FROM t ORDER BY name, surname DESC" | SQL.select("*").from("t").orderBy("name").orderBy("surname DESC")
        "SELECT * FROM t GROUP BY g ORDER BY name"    | SQL.select("*").from("t").groupBy("g").orderBy("name")
    }

    def "orderBy and paging"() {
        def page = new PageRequest(0, 10, new Sort(new Sort.Order(Sort.Direction.ASC, "aProp"), new Sort.Order(Sort.Direction.DESC, "bProp")))

        when:
        def sql = SQL.select("*")
                .from("t")
                .orderBy("name")
                .complete()
                .toSql(null, page)

        then:
        "SELECT * FROM t ORDER BY name LIMIT ? OFFSET ?" == sql.sql
        [10, 0] == sql.parameterList
    }
}
