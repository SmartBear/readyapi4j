package com.smartbear.readyapi4j;

import com.smartbear.readyapi.client.model.GroovyScriptTestStep;
import com.smartbear.readyapi.client.model.PropertyTransfer;
import com.smartbear.readyapi.client.model.PropertyTransferSource;
import com.smartbear.readyapi.client.model.PropertyTransferTarget;
import com.smartbear.readyapi.client.model.PropertyTransferTestStep;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;
import com.smartbear.readyapi4j.teststeps.propertytransfer.PathLanguage;
import org.junit.Test;

import java.util.List;

import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi4j.properties.Properties.property;
import static com.smartbear.readyapi4j.teststeps.TestSteps.GET;
import static com.smartbear.readyapi4j.teststeps.TestSteps.groovyScriptStep;
import static com.smartbear.readyapi4j.teststeps.TestSteps.POST;
import static com.smartbear.readyapi4j.teststeps.TestSteps.propertyTransfer;
import static com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferBuilder.from;
import static com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferBuilder.fromPreviousResponse;
import static com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferBuilder.fromResponse;
import static com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferSourceBuilder.aSource;
import static com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferTargetBuilder.aTarget;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class TestRecipeBuilderTest {

    private static final String URI = "http://maps.googleapis.com/maps/api/geocode/xml";

    @Test
    public void dumpsRecipe() throws Exception {
        String recipe = newTestRecipe(GET(URI)
                                .addQueryParameter("address", "1600+Amphitheatre+Parkway,+Mountain+View,+CA")
                                .addQueryParameter("sensor", "false")
                                .assertJsonContent("$.results[0].address_components[1].long_name", "Amphitheatre Parkway")
                ).buildTestRecipe().toString();

        assertThat(recipe.length(), not(0));
    }

    @Test
    public void buildRecipeWithTestCaseProperty() {
        TestRecipe recipe = newTestRecipe().withProperty("test", "test").buildTestRecipe();
        assertThat(recipe.getTestCase().getProperties().size(), is(1));
        assertThat(recipe.getTestCase().getProperties().get("test"), is("test"));
    }

    @Test
    public void buildRecipeWithTestCaseProperties() {
        TestRecipe recipe = newTestRecipe().withProperties(property("test", "test")).buildTestRecipe();
        assertThat(recipe.getTestCase().getProperties().size(), is(1));
        assertThat(recipe.getTestCase().getProperties().get("test"), is("test"));
    }

    @Test
    public void buildsRecipeWithPropertyTransferTestStep() throws Exception {
        TestRecipe recipe = newTestRecipe(propertyTransfer(from(aSource()
                        .withSourceStep("sourceName")
                        .withProperty("username")
                        .withPath("sourcePath")
                        .withPathLanguage(PathLanguage.XPath)
                )
                        .to(aTarget()
                                .withTargetStep("targetName")
                                .withProperty("username")
                                .withPath("targetPath")
                                .withPathLanguage(PathLanguage.XPath)
                        )
                )
        )
                .buildTestRecipe();

        PropertyTransferTestStep testStep = (PropertyTransferTestStep) recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getType(), is(TestStepTypes.PROPERTY_TRANSFER.getName()));

        assertThat(testStep.getTransfers().size(), is(1));

        PropertyTransfer propertyTransfer = testStep.getTransfers().get(0);
        assertSource(propertyTransfer.getSource());
        assertTarget(propertyTransfer.getTarget());
    }

    @Test
    public void buildsJsonPathTransferWithImplicitSourceStep() throws Exception {
        final String jsonPath = "$.customer.address";
        TestRecipe recipe = newTestRecipe(
                GET("/get/something").named("theGet"),
                propertyTransfer(fromPreviousResponse(jsonPath)
                        .to(aTarget()
                                .withTargetStep("targetName")
                                .withProperty("username")
                                .withPath("targetPath")
                                .withPathLanguage(PathLanguage.XPath)
                        )
                )
        )
                .buildTestRecipe();

        verifyImplicitTransfer(recipe, jsonPath, "JSONPath");
    }

    private void verifyImplicitTransfer(TestRecipe recipe, String expectedPath, String pathLanguage) {
        List<TestStep> testSteps = recipe.getTestCase().getTestSteps();
        PropertyTransferTestStep testStep = (PropertyTransferTestStep) testSteps.get(1);
        assertThat(testStep.getType(), is(TestStepTypes.PROPERTY_TRANSFER.getName()));

        String firstStepName = testSteps.get(0).getName();
        assertThat(testStep.getTransfers().size(), is(1));

        PropertyTransfer propertyTransfer = testStep.getTransfers().get(0);
        PropertyTransferSource source = propertyTransfer.getSource();
        assertThat(source.getSourceName(), is(firstStepName));
        assertThat(source.getPath(), is(expectedPath));
        assertThat(source.getPathLanguage(), is(pathLanguage));
        assertThat(source.getProperty(), is("Response"));
    }

    @Test
    public void buildsXPathTransferWithImplicitSourceStep() throws Exception {
        final String xPath = "/customer/address";
        TestRecipe recipe = newTestRecipe(
                GET("/get/something").named("theGet"),
                propertyTransfer(fromPreviousResponse(xPath)
                                .to(aTarget()
                                        .withTargetStep("targetName")
                                        .withProperty("username")
                                        .withPath("targetPath")
                                        .withPathLanguage(PathLanguage.XPath)
                                )
                        )
                )
                .buildTestRecipe();

        verifyImplicitTransfer(recipe, xPath, "XPath");
    }

    @Test
    public void buildsTransferWithSpecifiedSourceStep() throws Exception {
        final String testStepName = "theGet";
        final String xPath = "/customer/address";
        TestRecipe recipe = newTestRecipe(
                GET("/get/something").named(testStepName),
                propertyTransfer(fromResponse(testStepName, xPath)
                                .to(aTarget()
                                        .withTargetStep("targetName")
                                        .withProperty("username")
                                        .withPath("targetPath")
                                        .withPathLanguage(PathLanguage.XPath)
                                )
                        )
                )
                .buildTestRecipe();

        verifyImplicitTransfer(recipe, xPath, "XPath");
    }

    @Test
    public void buildsXPathTransferWithImplicitTargetStep() throws Exception {
        final String xPath = "/customer/address";
        TestRecipe recipe = newTestRecipe(
                GET("/get/something"),
                propertyTransfer(fromPreviousResponse("/some/path").toNextRequest(xPath)),
                POST("/some/destination").named("thePost"))
                .buildTestRecipe();

        List<TestStep> testSteps = recipe.getTestCase().getTestSteps();
        verifyImplicitTargetStep(testSteps, xPath, "XPath");
    }

    private void verifyImplicitTargetStep(List<TestStep> testSteps, String xPath, String expectedPathLanguage) {
        PropertyTransferTestStep testStep = (PropertyTransferTestStep) testSteps.get(1);
        assertThat(testStep.getType(), is(TestStepTypes.PROPERTY_TRANSFER.getName()));

        String targetStepName = testSteps.get(2).getName();
        assertThat(testStep.getTransfers().size(), is(1));

        PropertyTransfer propertyTransfer = testStep.getTransfers().get(0);
        PropertyTransferTarget target = propertyTransfer.getTarget();
        assertThat(target.getTargetName(), is(targetStepName));
        assertThat(target.getPath(), is(xPath));
        assertThat(target.getPathLanguage(), is(expectedPathLanguage));
        assertThat(target.getProperty(), is("Request"));
    }

    @Test
    public void buildsJsonPathTransferWithImplicitTargetStep() throws Exception {
        final String jsonPath = "$.customer.address";
        TestRecipe recipe = newTestRecipe(
                GET("/get/something"),
                propertyTransfer(fromPreviousResponse("/some/path").toNextRequest(jsonPath)),
                POST("/some/destination").named("thePost")
        ).buildTestRecipe();

        List<TestStep> testSteps = recipe.getTestCase().getTestSteps();
        verifyImplicitTargetStep(testSteps, jsonPath, "JSONPath");
    }

    @Test
    public void buildsJsonPathTransferWithSpecifiedTargetStep() throws Exception {
        final String jsonPath = "$.customer.address";
        TestRecipe recipe = newTestRecipe(
                GET("/get/something"),
                propertyTransfer(fromPreviousResponse("/some/path").toRequestStep("thePost", jsonPath)),
                POST("/some/destination").named("thePost")
        ).buildTestRecipe();

        List<TestStep> testSteps = recipe.getTestCase().getTestSteps();
        verifyImplicitTargetStep(testSteps, jsonPath, "JSONPath");
    }

    @Test
    public void buildsRecipeWithGroovyScriptTestStep() throws Exception {
        String script = "def a = 'a'";
        String name = "The name";
        TestRecipe recipe = newTestRecipe(
                groovyScriptStep(script).named(name)
        ).buildTestRecipe();

        GroovyScriptTestStep testStep = (GroovyScriptTestStep) recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getType(), is(TestStepTypes.GROOVY_SCRIPT.getName()));
        assertThat(testStep.getScript(), is(script));
        assertThat(testStep.getName(), is(name));
    }

    private void assertSource(PropertyTransferSource source) {
        assertThat(source.getSourceName(), is("sourceName"));
        assertThat(source.getProperty(), is("username"));
        assertThat(source.getPath(), is("sourcePath"));
        assertThat(source.getPathLanguage(), is(PathLanguage.XPath.name()));
    }

    private void assertTarget(PropertyTransferTarget target) {
        assertThat(target.getTargetName(), is("targetName"));
        assertThat(target.getProperty(), is("username"));
        assertThat(target.getPath(), is("targetPath"));
        assertThat(target.getPathLanguage(), is(PathLanguage.XPath.name()));
    }
}
