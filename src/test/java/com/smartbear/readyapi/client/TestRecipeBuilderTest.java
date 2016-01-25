package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.propertytransfer.PathLanguage;
import io.swagger.client.model.GroovyScriptTestStep;
import io.swagger.client.model.PropertyTransfer;
import io.swagger.client.model.PropertyTransferSource;
import io.swagger.client.model.PropertyTransferTarget;
import io.swagger.client.model.PropertyTransferTestStep;
import org.junit.Test;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static com.smartbear.readyapi.client.teststeps.TestSteps.groovyScriptStep;
import static com.smartbear.readyapi.client.teststeps.TestSteps.propertyTransfer;
import static com.smartbear.readyapi.client.teststeps.propertytransfer.PropertyTransferBuilder.from;
import static com.smartbear.readyapi.client.teststeps.propertytransfer.PropertyTransferSourceBuilder.aSource;
import static com.smartbear.readyapi.client.teststeps.propertytransfer.PropertyTransferTargetBuilder.aTarget;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class TestRecipeBuilderTest {

    public static final String URI = "http://maps.googleapis.com/maps/api/geocode/xml";

    @Test
    public void dumpsRecipe() throws Exception {
        String recipe = newTestRecipe()
                .addStep(
                        getRequest(URI)
                                .addQueryParameter("address", "1600+Amphitheatre+Parkway,+Mountain+View,+CA")
                                .addQueryParameter("sensor", "false")
                                .assertJsonContent("$.results[0].address_components[1].long_name", "Amphitheatre Parkway")
                )
                .buildTestRecipe().toString();

        assertThat(recipe.length(), not(0));
    }

    @Test
    public void buildsRecipeWithPropertyTransferTestStep() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(propertyTransfer()
                        .addTransfer(
                                from(aSource()
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
    public void buildsRecipeWithGroovyScriptTestStep() throws Exception {
        String script = "def a = 'a'";
        String name = "The name";
        TestRecipe recipe = newTestRecipe()
                .addStep(groovyScriptStep(script).named(name))
                .buildTestRecipe();

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
