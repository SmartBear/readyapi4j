package io.swagger.assert4j.dsl.assertions

import io.swagger.assert4j.assertions.Assertions
import io.swagger.assert4j.assertions.JdbcStatusAssertionBuilder

import static io.swagger.assert4j.assertions.Assertions.jdbcRequestTimeout

/**
 * Delegate class to execute the commands in 'asserting' closure on JDBC test step.
 */
class JdbcAssertionDelegate extends AbstractAssertionsDelegate {
    /**
     * Not a real getter, but a way to call this method without using parentheses in the client code/DSL.
     * @return null
     */
    JdbcStatusAssertionBuilder getJdbcStatusOk() {
        assertionBuilders.add(Assertions.jdbcRequestStatusOk())
        //Returning null as it is not a real getter but a hack to be able to call this method without parentheses in client code
        return null
    }

    /**
     * Creates delegate to add timeout assertion on JDBC request
     * @param timeout value to assert that JDBC request was executed within this time, assertion fails if request takes
     *                  more time than 'timeout'
     * @return TimeBasedAssertionDelegate
     */
    TimeBasedAssertionDelegate timeout(int timeout) {
        return new TimeBasedAssertionDelegate({ BigDecimal time -> jdbcRequestTimeout(time.longValue()) },
                new BigDecimal(timeout), assertionBuilders)
    }
}
