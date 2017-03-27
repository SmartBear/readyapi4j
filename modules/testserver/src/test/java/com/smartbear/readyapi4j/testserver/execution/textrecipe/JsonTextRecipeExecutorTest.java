package com.smartbear.readyapi4j.testserver.execution.textrecipe;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.TestRecipeBuilder;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.testserver.execution.ExecutionTestHelper;
import com.smartbear.readyapi4j.testserver.execution.ProjectExecutionTestBase;
import io.swagger.client.auth.HttpBasicAuth;
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
        ProjectResultReport report = ExecutionTestHelper.makeFinishedReport("execution_ID");
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
