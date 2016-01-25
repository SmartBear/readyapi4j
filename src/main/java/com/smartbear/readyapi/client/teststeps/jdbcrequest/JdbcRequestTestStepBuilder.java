package com.smartbear.readyapi.client.teststeps.jdbcrequest;

import com.smartbear.readyapi.client.assertions.AbstractAssertionBuilder;
import com.smartbear.readyapi.client.assertions.AssertionBuilder;
import com.smartbear.readyapi.client.teststeps.TestStepBuilder;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import io.swagger.client.model.Assertion;
import io.swagger.client.model.JdbcRequestTestStepStruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Builder for JdbcRequestTestStepStruct objects.
 */
public class JdbcRequestTestStepBuilder implements TestStepBuilder<JdbcRequestTestStepStruct> {

    private final String driver;
    private final String connectionString;
    private final boolean storedProcedure;
    private String sqlQuery;
    private List<AssertionBuilder> assertionBuilders = new ArrayList<>();
    private Map<String, Object> properties = new HashMap<>();
    private String name;

    public JdbcRequestTestStepBuilder(String driver, String connectionString, boolean storedProcedure) {
        this.driver = driver;
        this.connectionString = connectionString;
        this.storedProcedure = storedProcedure;
    }

    public JdbcRequestTestStepBuilder withSql(String sql) {
        this.sqlQuery = sql;
        return this;
    }

    public JdbcRequestTestStepBuilder named(String name) {
        this.name = name;
        return this;
    }

    public JdbcRequestTestStepBuilder withProperties(Map<String, Object> newProperties) {
        Objects.requireNonNull(newProperties, "Properties can't be null");
        this.properties = newProperties;
        return this;
    }

    public JdbcRequestTestStepBuilder addProperty(String propertyName, Object value) {
        properties.put(propertyName, value);
        return this;
    }

    public JdbcRequestTestStepBuilder addAssertion(AssertionBuilder assertion) {
        assertionBuilders.add(assertion);
        return this;
    }

    @Override
    public JdbcRequestTestStepStruct build() {
        JdbcRequestTestStepStruct testStep = new JdbcRequestTestStepStruct();
        testStep.setType(TestStepTypes.JDBC_REQUEST.getName());
        testStep.setDriver(driver);
        testStep.setConnectionString(connectionString);
        testStep.setStoredProcedure(storedProcedure);
        testStep.setSqlQuery(sqlQuery);
        testStep.setProperties(properties);
        testStep.setName(name);
        setAssertions(testStep);
        return testStep;
    }

    private void setAssertions(JdbcRequestTestStepStruct testStep) {
        List<Assertion> assertions = new ArrayList<>();
        for (AssertionBuilder assertionBuilder : assertionBuilders) {
            assertions.add(((AbstractAssertionBuilder) assertionBuilder).build());
        }
        testStep.setAssertions(assertions);
    }
}
