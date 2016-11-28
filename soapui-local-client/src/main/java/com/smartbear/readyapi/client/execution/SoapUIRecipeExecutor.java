package com.smartbear.readyapi.client.execution;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.testcase.WsdlProjectRunner;
import com.eviware.soapui.model.testsuite.ProjectRunner;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.model.testsuite.TestSuiteRunner;
import com.eviware.soapui.support.types.StringToObjectMap;
import com.smartbear.ready.recipe.JsonRecipeParser;
import com.smartbear.ready.recipe.PropertyTransferSource;
import com.smartbear.ready.recipe.PropertyTransferTarget;
import com.smartbear.ready.recipe.assertions.*;
import com.smartbear.ready.recipe.teststeps.AuthenticationStruct;
import com.smartbear.ready.recipe.teststeps.GroovyScriptTestStepStruct;
import com.smartbear.ready.recipe.teststeps.JdbcRequestTestStepStruct;
import com.smartbear.ready.recipe.teststeps.ParamStruct;
import com.smartbear.ready.recipe.teststeps.PropertyTransferStruct;
import com.smartbear.ready.recipe.teststeps.PropertyTransferTestStepStruct;
import com.smartbear.ready.recipe.teststeps.RestTestRequestStepStruct;
import com.smartbear.ready.recipe.teststeps.SoapParamStruct;
import com.smartbear.ready.recipe.teststeps.SoapTestRequestStepStruct;
import com.smartbear.ready.recipe.teststeps.TestCaseStruct;
import com.smartbear.ready.recipe.teststeps.TestStepStruct;
import com.smartbear.readyapi.client.model.*;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import io.swagger.client.auth.HttpBasicAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Class that can execute a Test recipe locally, using the SoapUI core classes.
 */
public class SoapUIRecipeExecutor implements RecipeExecutionSupport {
    private static final Logger logger = LoggerFactory.getLogger(SoapUIRecipeExecutor.class);

    private static final String TEST_STEP_STRUCT_TYPE = "TestStepStruct";
    private static final String CONTAINS_ASSERTION_TYPE = "Contains";
    private static final String NOT_CONTAINS_ASSERTION_TYPE = "Not Contains";
    private static final String VALID_HTTP_STATUSES_TYPE = "Valid HTTP Status Codes";
    private static final String INVALID_HTTP_STATUSES_TYPE = "Invalid HTTP Status Codes";
    private static final String JDBC_STATUS_ASSERTION_TYPE = "JDBC Status";
    private static final String JDBC_TIMEOUT_ASSERTION_TYPE = "JDBC Timeout";
    private static final String JSON_PATH_COUNT_ASSERTION_TYPE = "JsonPath Count";
    private static final String JSON_PATH_MATCH_ASSERTION_TYPE = "JsonPath Match";
    private static final String XPATH_MATCH_ASSERTION_TYPE = "XPath Match";
    private static final String XQUERY_MATCH_ASSERTION_TYPE = "XQuery Match";
    private static final String RESPONSE_SLA_ASSERTION_TYPE = "Response SLA";
    private static final String SCRIPT_ASSERTION_TYPE = "Script Assertion";
    private static final String NOT_SOAP_FAULT_ASSERTION = "Not SOAP Fault";

    private final Map<String,ProjectRunner> executionsMap = new HashMap<>();
    private final JsonRecipeParser recipeParser = new JsonRecipeParser();

    @Override
    public ProjectResultReport postTestRecipe(TestCase testCase, boolean async, HttpBasicAuth auth) throws ApiException {
        String executionId = UUID.randomUUID().toString();
        try {
            WsdlProject project = recipeParser.parse(makeTestCaseStruct(testCase));
            WsdlProjectRunner projectRunner = new WsdlProjectRunner(project, new StringToObjectMap());
            executionsMap.put(executionId, projectRunner);
            projectRunner.start(async);
            return makeProjectResultReport(projectRunner, executionId);
        } catch (Exception e) {
            throw new RecipeExecutionException("Failed to execute Test recipe", e);
        }
    }

    private TestCaseStruct makeTestCaseStruct(TestCase testCase) {
        boolean maintainSession = nullSafeBoolean(testCase.getMaintainSession());
        boolean abortOnError = nullSafeBoolean(testCase.getAbortOnError());
        boolean failTestCaseOnError = nullSafeBoolean(testCase.getFailTestCaseOnError());
        boolean discardOkResults = nullSafeBoolean(testCase.getDiscardOkResults());
        String socketTimeout = testCase.getSocketTimeout();
        int testCaseTimeout = testCase.getTestCaseTimeout() == null ? 0 : testCase.getTestCaseTimeout();
        TestStepStruct[] testSteps = convertTestSteps(testCase.getTestSteps());
        String clientCertFile = testCase.getClientCertFileName();
        String clientCertPassword = testCase.getClientCertPassword();
        return new TestCaseStruct(testCase.getSearchProperties(), maintainSession,
                abortOnError, failTestCaseOnError,
                discardOkResults, socketTimeout, testCaseTimeout, testSteps,
                clientCertFile, clientCertPassword);
    }

    private TestStepStruct[] convertTestSteps(List<TestStep> testSteps) {
        TestStepStruct[] testStepStructs = new TestStepStruct[testSteps.size()];
        int index = 0;
        for (TestStep sourceTestStep : testSteps) {
            testStepStructs[index] = convertToStruct(sourceTestStep);
            index++;
        }
        return testStepStructs;
    }

    private AuthenticationStruct nullAuthenticationConversion(Authentication authentication){
         return authentication != null ? convertToAuthenticationStruct(authentication): null;
    }

    private TestStepStruct convertToStruct(TestStep testStep) {
        String stepName = testStep.getName();
        String typeName = testStep.getType();
        if (typeName.equals(TEST_STEP_STRUCT_TYPE)) {
            return new TestStepStruct(typeName, stepName);
        }
        try {
            TestStepTypes testStepType = TestStepTypes.fromString(typeName);
            switch (testStepType) {
                case REST_REQUEST:
                    RestTestRequestStep requestTestStep = (RestTestRequestStep) testStep;
                    AuthenticationStruct authenticationStruct = nullAuthenticationConversion(requestTestStep.getAuthentication());
                    ParamStruct[] parameters = convertToParamStructArray(requestTestStep.getParameters());
                    AssertionStruct[] assertions = convertToAssertionStructArray(requestTestStep.getAssertions());
                    return new RestTestRequestStepStruct(typeName, stepName, requestTestStep.getMethod(),
                            requestTestStep.getURI(), requestTestStep.getRequestBody(), authenticationStruct, parameters, assertions,
                            requestTestStep.getHeaders(), requestTestStep.getEncoding(), requestTestStep.getTimeout(), requestTestStep.getMediaType(),
                            nullSafeBoolean(requestTestStep.getFollowRedirects()), nullSafeBoolean(requestTestStep.getEntitizeParameters()),
                            nullSafeBoolean(requestTestStep.getPostQueryString()), requestTestStep.getClientCertificateFileName(),
                            requestTestStep.getClientCertificatePassword());
                case SOAP_REQUEST:
                    SoapRequestTestStep soapTestStep = (SoapRequestTestStep) testStep;
                    AuthenticationStruct soapAuthentication = nullAuthenticationConversion(soapTestStep.getAuthentication());
                    SoapParamStruct[] soapParameters = convertToSoapParameterStructs(soapTestStep.getParameters());
                    return new SoapTestRequestStepStruct(typeName, stepName, soapTestStep.getWsdl(), soapTestStep.getBinding(),
                            soapTestStep.getOperation(), soapTestStep.getURI(), soapTestStep.getRequestBody(), soapAuthentication,
                            soapParameters, convertToAssertionStructArray(soapTestStep.getAssertions()), soapTestStep.getHeaders(),
                            soapTestStep.getEncoding(), soapTestStep.getTimeout(), nullSafeBoolean(soapTestStep.getFollowRedirects()),
                            nullSafeBoolean(soapTestStep.getEntitizeParameters()), soapTestStep.getClientCertificateFileName(),
                            soapTestStep.getClientCertificatePassword());
                case PROPERTY_TRANSFER:
                    PropertyTransferTestStep propertyTransferTestStep = (PropertyTransferTestStep) testStep;
                    return new PropertyTransferTestStepStruct(typeName, stepName,
                            convertToPropertyTransferStructs(propertyTransferTestStep.getTransfers()));
                case GROOVY_SCRIPT:
                    GroovyScriptTestStep groovyTestStep = (GroovyScriptTestStep) testStep;
                    return new GroovyScriptTestStepStruct(typeName, stepName, groovyTestStep.getScript());
                case JDBC_REQUEST:
                    JdbcRequestTestStep jdbcTestStep = (JdbcRequestTestStep) testStep;
                    return new JdbcRequestTestStepStruct(typeName, stepName, jdbcTestStep.getDriver(),
                            jdbcTestStep.getConnectionString(), jdbcTestStep.getSqlQuery(),
                            nullSafeBoolean(jdbcTestStep.getStoredProcedure()), jdbcTestStep.getProperties(),
                            convertToAssertionStructArray(jdbcTestStep.getAssertions()));
                default:
                    throw new IllegalArgumentException("Test step type not supported in SoapUI: " + testStepType);
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidRecipeException("Failed to process Test recipe - error: " + e.getMessage(), e);
        }
    }

    private PropertyTransferStruct[] convertToPropertyTransferStructs(List<PropertyTransfer> transfers) {
        PropertyTransferStruct[] propertyTransferStructs = new PropertyTransferStruct[transfers.size()];
        for (int i = 0; i < propertyTransferStructs.length; i++) {
            PropertyTransfer transfer = transfers.get(i);
            com.smartbear.readyapi.client.model.PropertyTransferSource transferSource = transfer.getSource();
            PropertyTransferSource sourceStruct = new PropertyTransferSource(transferSource.getSourceName(), transferSource.getProperty(),
                    transferSource.getPathLanguage(), transferSource.getPath());
            com.smartbear.readyapi.client.model.PropertyTransferTarget transferTarget = transfer.getTarget();
            PropertyTransferTarget targetStruct = new PropertyTransferTarget(transferTarget.getTargetName(), transferTarget.getProperty(),
                    transferTarget.getPathLanguage(), transferTarget.getPath());
            propertyTransferStructs[i] = new PropertyTransferStruct(transfer.getTransferName(), sourceStruct, targetStruct,
                    nullSafeBoolean(transfer.getFailTransferOnError()), nullSafeBoolean(transfer.getSetNullOnMissingSource()),
                    nullSafeBoolean(transfer.getTransferTextContent()), nullSafeBoolean(transfer.getIgnoreEmptyValue()),
                    nullSafeBoolean(transfer.getTransferToAll()), nullSafeBoolean(transfer.getTransferChildNodes()),
                    nullSafeBoolean(transfer.getEntitizeTransferredValues()));

        }
        return propertyTransferStructs;
    }

    private AssertionStruct[] convertToAssertionStructArray(List<Assertion> assertions) {
        AssertionStruct[] assertionStructs = new AssertionStruct[assertions.size()];
        for (int i = 0; i < assertionStructs.length; i++) {
            Assertion sourceAssertion = assertions.get(i);
            String name = sourceAssertion.getName();
            String assertionType = sourceAssertion.getType();
            switch (assertionType) {
                case CONTAINS_ASSERTION_TYPE:
                    SimpleContainsAssertion containsAssertion = (SimpleContainsAssertion) sourceAssertion;
                    assertionStructs[i] = new SimpleContainsAssertionStruct(name, containsAssertion.getToken(), nullSafeBoolean(containsAssertion.getIgnoreCase()),
                            nullSafeBoolean(containsAssertion.getUseRegexp()));
                    break;
                case NOT_CONTAINS_ASSERTION_TYPE:
                    SimpleNotContainsAssertion notContainsAssertion = (SimpleNotContainsAssertion) sourceAssertion;
                    assertionStructs[i] = new SimpleContainsAssertionStruct(name, notContainsAssertion.getToken(), nullSafeBoolean(notContainsAssertion.getIgnoreCase()),
                            nullSafeBoolean(notContainsAssertion.getUseRegexp()));
                    break;
                case VALID_HTTP_STATUSES_TYPE:
                    ValidHttpStatusCodesAssertion validStatusesAssertion = (ValidHttpStatusCodesAssertion) sourceAssertion;
                    List<String> validStatusCodes = validStatusesAssertion.getValidStatusCodes();
                    assertionStructs[i] = new InvalidHttpStatusCodesAssertionStruct(name, validStatusCodes.toArray(new String[validStatusCodes.size()]));
                    break;
                case INVALID_HTTP_STATUSES_TYPE:
                    InvalidHttpStatusCodesAssertion invalidStatusesAssertion = (InvalidHttpStatusCodesAssertion) sourceAssertion;
                    List<String> invalidStatusCodes = invalidStatusesAssertion.getInvalidStatusCodes();
                    assertionStructs[i] = new InvalidHttpStatusCodesAssertionStruct(name, invalidStatusCodes.toArray(new String[invalidStatusCodes.size()]));
                    break;
                case JDBC_STATUS_ASSERTION_TYPE:
                    assertionStructs[i] = new JdbcStatusAssertionStruct(name);
                    break;
                case JDBC_TIMEOUT_ASSERTION_TYPE:
                    JdbcTimeoutAssertion timeoutAssertion = (JdbcTimeoutAssertion) sourceAssertion;
                    assertionStructs[i] = new JdbcTimeoutAssertionStruct(name, timeoutAssertion.getTimeout());
                    break;
                case JSON_PATH_COUNT_ASSERTION_TYPE:
                    JsonPathCountAssertion countAssertion = (JsonPathCountAssertion) sourceAssertion;
                    assertionStructs[i] = new JsonPathCountAssertionStruct(name, countAssertion.getJsonPath(),
                            countAssertion.getExpectedCount(), nullSafeBoolean(countAssertion.getAllowWildcards()));
                    break;
                case JSON_PATH_MATCH_ASSERTION_TYPE:
                    JsonPathContentAssertion contentAssertion = (JsonPathContentAssertion) sourceAssertion;
                    assertionStructs[i] = new JsonPathContentAssertionStruct(name, contentAssertion.getJsonPath(),
                            contentAssertion.getExpectedContent(), nullSafeBoolean(contentAssertion.getAllowWildcards()));
                    break;
                case XPATH_MATCH_ASSERTION_TYPE:
                    XPathContainsAssertion xpathAssertion = (XPathContainsAssertion) sourceAssertion;
                    assertionStructs[i] = new XPathContainsAssertionStruct(name, xpathAssertion.getXpath(),
                            xpathAssertion.getExpectedContent(), nullSafeBoolean(xpathAssertion.getAllowWildcards()),
                            nullSafeBoolean(xpathAssertion.getIgnoreNamespaces()),
                            nullSafeBoolean(xpathAssertion.getIgnoreComments()));
                    break;
                case XQUERY_MATCH_ASSERTION_TYPE:
                    XQueryContainsAssertion xqueryAssertion = (XQueryContainsAssertion) sourceAssertion;
                    assertionStructs[i] = new XQueryContainsAssertionStruct(name, xqueryAssertion.getXquery(),
                            xqueryAssertion.getExpectedContent(), nullSafeBoolean(xqueryAssertion.getAllowWildcards()));
                    break;
                case RESPONSE_SLA_ASSERTION_TYPE:
                    ResponseSLAAssertion slaAssertion = (ResponseSLAAssertion) sourceAssertion;
                    assertionStructs[i] = new ResponseSLAAssertionStruct(name, slaAssertion.getMaxResponseTime());
                    break;
                case SCRIPT_ASSERTION_TYPE:
                    GroovyScriptAssertion scriptAssertion = (GroovyScriptAssertion) sourceAssertion;
                    assertionStructs[i] = new GroovyScriptAssertionStruct(name, scriptAssertion.getScript());
                    break;
                case NOT_SOAP_FAULT_ASSERTION:
                    assertionStructs[i] = new NotSoapFaultAssertionStruct(name);
                    break;
                default:
                    logger.warn("No such assertion type defined in the soapui local client api");
                    break;
            }

        }
        return assertionStructs;
    }

    private ParamStruct[] convertToParamStructArray(List<RestParameter> parameters) {
        ParamStruct[] paramStructs = new ParamStruct[parameters.size()];
        for (int i = 0; i < paramStructs.length; i++) {
            RestParameter parameter = parameters.get(i);
            paramStructs[i] = new ParamStruct(parameter.getType().toString(), parameter.getName(), parameter.getValue());
        }
        return paramStructs;
    }

    private SoapParamStruct[] convertToSoapParameterStructs(List<SoapParameter> parameters) {
        SoapParamStruct[] paramStructs = new SoapParamStruct[parameters.size()];
        for (int i = 0; i < paramStructs.length; i++) {
            SoapParameter parameter = parameters.get(i);
            paramStructs[i] = new SoapParamStruct(parameter.getPath(), parameter.getName(), parameter.getValue());
        }
        return paramStructs;
    }

    private AuthenticationStruct convertToAuthenticationStruct(Authentication authentication) {
        return new AuthenticationStruct(authentication.getType(), authentication.getUsername(), authentication.getPassword(),
                authentication.getDomain());
    }

    private boolean nullSafeBoolean(Boolean possiblyNull) {
        return possiblyNull != null && possiblyNull;
    }

    @Override
    public ProjectResultReport getExecutionStatus(String executionID, HttpBasicAuth auth) throws ApiException {
        ProjectRunner projectRunner = executionsMap.get(executionID);
        if (projectRunner == null) {
            return null;
        } else {
            return makeProjectResultReport(projectRunner, executionID);
        }
    }

    @Override
    public ProjectResultReports getExecutions(HttpBasicAuth auth) throws ApiException {
        return null;
    }

    @Override
    public ProjectResultReport cancelExecution(String executionID, HttpBasicAuth auth) throws ApiException {
        return null;
    }

    private ProjectResultReport makeProjectResultReport(TestRunner testRunner, String executionID) {
        ProjectResultReport report = new ProjectResultReport();
        report.setStatus(convertTestRunnerStatus(testRunner.getStatus()));
        report.setTimeTaken(testRunner.getTimeTaken());
        report.setStartTime(testRunner.getStartTime());
        report.setExecutionID(executionID);
        List<TestSuiteResultReport> testSuiteResultReports = new ArrayList<>();
        ProjectRunner projectRunner = (ProjectRunner) testRunner;
        report.setProjectName(projectRunner.getProject().getName());
        for (TestSuiteRunner testSuiteResult : projectRunner.getResults()) {
            testSuiteResultReports.add(makeTestSuiteResultReport(testSuiteResult));
        }
        report.setTestSuiteResultReports(testSuiteResultReports);
        return report;
    }

    private TestSuiteResultReport makeTestSuiteResultReport(TestSuiteRunner runner) {
        TestSuiteResultReport report = new TestSuiteResultReport();
        report.setTestSuiteName(runner.getTestSuite().getName());
        List<TestCaseResultReport> testCaseResultReports = new ArrayList<>();
        for (TestCaseRunner testCaseResult : runner.getResults()) {
            testCaseResultReports.add(makeTestCaseResultReport(testCaseResult));
        }
        report.setTestCaseResultReports(testCaseResultReports);
        return report;
    }

    private TestCaseResultReport makeTestCaseResultReport(TestCaseRunner testCaseResult) {
        TestCaseResultReport report = new TestCaseResultReport();
        report.setTestCaseName(testCaseResult.getTestCase().getName());
        List<TestStepResultReport> testStepResultReports = new ArrayList<>();
        for (TestStepResult stepResult : testCaseResult.getResults()) {
            testStepResultReports.add(makeTestStepResultReport(stepResult));
        }
        report.setTestStepResultReports(testStepResultReports);
        return report;
    }

    private TestStepResultReport makeTestStepResultReport(TestStepResult result) {
        TestStepResultReport report = new TestStepResultReport();
        report.setTestStepName(result.getTestStep().getName());
        report.setMessages(Arrays.asList(result.getMessages()));
        report.setAssertionStatus(convertTestStepStatus(result.getStatus()));
        report.setTimeTaken(result.getTimeTaken());
        return report;
    }

    private TestStepResultReport.AssertionStatusEnum convertTestStepStatus(TestStepResult.TestStepStatus status) {
        switch(status) {
            case UNKNOWN:
                return TestStepResultReport.AssertionStatusEnum.UNKNOWN;
            case OK:
                return TestStepResultReport.AssertionStatusEnum.OK;
            case FAILED:
                return TestStepResultReport.AssertionStatusEnum.FAILED;
            case CANCELED:
                return TestStepResultReport.AssertionStatusEnum.CANCELED;
            default:
                throw new Error("Unexpected Test step status: " + status);
        }
    }

    private ProjectResultReport.StatusEnum convertTestRunnerStatus(TestRunner.Status status) {
        switch(status) {
            case INITIALIZED:
                return ProjectResultReport.StatusEnum.INITIALIZED;
            case RUNNING:
                return ProjectResultReport.StatusEnum.RUNNING;
            case CANCELED:
                return ProjectResultReport.StatusEnum.CANCELED;
            case FINISHED:
                return ProjectResultReport.StatusEnum.FINISHED;
            case FAILED:
                return ProjectResultReport.StatusEnum.FAILED;
            case WARNING:
                return ProjectResultReport.StatusEnum.WARNING;
            default:
                throw new Error("Unexpected Test Runner status: " + status);
        }
    }

}
