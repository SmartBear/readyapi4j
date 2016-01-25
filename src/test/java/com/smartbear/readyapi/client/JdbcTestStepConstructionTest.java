package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.jdbcrequest.JdbcConnection;
import io.swagger.client.model.Assertion;
import io.swagger.client.model.JdbcRequestTestStepStruct;
import io.swagger.client.model.SimpleContainsAssertion;
import io.swagger.client.model.TestStep;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.List;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.assertions.Assertions.contains;
import static com.smartbear.readyapi.client.teststeps.TestSteps.jdbcConnection;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests verifying construction of JDBC request test steps in recipes.
 */
public class JdbcTestStepConstructionTest {

    public static final String DRIVER = "org.mysql.Driver";
    public static final String CONNECTION_STRING = "jdbc:mysql://localhost/mydb";

    private JdbcConnection connection = jdbcConnection(DRIVER, CONNECTION_STRING);

    @Test
    public void buildsRecipeWithPlainJdbcRequest() throws Exception {
        String sql = "select * from users";
        TestRecipe recipe = newTestRecipe()
                .addStep(connection.jdbcRequest(sql))
                .buildTestRecipe();

        JdbcRequestTestStepStruct jdbcRequest = extractJdbcRequestTestStep(recipe);
        assertConnectionProperties(jdbcRequest);
        assertThat(jdbcRequest.getSqlQuery(), is(sql));
        assertThat(jdbcRequest.getStoredProcedure(), is(false));
    }

    @Test
    public void buildsRecipeWithStoredProcedureCall() throws Exception {
        String sql = "registerVisit()";
        TestRecipe recipe = newTestRecipe()
                .addStep(connection.storedProcedureCall(sql))
                .buildTestRecipe();

        JdbcRequestTestStepStruct jdbcRequest = extractJdbcRequestTestStep(recipe);
        assertConnectionProperties(jdbcRequest);
        assertThat(jdbcRequest.getSqlQuery(), is(sql));
        assertThat(jdbcRequest.getStoredProcedure(), is(true));
    }

    @Test
    public void retainsName() throws Exception {
        String stepName = "The Name";
        TestRecipe recipe = newTestRecipe()
                .addStep(connection.jdbcRequest("select * from some_table").named(stepName)
                )

                .buildTestRecipe();

        JdbcRequestTestStepStruct jdbcRequest = extractJdbcRequestTestStep(recipe);
        assertThat(jdbcRequest.getName(), is(stepName));
    }

    @Test
    public void addsProperties() throws Exception {
        String propertyName = "prop1";
        String propertyValue = "propValue";
        TestRecipe recipe = newTestRecipe()
                .addStep(connection.jdbcRequest("select * from some_table")
                        .addProperty(propertyName, propertyValue)
                )

                .buildTestRecipe();

        JdbcRequestTestStepStruct jdbcRequest = extractJdbcRequestTestStep(recipe);
        assertThat(jdbcRequest.getProperties().get(propertyName), is((Object) propertyValue));
    }

    @Test
    public void addsAssertions() throws Exception {
        String token = "Kalle";
        TestRecipe recipe = newTestRecipe()
                .addStep(connection.jdbcRequest("select * from some_table")
                        .addAssertion(contains(token))
                )

                .buildTestRecipe();

        JdbcRequestTestStepStruct jdbcRequest = extractJdbcRequestTestStep(recipe);
        List<Assertion> assertions = jdbcRequest.getAssertions();
        assertThat(assertions.size(), is(1));
        Assertion assertion = assertions.get(0);
        assertTrue("Wrong assertion class: " + assertion.getClass(), assertion instanceof SimpleContainsAssertion);
        assertThat(((SimpleContainsAssertion) assertion).getToken(), is(token));
    }

    private JdbcRequestTestStepStruct extractJdbcRequestTestStep(TestRecipe recipe) {
        TestStep testStep = recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getType(), is(TestStepTypes.JDBC_REQUEST.getName()));
        return (JdbcRequestTestStepStruct) testStep;
    }

    private void assertConnectionProperties(JdbcRequestTestStepStruct jdbcRequest) {
        assertThat(jdbcRequest.getDriver(), is(DRIVER));
        assertThat(jdbcRequest.getConnectionString(), is(CONNECTION_STRING));
    }
}
