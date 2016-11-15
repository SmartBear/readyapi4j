package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import org.junit.Test;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.groovyScriptStep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class SoapUIRecipeExecutorTest {

    private SoapUIRecipeExecutor executor = new SoapUIRecipeExecutor();

    @Test
    public void runsMinimalProject() throws Exception {
        TestRecipe testRecipe = newTestRecipe(
                groovyScriptStep("println 'Hello Earth'")
        ).buildTestRecipe();
        ProjectResultReport projectResultReport = executor.postTestRecipe(testRecipe.getTestCase(), false, null);
        assertThat(projectResultReport.getExecutionID(), is(not(nullValue())));
        assertThat(projectResultReport.getStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
    }
}
