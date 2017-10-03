package io.swagger.assert4j.teststeps.jdbcrequest;

import java.util.Objects;

/**
 * Encapsulates information about a JDBC connection, to make the API for constructing JDBC request test steps
 * less error-prone and avoid duplicating parameters.
 */
public class JdbcConnection {

    private final String driver;
    private final String connectionString;

    public JdbcConnection(String driver, String connectionString) {
        Objects.requireNonNull(driver, "Driver class name required");
        Objects.requireNonNull(connectionString, "Connection string required");
        this.driver = driver;
        this.connectionString = connectionString;
    }

    public JdbcRequestTestStepBuilder jdbcRequest(String sql) {
        return new JdbcRequestTestStepBuilder(driver, connectionString, false).withSql(sql);
    }

    public JdbcRequestTestStepBuilder storedProcedureCall(String sql) {
        return new JdbcRequestTestStepBuilder(driver, connectionString, true).withSql(sql);
    }
}
