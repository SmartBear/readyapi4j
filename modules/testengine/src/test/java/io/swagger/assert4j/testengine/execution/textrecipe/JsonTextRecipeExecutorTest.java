package io.swagger.assert4j.testengine.execution.textrecipe;

import io.swagger.assert4j.HttpBasicAuth;
import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.TestRecipeBuilder;
import io.swagger.assert4j.client.model.TestJobReport;
import io.swagger.assert4j.execution.Execution;
import io.swagger.assert4j.testengine.execution.ExecutionTestHelper;
import io.swagger.assert4j.testengine.execution.ProjectExecutionTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class JsonTextRecipeExecutorTest extends ProjectExecutionTestBase {

    private final File file;

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> data() throws IOException {
        File recipeDir = new File(JsonTextRecipeExecutorTest.class.getResource("/recipes").getFile());
        File[] listFiles = recipeDir.listFiles();
        assertThat("Json recipe directory not found", listFiles, is(notNullValue()));
        List<Object[]> recipeFiles = new ArrayList<>();
        for (File file : listFiles) {
            recipeFiles.add(new Object[]{file, file.getName()});
        }
        return recipeFiles;
    }

    @Test
    public void executesJsonTextRecipe() throws IOException {
        TestJobReport report = ExecutionTestHelper.makeFinishedReport("execution_ID");
        when(apiWrapper.postTestRecipe(any(TestRecipe.class), eq(false), any(HttpBasicAuth.class))).thenReturn(report);
        String recipeJsonText = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        //Mainly it tests that the text recipe is successfully deserialized into TestCase object and submitted to the server
        TestRecipe testRecipe = TestRecipeBuilder.createFrom(recipeJsonText);
        Execution execution = recipeExecutor.executeRecipe(testRecipe);
        assertThat(execution.getCurrentReport(), is(report));
    }

    public JsonTextRecipeExecutorTest(File file, String name) {
        this.file = file;
    }
}
