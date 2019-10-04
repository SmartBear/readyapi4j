package com.smartbear.readyapi4j.testengine.teststeps.datasource.datagen;

import com.smartbear.readyapi4j.ApiException;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.client.model.*;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.execution.ExecutionListener;
import com.smartbear.readyapi4j.testengine.execution.*;
import com.smartbear.readyapi4j.testengine.teststeps.ServerTestSteps;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;
import com.sun.jersey.api.client.GenericType;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi4j.teststeps.TestSteps.GET;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class DataSourceTestStepRecipeTest {

    private static final String EXECUTIONS_BASE_PATH = ServerDefaults.SERVICE_BASE_PATH + "/executions";

    @Test
    public void buildsRecipeWithDataSourceTestStepWithGridDataSource() throws Exception {
        final List<String> propertyValues = Arrays.asList("Value1", "Value");
        TestRecipe recipe = newTestRecipe()
                .addStep(ServerTestSteps.gridDataSource()
                        .addProperty("property1", propertyValues)
                        .addProperty("property2", propertyValues)
                )
                .buildTestRecipe();

        TestStep testStep = recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getType(), is(TestStepTypes.DATA_SOURCE.getName()));

        Map<String, List<String>> grid = ((DataSourceTestStep) testStep).getDataSource().getGrid();
        assertThat(grid.size(), is(2));
        assertThat(grid.get("property1"), is(propertyValues));
        assertThat(grid.get("property2"), is(propertyValues));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithExcelDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(ServerTestSteps.excelDataSource()
                        .addProperty("property1")
                        .addProperty("property2")
                        .withFilePath("ExcelFilePath")
                        .withWorksheet("Worksheet1")
                        .startAtCell("A1")
                        .ignoreEmpty()
                )
                .buildTestRecipe();

        DataSourceTestStep testStep = (DataSourceTestStep) recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getType(), is(TestStepTypes.DATA_SOURCE.getName()));

        DataSource dataSource = testStep.getDataSource();
        assertThat(dataSource.getProperties(), is(Arrays.asList("property1", "property2")));

        ExcelDataSource excel = dataSource.getExcel();
        assertThat(excel.getFile(), is("ExcelFilePath"));
        assertThat(excel.getWorksheet(), is("Worksheet1"));
        assertThat(excel.getStartAtCell(), is("A1"));
        assertThat(excel.isIgnoreEmpty(), is(true));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithFileDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(ServerTestSteps.fileDataSource()
                        .addProperty("property1")
                        .addProperty("property2")
                        .withFilePath("FilePath")
                        .withCharSet("UTF-8")
                        .withSeparator(",")
                        .quotedValues()
                        .trim()
                )
                .buildTestRecipe();

        DataSourceTestStep testStep = (DataSourceTestStep) recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getType(), is(TestStepTypes.DATA_SOURCE.getName()));

        DataSource dataSource = testStep.getDataSource();
        assertThat(dataSource.getProperties(), is(Arrays.asList("property1", "property2")));

        FileDataSource file = dataSource.getFile();
        assertThat(file.getFile(), is("FilePath"));
        assertThat(file.getCharset(), is("UTF-8"));
        assertThat(file.getSeparator(), is(","));
        assertThat(file.isQuotedValues(), is(true));
        assertThat(file.isTrim(), is(true));
    }

    @Test
    public void throwsExceptionIfDataSourceFileDoesNotExist() {
        TestRecipe recipe = newTestRecipe().addStep(
                ServerTestSteps.excelDataSource()
                        .withFilePath("abc.xlsx")
                        .withWorksheet("Sheet1")
                        .addProperty("cityName")
                        .startAtCell("A1")
        )
                .buildTestRecipe();

        TestEngineRecipeExecutor recipeExecutor = new TestEngineClient("localhost", ServerDefaults.DEFAULT_PORT).createRecipeExecutor();
        recipeExecutor.addExecutionListener(new ExecutionListener() {
            @Override
            public void errorOccurred(Exception exception) {
                assertThat(exception.getMessage(), is("Data source file not found: abc.xlsx"));
            }
        });

        try {
            recipeExecutor.submitRecipe(recipe);
            assertTrue(false);
        } catch (com.smartbear.readyapi4j.testengine.execution.ApiException e) {
        }
    }

    @Test
    @Ignore( "need to package excel file into zip with project")
    public void sendsRecipeWithExcelDataSource() throws Exception {
        final String dataSourceFilePath = getClass().getResource("/DataSource.xlsx").getPath();

        TestRecipe recipe = newTestRecipe().
                addStep(
                        ServerTestSteps.excelDataSource()
                                .withFilePath(dataSourceFilePath)
                                .withWorksheet("Sheet1")
                                .addProperty("cityName")
                                .startAtCell("A1"))
                .addStep(
                        GET("http://maps.googleapis.com/maps/api/geocode/xml")
                                .addQueryParameter("address", "${DataSourceStep#cityName}")
                )
                .buildTestRecipe();

        ApiClientWrapper apiClientWrapper = mockApiClientWrapper();
        TestEngineApi testEngineApi = new CodegenBasedTestEngineApi(apiClientWrapper);
        TestEngineClient testEngineClient = RecipeExecutorTestCreator.createRecipeExecutor(ServerDefaults.DEFAULT_SCHEME, "localhost", ServerDefaults.DEFAULT_PORT,
                ServerDefaults.VERSION_PREFIX, testEngineApi);
        testEngineClient.setCredentials("user", "password");
        Execution execution = testEngineClient.createRecipeExecutor().executeRecipe(recipe);

        verify(apiClientWrapper, times(1)).invokeAPI(eq(EXECUTIONS_BASE_PATH), anyString(), anyList(), anyObject(),
                anyMap(), anyString(), eq("application/json"), any(String[].class), any(GenericType.class));

        ArgumentCaptor<Map> formDataArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(apiClientWrapper, times(1)).invokeAPI(eq(EXECUTIONS_BASE_PATH + "/" + execution.getId() + "/files"),
                anyString(), anyList(), anyObject(), formDataArgumentCaptor.capture(), anyString(),
                eq("multipart/form-data"), any(String[].class), any(GenericType.class));

        Iterator iterator = formDataArgumentCaptor.getValue().entrySet().iterator();
        Map.Entry<String, File> dataSourceFile = (Map.Entry<String, File>) iterator.next();
        assertThat(dataSourceFile.getKey(), is("DataSource.xlsx"));
        assertThat(dataSourceFile.getValue(), is(new File(dataSourceFilePath)));
    }

    private ApiClientWrapper mockApiClientWrapper() throws ApiException {
        ApiClientWrapper apiClientWrapper = mock(ApiClientWrapper.class);
        when(apiClientWrapper.parameterToPairs(anyString(), anyString(), anyObject())).thenCallRealMethod();
        when(apiClientWrapper.escapeString(anyString())).thenCallRealMethod();
        when(apiClientWrapper.serialize(anyObject(), anyString())).thenCallRealMethod();
        TestJobReport projectResultReport = new TestJobReport();
        projectResultReport.setTestjobId("exec_id");
        projectResultReport.setStatus(TestJobReport.StatusEnum.RUNNING);
        when(apiClientWrapper.invokeAPI(anyString(), anyString(), anyList(), anyObject(), anyMap(), anyString(), anyString(), any(String[].class), any(GenericType.class))).thenReturn(projectResultReport);

        return apiClientWrapper;
    }
}
