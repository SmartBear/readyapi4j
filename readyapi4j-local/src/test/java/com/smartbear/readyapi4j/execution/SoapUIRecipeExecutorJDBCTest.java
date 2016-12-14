package com.smartbear.readyapi4j.execution;

import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi4j.teststeps.jdbcrequest.JdbcConnection;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi4j.assertions.Assertions.contains;
import static com.smartbear.readyapi4j.teststeps.TestSteps.jdbcConnection;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class SoapUIRecipeExecutorJDBCTest {
    private static Logger logger = LoggerFactory.getLogger(SoapUIRecipeExecutorJDBCTest.class);

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String CREATE_STATEMENT = "CREATE TABLE USERS(id int primary key, firstname varchar(255), lastname varchar(255))";
    private static final String INSERT_STATEMENT = "INSERT INTO USERS VALUES (?,?,?)";
    private static final String SELECT_STATEMENT = "SELECT * FROM USERS";

    private JdbcConnection connection = jdbcConnection(DB_DRIVER, DB_CONNECTION);

    private SoapUIRecipeExecutor executor = new SoapUIRecipeExecutor();

    @BeforeClass
    public static void setUp() {
        try (Connection dbConnection = getDBConnection()) {
            PreparedStatement createStatement = dbConnection.prepareStatement(CREATE_STATEMENT);
            createStatement.executeUpdate();
            createStatement.close();

            PreparedStatement insertStatement1 = dbConnection.prepareStatement(INSERT_STATEMENT);
            insertStatement1.setInt(1, 0);
            insertStatement1.setString(2, "Johan");
            insertStatement1.setString(3, "Johansson");
            insertStatement1.executeUpdate();
            insertStatement1.close();

            PreparedStatement insertStatement2 = dbConnection.prepareStatement(INSERT_STATEMENT);
            insertStatement2.setInt(1, 1);
            insertStatement2.setString(2, "Arvid");
            insertStatement2.setString(3, "Arvidsson");
            insertStatement2.executeUpdate();
            insertStatement2.close();
        } catch (SQLException e) {
            logger.error("Could not create test tables in in-memory db due to: " + e.getMessage());
        }
    }

    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
            dbConnection = DriverManager.getConnection(DB_CONNECTION);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Could not get in-memory db connection due to: " + e.getMessage());
        }
        return dbConnection;
    }

    @Test
    public void runsJDBCTestStepSelect() {
        TestRecipe testRecipe = newTestRecipe(
                connection.jdbcRequest(SELECT_STATEMENT).addAssertion(contains("Arvid"))
        ).buildTestRecipe();
        Execution execution = executor.postTestCase(testRecipe.getTestCase(), false);
        assertThat(execution.getId(), is(not(nullValue())));
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
    }
}