package io.swagger.assert4j.local.execution;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.testcase.WsdlProjectRunner;
import com.eviware.soapui.model.iface.MessageExchange;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.*;
import com.google.common.collect.Maps;
import io.swagger.assert4j.client.model.*;
import io.swagger.assert4j.execution.Execution;
import io.swagger.assert4j.execution.RecipeExecutionException;
import io.swagger.assert4j.result.AbstractRecipeExecutionResult;
import io.swagger.assert4j.result.AbstractTestStepResult;
import io.swagger.assert4j.result.RecipeExecutionResult;

import java.util.*;

public class SoapUIRecipeExecution implements Execution {

    private Map<TestStepResultReport, MessageExchange[]> messageExchangeMap = Maps.newConcurrentMap();
    private final String executionId;
    private final WsdlProjectRunner projectRunner;
    private TestJobReport finalReport;

    SoapUIRecipeExecution(String executionId, WsdlProjectRunner projectRunner) {
        this.executionId = executionId;
        this.projectRunner = projectRunner;
    }

    @Override
    public String getId() {
        return executionId;
    }

    @Override
    public TestJobReport.StatusEnum getCurrentStatus() {
        return convertTestRunnerStatus(projectRunner.getStatus());
    }

    public WsdlProject getProject() {
        return (WsdlProject) projectRunner.getProject();
    }

    @Override
    public TestJobReport getCurrentReport() {
        if (finalReport != null) {
            return finalReport;
        }

        TestJobReport report = new TestJobReport();
        report.setStatus(convertTestRunnerStatus(projectRunner.getStatus()));
        report.setTotalTime(projectRunner.getTimeTaken());
        report.setStartTime(projectRunner.getStartTime());
        report.setTestjobId(executionId);
        List<TestSuiteResultReport> testSuiteResultReports = new ArrayList<>();
        report.setProjectName(projectRunner.getProject().getName());
        for (TestSuiteRunner testSuiteResult : projectRunner.getResults()) {
            testSuiteResultReports.add(makeTestSuiteResultReport(testSuiteResult));
        }
        report.setTestSuiteResultReports(testSuiteResultReports);

        if (report.getStatus() == TestJobReport.StatusEnum.FINISHED) {
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
                throw new RecipeExecutionException("Unexpected Test step status: " + status);
        }
    }

    private TestJobReport.StatusEnum convertTestRunnerStatus(TestRunner.Status status) {
        switch (status) {
            case RUNNING:
                return TestJobReport.StatusEnum.RUNNING;
            case CANCELED:
                return TestJobReport.StatusEnum.CANCELED;
            case FINISHED:
                return TestJobReport.StatusEnum.FINISHED;
            case FAILED:
                return TestJobReport.StatusEnum.FAILED;
            default:
                throw new RecipeExecutionException("Unexpected Test Runner status: " + status);
        }
    }

    public static class SoapUIExecutionResult extends AbstractRecipeExecutionResult {
        SoapUIExecutionResult(TestJobReport currentReport, SoapUIRecipeExecution execution) {
            super(currentReport, e -> new SoapUITestStepResult(e, execution));
        }
    }

    public static class SoapUITestStepResult extends AbstractTestStepResult {
        private final SoapUIRecipeExecution execution;

        SoapUITestStepResult(TestStepResultReport testStepResultReport, SoapUIRecipeExecution execution) {
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
