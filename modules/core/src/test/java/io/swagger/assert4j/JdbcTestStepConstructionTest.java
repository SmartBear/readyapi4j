package io.swagger.assert4j;

import io.swagger.assert4j.client.model.Assertion;
import io.swagger.assert4j.client.model.JdbcRequestTestStep;
import io.swagger.assert4j.client.model.SimpleContainsAssertion;
import io.swagger.assert4j.client.model.TestStep;
import io.swagger.assert4j.teststeps.TestStepTypes;
import io.swagger.assert4j.teststeps.jdbcrequest.JdbcConnection;
import org.junit.Test;

import java.util.List;

import static io.swagger.assert4j.TestRecipeBuilder.newTestRecipe;
import static io.swagger.assert4j.assertions.Assertions.contains;
import static io.swagger.assert4j.teststeps.TestSteps.jdbcConnection;
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
        TestRecipe recipe = newTestRecipe(
                connection.jdbcRequest(sql))
                .buildTestRecipe();

        JdbcRequestTestStep jdbcRequest = extractJdbcRequestTestStep(recipe);
        assertConnectionProperties(jdbcRequest);
        assertThat(jdbcRequest.getSqlQuery(), is(sql));
        assertThat(jdbcRequest.isStoredProcedure(), is(false));
    }

    @Test
    public void buildsRecipeWithStoredProcedureCall() throws Exception {
        String sql = "registerVisit()";
        TestRecipe recipe = newTestRecipe(
                connection.storedProcedureCall(sql))
                .buildTestRecipe();

        JdbcRequestTestStep jdbcRequest = extractJdbcRequestTestStep(recipe);
        assertConnectionProperties(jdbcRequest);
        assertThat(jdbcRequest.getSqlQuery(), is(sql));
        assertThat(jdbcRequest.isStoredProcedure(), is(true));
    }

    @Test
    public void retainsName() throws Exception {
        String stepName = "The Name";
        TestRecipe recipe = newTestRecipe(
                connection.jdbcRequest("select * from some_table").named(stepName)
        )

                .buildTestRecipe();

        JdbcRequestTestStep jdbcRequest = extractJdbcRequestTestStep(recipe);
        assertThat(jdbcRequest.getName(), is(stepName));
    }

    @Test
    public void addsProperties() throws Exception {
        String propertyName = "prop1";
        String propertyValue = "propValue";
        TestRecipe recipe = newTestRecipe(
                connection.jdbcRequest("select * from some_table")
                        .addProperty(propertyName, propertyValue)
        )

                .buildTestRecipe();

        JdbcRequestTestStep jdbcRequest = extractJdbcRequestTestStep(recipe);
        assertThat(jdbcRequest.getProperties().get(propertyName), is((Object) propertyValue));
    }

    @Test
    public void addsAssertions() throws Exception {
        String token = "Kalle";
        TestRecipe recipe = newTestRecipe(
                connection.jdbcRequest("select * from some_table")
                        .addAssertion(contains(token))
        )

                .buildTestRecipe();

        JdbcRequestTestStep jdbcRequest = extractJdbcRequestTestStep(recipe);
        List<Assertion> assertions = jdbcRequest.getAssertions();
        assertThat(assertions.size(), is(1));
        Assertion assertion = assertions.get(0);
        assertTrue("Wrong assertion class: " + assertion.getClass(), assertion instanceof SimpleContainsAssertion);
        assertThat(((SimpleContainsAssertion) assertion).getToken(), is(token));
    }

    private JdbcRequestTestStep extractJdbcRequestTestStep(TestRecipe recipe) {
        TestStep testStep = recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getType(), is(TestStepTypes.JDBC_REQUEST.getName()));
        return (JdbcRequestTestStep) testStep;
    }

    private void assertConnectionProperties(JdbcRequestTestStep jdbcRequest) {
        assertThat(jdbcRequest.getDriver(), is(DRIVER));
        assertThat(jdbcRequest.getConnectionString(), is(CONNECTION_STRING));
    }
}
