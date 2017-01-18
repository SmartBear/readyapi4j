package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.assertions.AssertionBuilder
import com.smartbear.readyapi4j.dsl.assertions.JdbcAssertionDelegate
import com.smartbear.readyapi4j.teststeps.TestStepBuilder
import com.smartbear.readyapi4j.teststeps.jdbcrequest.JdbcRequestTestStepBuilder

class JdbcRequestDelegate {
    private String testStepName;
    private String driver;
    private String connectionString;
    private boolean storedProcedure;
    private Map<String, String> testStepProperties
    List<AssertionBuilder> assertions

    void name(String testStepName) {
        this.testStepName = testStepName;
    }

    void driver(String driver) {
        this.driver = driver;
    }

    void connectionString(String connectionString) {
        this.connectionString = connectionString;
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

    TestStepBuilder buildJdbcRequestStep() {
        JdbcRequestTestStepBuilder builder = new JdbcRequestTestStepBuilder(driver, connectionString, storedProcedure)
        builder.named(testStepName)
        if (testStepProperties) {
            builder.withProperties(testStepProperties)
        }
        this.assertions.each { assertionBuilder -> builder.addAssertion(assertionBuilder) }
        return builder
    }
}
