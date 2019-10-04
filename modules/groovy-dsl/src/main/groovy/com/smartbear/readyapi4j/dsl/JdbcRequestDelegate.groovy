package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.assertions.AssertionBuilder
import com.smartbear.readyapi4j.dsl.assertions.JdbcAssertionDelegate

/**
 * Delegate for the 'jdbcRequest'  closure in 'recipe'
 */
class JdbcRequestDelegate {
    String testStepName;
    String driver;
    String connectionString;
    boolean storedProcedure;
    Map<String, String> testStepProperties
    List<AssertionBuilder> assertions
    String query

    void name(String testStepName) {
        this.testStepName = testStepName;
    }

    void driver(String driver) {
        this.driver = driver;
    }

    void connectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    void query(String query) {
        this.query = query
    }

    void storedProcedure(boolean storedProcedure) {
        this.storedProcedure = storedProcedure;
    }

    void testStepProperties(Map<String, String> testStepProperties = [:]) {
        this.testStepProperties = testStepProperties
    }

    void asserting(@DelegatesTo(JdbcAssertionDelegate) Closure assertionDefinition) {
        JdbcAssertionDelegate delegate = new JdbcAssertionDelegate()
        assertionDefinition.delegate = delegate
        //Have to use DELEGATE_FIRST here, otherwise Groovy end up calling 'get' method on DslDelegate with assertion
        // name being converted into URI, e.g. "jdbcStatusOk"
        assertionDefinition.resolveStrategy = Closure.DELEGATE_FIRST
        assertionDefinition.call()
        this.assertions = delegate.assertionBuilders
    }
}
