package io.swagger.assert4j.support;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.assert4j.client.model.TestJobReport;
import io.swagger.assert4j.client.model.TestSuiteResultReport;
import io.swagger.assert4j.execution.Execution;
import io.swagger.assert4j.execution.ExecutionListener;
import io.swagger.assert4j.result.TestStepResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import static io.swagger.assert4j.support.RecipeLogger.createFileName;

/**
 * ExecutionListener that writes response HAR entries to a single log file after execution
 */
public class ExecutionLogger implements ExecutionListener {

    private final static Logger LOG = LoggerFactory.getLogger(ExecutionLogger.class);

    public static final String DEFAULT_EXTENSION = "har";

    private final String targetFolder;
    private final String extension;

    public ExecutionLogger(String targetFolder, String extension) {
        this.targetFolder = targetFolder;
        this.extension = extension;
    }

    public ExecutionLogger(String targetFolder) {
        this(targetFolder, DEFAULT_EXTENSION);
    }

    @Override
    public void executionFinished(Execution execution) {
        try {
            File directory = new File(targetFolder);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            List<Map<Object, Object>> entries = Lists.newArrayList();
            for (TestStepResult result : execution.getExecutionResult().getTestStepResults()) {
                entries.add(getLogDataForResult(result));
            }

            File file;
            String name = createExecutionName(execution);

            if (StringUtils.isNotBlank(name)) {
                file = new File(directory, createFileName(name, '_') + "." + extension);
            } else {
                file = File.createTempFile("execution-" + execution.getId(), "." + extension, directory);
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(JsonUtils.pretty(entries).getBytes());
            }
        } catch (Exception e) {
            LOG.error("Failed to write response logs to file", e);
        }
    }

    private String createExecutionName(Execution execution) {
        TestJobReport currentReport = execution.getCurrentReport();
        String name = currentReport.getProjectName();
        List<TestSuiteResultReport> testSuiteResultReports = currentReport.getTestSuiteResultReports();
        if (testSuiteResultReports.size() == 1) {
            if (testSuiteResultReports.get(0).getTestCaseResultReports().size() == 1) {
                name = "TestCase " + testSuiteResultReports.get(0).getTestCaseResultReports().get(0).getTestCaseName();
            } else {
                name = "TestSuite " + testSuiteResultReports.get(0).getTestSuiteName();
            }
        }

        if (execution.getCurrentStatus() == TestJobReport.StatusEnum.FAILED) {
            name += "-" + execution.getCurrentStatus().name();
        }

        return name;
    }

    private Map<Object, Object> getLogDataForResult(TestStepResult testStepResult) {
        Map<Object, Object> result = Maps.newConcurrentMap();

        result.put("testStep", testStepResult.getTestStepName());
        result.put("timeTaken", testStepResult.getTimeTaken());
        result.put("status", testStepResult.getAssertionStatus());

        if (testStepResult.getMessages() != null && !testStepResult.getMessages().isEmpty()) {
            result.put("messsages", testStepResult.getMessages());
        }
        if (testStepResult.getHarEntry() != null) {
            result.put("harEntry", testStepResult.getHarEntry());
        }

        return result;
    }
}
