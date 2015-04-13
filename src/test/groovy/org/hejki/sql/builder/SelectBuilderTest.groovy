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
                .where(eqParam("col1", "str"))
                .and(neParam("col2", "number"))
                .and(gtParam("none", "none"))
                .or(lt("col3", 4))
                .or(ge("none", null))
                .toSql(filter)

        then:
        "SELECT * FROM table WHERE col1 = ? AND col2 != ? OR col3 < ?" == result.sql
        ["a", 1, 4] == result.parameters
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
}
