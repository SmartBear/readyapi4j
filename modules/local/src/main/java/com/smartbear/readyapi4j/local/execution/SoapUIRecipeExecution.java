package com.smartbear.readyapi4j.local.execution;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.testcase.WsdlProjectRunner;
import com.eviware.soapui.model.iface.MessageExchange;
import com.eviware.soapui.model.testsuite.MessageExchangeTestStepResult;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestProperty;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.model.testsuite.TestSuiteRunner;
import com.google.common.collect.Maps;
import com.smartbear.readyapi.client.model.HarEntry;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestCaseResultReport;
import com.smartbear.readyapi.client.model.TestStepResultReport;
import com.smartbear.readyapi.client.model.TestSuiteResultReport;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.result.AbstractRecipeExecutionResult;
import com.smartbear.readyapi4j.result.AbstractTestStepResult;
import com.smartbear.readyapi4j.result.RecipeExecutionResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoapUIRecipeExecution implements Execution {

    private Map<TestStepResultReport, MessageExchange[]> messageExchangeMap = Maps.newConcurrentMap();
    private final String executionId;
    private final WsdlProjectRunner projectRunner;
    private ProjectResultReport finalReport;

    public SoapUIRecipeExecution(String executionId, WsdlProjectRunner projectRunner) {
        this.executionId = executionId;
        this.projectRunner = projectRunner;
    }

    @Override
    public String getId() {
        return executionId;
    }

    @Override
    public ProjectResultReport.StatusEnum getCurrentStatus() {
        return convertTestRunnerStatus(projectRunner.getStatus());
    }

    public WsdlProject getProject() {
        return (WsdlProject) projectRunner.getProject();
    }

    @Override
    public ProjectResultReport getCurrentReport() {
        if (finalReport != null) {
            return finalReport;
        }

        ProjectResultReport report = new ProjectResultReport();
        report.setStatus(convertTestRunnerStatus(projectRunner.getStatus()));
        report.setTimeTaken(projectRunner.getTimeTaken());
        report.setStartTime(projectRunner.getStartTime());
        report.setExecutionID(executionId);
        List<TestSuiteResultReport> testSuiteResultReports = new ArrayList<>();
        report.setProjectName(projectRunner.getProject().getName());
        for (TestSuiteRunner testSuiteResult : projectRunner.getResults()) {
            testSuiteResultReports.add(makeTestSuiteResultReport(testSuiteResult));
        }
        report.setTestSuiteResultReports(testSuiteResultReports);

        if (report.getStatus() == ProjectResultReport.StatusEnum.FINISHED) {
            finalReport = report;
        }

        return report;
    }

    @Override
    public RecipeExecutionResult getExecutionResult() {
        return new SoapUIExecutionResult(getCurrentReport(), this);
    }

    @Override
    public List<String> getErrorMessages() {
        return getExecutionResult().getErrorMessages();
    }

    @Override
    public void cancelExecution() {
        projectRunner.cancel("Canceled by user");
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
        TestCase testCase = testCaseResult.getTestCase();
        report.setTestCaseName(testCase.getName());
        Map<String, String> testCaseProperties = new HashMap<>();
        report.setProperties(testCaseProperties);
        for (TestProperty testProperty : testCase.getProperties().values()) {
            testCaseProperties.put(testProperty.getName(), testProperty.getValue());
        }
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

        if (result instanceof MessageExchangeTestStepResult) {
            messageExchangeMap.put(report, ((MessageExchangeTestStepResult) result).getMessageExchanges());
        }

        return report;
    }

    private TestStepResultReport.AssertionStatusEnum convertTestStepStatus(TestStepResult.TestStepStatus status) {
        switch (status) {
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
        switch (status) {
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

    public static class SoapUIExecutionResult extends AbstractRecipeExecutionResult {
        public SoapUIExecutionResult(ProjectResultReport currentReport, SoapUIRecipeExecution execution) {
            super(currentReport, e -> new SoapUITestStepResult(e, execution));
        }
    }

    public static class SoapUITestStepResult extends AbstractTestStepResult {
        private final SoapUIRecipeExecution execution;

        public SoapUITestStepResult(TestStepResultReport testStepResultReport, SoapUIRecipeExecution execution) {
            super(testStepResultReport);
            this.execution = execution;
        }

        @Override
        public HarEntry getHarEntry() {
            if (execution.hasMessageExchange(testStepResultReport)) {
                return new HarEntryBuilder().createHarEntry(execution.getMessageExchange(testStepResultReport));
            }

            return null;
        }
    }

    public MessageExchange getMessageExchange(TestStepResultReport testStepResultReport) {
        return messageExchangeMap.get(testStepResultReport)[0];
    }

    public boolean hasMessageExchange(TestStepResultReport testStepResultReport) {
        MessageExchange[] messageExchanges = messageExchangeMap.get(testStepResultReport);
        return messageExchanges != null && messageExchanges.length > 0;
    }
}
