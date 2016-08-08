package com.smartbear.readyapi.client.execution;

import com.google.common.collect.Lists;
import com.smartbear.readyapi.client.RepositoryProjectExecutionRequest;
import com.smartbear.readyapi.client.model.DataSource;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.ExcelDataSource;
import com.smartbear.readyapi.client.model.FileDataSource;
import com.smartbear.readyapi.client.model.HarLogRoot;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;
import com.smartbear.readyapi.client.model.RequestTestStepBase;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.teststeps.TestSteps;
import com.sun.jersey.api.client.GenericType;
import io.swagger.client.Pair;
import io.swagger.client.auth.HttpBasicAuth;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CodegenBasedTestServerApi implements TestServerApi {

    private static final Logger logger = LoggerFactory.getLogger(CodegenBasedTestServerApi.class);

    private ApiClientWrapper apiClient;

    CodegenBasedTestServerApi() {
        this(new ApiClientWrapper());
    }

    public CodegenBasedTestServerApi(ApiClientWrapper apiClientWrapper) {
        apiClient = apiClientWrapper;
    }

    private static final String APPLICATION_JSON = "application/json";

    /**
     * Execute submitted test recipe
     *
     * @param testCase test case to run
     * @param async    if true server will return executionID immediately without waiting for execution to complete.
     *                 If false, it will wait for the execution to finish before it returns executionId and execution results.
     *                 Default is true;
     * @param auth     authentication object
     * @return ProjectResultReport execution result
     */
    @Override
    public ProjectResultReport postTestRecipe(TestCase testCase, boolean async, HttpBasicAuth auth) throws ApiException {
        // verify the required parameter 'testCase' is set
        if (testCase == null) {
            throw new ApiException(400, "Missing the required parameter 'testCase' when calling postTestRecipe");
        }
        verifyDataSourceFilesExist(testCase);
        setAuthentication(auth);

        // create path and map variables
        String path = (ServerDefaults.SERVICE_BASE_PATH + "/executions").replaceAll("\\{format\\}", "json");

        // query params
        List<Pair> queryParams = new ArrayList<>();
        queryParams.add(new Pair("async", String.valueOf(async)));

        Map<String, File> formParams = new HashMap<>();

        ProjectResultReport projectResultReport = invokeAPI(path, TestSteps.HttpMethod.POST.name(), testCase, APPLICATION_JSON, queryParams, formParams);
        return sendPendingFiles(testCase, projectResultReport, queryParams);
    }

    private void verifyDataSourceFilesExist(TestCase testCase) {
        for (TestStep testStep : testCase.getTestSteps()) {
            if (testStep instanceof DataSourceTestStep) {
                DataSource dataSource = ((DataSourceTestStep) testStep).getDataSource();
                if (dataSource.getExcel() != null) {
                    verifyFileExists(dataSource.getExcel().getFile());
                }
                if (dataSource.getFile() != null) {
                    verifyFileExists(dataSource.getFile().getFile());
                }
            }
        }
    }

    public void setConnectTimeout(int connectionTimeout) {
        apiClient.setConnectTimeout(connectionTimeout);
    }

    public void setDebugging(boolean debugging) {
        apiClient.setDebugging(debugging);
    }

    public ApiClientWrapper getApiClient() {
        return apiClient;
    }

    private ProjectResultReport sendPendingFiles(TestCase body, ProjectResultReport projectResultReport, List<Pair> queryParams) {
        String path = ServerDefaults.SERVICE_BASE_PATH + "/executions/" + projectResultReport.getExecutionID() + "/files";

        Map<String, File> formParams = buildFormParametersForDataSourceFiles(body);
        addClientCertificateFile(formParams, body.getClientCertFileName());
        addTestStepClientCertificateFile(body, formParams);
        if (formParams.isEmpty()) {
            return projectResultReport;
        }
        return invokeAPI(path, TestSteps.HttpMethod.POST.name(), body, "multipart/form-data", queryParams, formParams);
    }

    private void addTestStepClientCertificateFile(TestCase body, Map<String, File> formParams) {
        for (TestStep testStep : body.getTestSteps()) {
            if (testStep instanceof RequestTestStepBase) {
                RequestTestStepBase testStepBase = (RequestTestStepBase) testStep;
                addClientCertificateFile(formParams, testStepBase.getClientCertificateFileName());
            }
        }
    }

    private void addClientCertificateFile(Map<String, File> formParams, String clientCertFileName) {
        if (StringUtils.isNotEmpty(clientCertFileName)) {
            File certificateFile = new File(clientCertFileName);
            if (certificateFile.exists()) {
                formParams.put(certificateFile.getName(), certificateFile);
            } else {
                logger.warn("Client certificate file not found, file path: " + clientCertFileName +
                        ". Execution will fail unless file exists on TestServer and file path added to allowed file paths.");
            }
        }
    }

    /**
     * Returns last executions
     *
     * @return ProjectResultReports
     */
    @Override
    public ProjectResultReports getExecutions(HttpBasicAuth auth) throws ApiException {
        String path = ServerDefaults.SERVICE_BASE_PATH + "/executions";
        setAuthentication(auth);
        List<Pair> queryParams = new ArrayList<>();
        Map<String, File> formParams = new HashMap<>();

        GenericType returnType = new GenericType<ProjectResultReports>() {
        };
        return (ProjectResultReports) apiClient.invokeAPI(path, TestSteps.HttpMethod.GET.name(), queryParams, null, formParams,
                APPLICATION_JSON, APPLICATION_JSON, getAuthNames(), returnType);

    }

    /**
     * Cancels execution
     *
     * @param executionID execution id
     * @return ProjectResultReport
     */
    public ProjectResultReport cancelExecution(String executionID, HttpBasicAuth auth) throws ApiException {
        if (executionID == null) {
            throw new ApiException(400, "Missing the required parameter 'executionID' when calling cancelExecution");
        }
        setAuthentication(auth);
        String path = ServerDefaults.SERVICE_BASE_PATH + "/executions/" + executionID;

        return invokeAPI(path, TestSteps.HttpMethod.DELETE.name(), null, APPLICATION_JSON, new ArrayList<Pair>(),
                new HashMap<String, File>());

    }

    /**
     * Gets transaction log for the provided execution id and transaction id
     *
     * @param executionID   execution id
     * @param transactionId transaction id
     * @return HarLogRoot
     */
    public HarLogRoot getTransactionLog(String executionID, String transactionId, HttpBasicAuth auth) throws ApiException {
        if (executionID == null) {
            throw new ApiException(400, "Missing the required parameter 'executionID' when calling cancelExecution");
        }
        setAuthentication(auth);
        String path = ServerDefaults.SERVICE_BASE_PATH + "/executions/" + executionID + "/transactions/" + transactionId;

        return getTransactionLog(path, TestSteps.HttpMethod.GET.name(), null, APPLICATION_JSON, new ArrayList<Pair>(),
                new HashMap<String, File>());

    }

    private Map<String, File> buildFormParametersForDataSourceFiles(TestCase testCase) {
        Map<String, File> formParams = new HashMap<>();
        for (TestStep testStep : testCase.getTestSteps()) {
            if (testStep instanceof DataSourceTestStep) {
                DataSource dataSource = ((DataSourceTestStep) testStep).getDataSource();
                addDataSourceFile(formParams, dataSource.getExcel());
                addDataSourceFile(formParams, dataSource.getFile());
            }
        }
        return formParams;
    }

    private void addDataSourceFile(Map<String, File> formParams, FileDataSource fileDataSource) {
        if (fileDataSource != null) {
            File dataSourceFile = new File(fileDataSource.getFile());
            formParams.put(dataSourceFile.getName(), dataSourceFile);
        }
    }

    private void addDataSourceFile(Map<String, File> formParams, ExcelDataSource excelDataSource) {
        if (excelDataSource != null) {
            File dataSourceFile = new File(excelDataSource.getFile());
            formParams.put(dataSourceFile.getName(), dataSourceFile);
        }
    }

    private void verifyFileExists(String filePath) {
        if (!new File(filePath).exists()) {
            throw new ApiException(400, "Data source file not found: " + filePath);
        }
    }

    /**
     * Gets execution report
     *
     * @param executionID execution id received when test case was submitted for execution
     * @return ProjectResultReport execution result
     */
    @Override
    public ProjectResultReport getExecutionStatus(String executionID, HttpBasicAuth auth) throws ApiException {
        // verify the required parameter 'executionID' is set
        if (executionID == null) {
            throw new ApiException(400, "Missing the required parameter 'executionID' when calling getExecutionStatus");
        }
        setAuthentication(auth);

        // create path and map variables
        String path = ServerDefaults.SERVICE_BASE_PATH + "/executions/" + executionID + "/status";

        return invokeAPI(path, TestSteps.HttpMethod.GET.name(), null, APPLICATION_JSON, new ArrayList<Pair>());
    }

    private void setAuthentication(HttpBasicAuth auth) {
        if (auth != null) {
            apiClient.setUsername(auth.getUsername());
            apiClient.setPassword(auth.getPassword());
        }
    }

    private ProjectResultReport invokeAPI(String path, String method, Object postBody, String contentType,
                                          List<Pair> queryParams) throws ApiException {
        return invokeAPI(path, method, postBody, contentType, queryParams, new HashMap<String, File>());
    }

    private ProjectResultReport invokeAPI(String path, String method, Object postBody, String contentType,
                                          List<Pair> queryParams, Map<String, File> formParams) throws ApiException {

        return (ProjectResultReport) apiClient.invokeAPI(path, method, queryParams, postBody, formParams, APPLICATION_JSON, contentType,
                getAuthNames(), getReturnTypeProjectResultReport());
    }

    private HarLogRoot getTransactionLog(String path, String method, Object postBody, String contentType,
                                         List<Pair> queryParams, Map<String, File> formParams) throws ApiException {

        return (HarLogRoot) apiClient.invokeAPI(path, method, queryParams, postBody, formParams, APPLICATION_JSON, contentType,
                getAuthNames(), getReturnTypeHarLogRoot());
    }

    private String[] getAuthNames() {
        return new String[]{"basicAuth"};
    }

    private GenericType getReturnTypeProjectResultReport() {
        return new GenericType<ProjectResultReport>() {
        };
    }

    private GenericType getReturnTypeHarLogRoot() {
        return new GenericType<HarLogRoot>() {
        };
    }

    @Override
    public void setBasePath(String basePath) {
        apiClient.setBasePath(basePath);
    }

    @Override
    public ProjectResultReport postProject(ProjectExecutionRequest executionRequest, boolean async, HttpBasicAuth auth) throws ApiException {

        File projectFile = executionRequest.getProjectFile();
        if (!projectFile.exists()) {
            throw new ApiException(404, "File [" + projectFile.toString() + "] not found");
        }

        setAuthentication(auth);

        List<Pair> queryParams = buildQueryParameters(async, executionRequest.getTestCaseName(),
                executionRequest.getTestSuiteName(), executionRequest.getEnvironment());

        String path = ServerDefaults.SERVICE_BASE_PATH + "/executions";
        String type = "application/xml";

        try {
            // composite project?
            if (projectFile.isDirectory()) {
                projectFile = zipCompositeProject(projectFile);
                path += "/composite";
                type = "application/zip";
            } else {
                path += "/xml";
            }

            byte[] data = Files.readAllBytes(projectFile.toPath());

            return invokeAPI(path, TestSteps.HttpMethod.POST.name(), data, type, queryParams, null);
        } catch (IOException e) {
            throw new ApiException(500, "Failed to read project; " + e.toString());
        }
    }

    @Override
    public ProjectResultReport postRepositoryProject(RepositoryProjectExecutionRequest request, boolean async, HttpBasicAuth auth) throws ApiException {
        setAuthentication(auth);
        List<Pair> queryParams = buildQueryParameters(async, request.getTestCaseName(), request.getTestSuiteName(),
                request.getEnvironment());
        queryParams.add(new Pair("projectFileName", request.getProjectFileName()));
        if (request.getRepositoryName() != null) {
            queryParams.add(new Pair("repositoryName", request.getRepositoryName()));
        }

        return invokeAPI(ServerDefaults.SERVICE_BASE_PATH + "/executions/project", TestSteps.HttpMethod.POST.name(), request.getCustomPropertiesMap().values(),
                "application/json", queryParams, null);
    }

    private List<Pair> buildQueryParameters(boolean async, String testCaseName, String testSuiteName, String environment) {
        List<Pair> queryParams = new ArrayList<>();
        queryParams.add(new Pair("async", String.valueOf(async)));
        if (testCaseName != null) {
            queryParams.add(new Pair("testCaseName", testCaseName));
        }
        if (testSuiteName != null) {
            queryParams.add(new Pair("testSuiteName", testSuiteName));
        }
        if (environment != null) {
            queryParams.add(new Pair("environment", environment));
        }
        return queryParams;
    }

    private File zipCompositeProject(File dir) throws IOException {
        File zipFile = File.createTempFile("soapui-project", ".zip");

        byte[] buffer = new byte[1024];

        try (
                FileOutputStream fout = new FileOutputStream(zipFile);
                ZipOutputStream zout = new ZipOutputStream(fout)) {

            List<String> files = Lists.newArrayList();
            populateFilesList(dir, files);

            for (String fileName : files) {
                File file = new File(fileName);

                try (FileInputStream fin = new FileInputStream(file)) {
                    String zipEntryName = fileName.substring(dir.getAbsolutePath().length());
                    zout.putNextEntry(new ZipEntry(zipEntryName));

                    int length;

                    while ((length = fin.read(buffer)) > 0) {
                        zout.write(buffer, 0, length);
                    }

                    zout.closeEntry();
                }
            }
        }

        return zipFile;
    }

    private void populateFilesList(File dir, List<String> files) throws IOException {
        File[] filesInDir = dir.listFiles();
        for (File file : filesInDir) {
            if (file.isFile()) {
                files.add(file.getAbsolutePath());
            } else {
                populateFilesList(file, files);
            }
        }
    }
}
