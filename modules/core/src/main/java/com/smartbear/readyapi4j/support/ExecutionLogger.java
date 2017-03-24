package com.smartbear.readyapi4j.support;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestSuiteResultReport;
import com.smartbear.readyapi4j.ExecutionListener;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.result.TestStepResult;
import io.swagger.util.Json;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import static com.smartbear.readyapi4j.support.RecipeLogger.createFileName;

/**
 * ExecutionListener that writes response HAR entries to a single log file after execution
 */
public class ExecutionLogger implements ExecutionListener {

    private final static Logger LOG = LoggerFactory.getLogger(ExecutionLogger.class);

    public static final String DEFAULT_EXTENSION = "json";

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

            List<Map<Object,Object>> entries = Lists.newArrayList();
            for(TestStepResult result : execution.getExecutionResult().getTestStepResults()){
                entries.add( getLogDataForResult(result));
            }

            File file;
            String name = getExecutionName(execution);

            if(StringUtils.isNotBlank(name)){
                file = new File( directory, createFileName( name, '_') + "." + extension );
            }
            else {
                file = File.createTempFile( "execution-" + execution.getId(), "." + extension, directory);
            }

            try ( FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write( Json.pretty(entries).getBytes());
            }
        } catch (Exception e) {
            LOG.error("Failed to write response logs to file", e);
        }
    }

    private String getExecutionName(Execution execution) {
        ProjectResultReport currentReport = execution.getCurrentReport();
        String name = currentReport.getProjectName();
        List<TestSuiteResultReport> testSuiteResultReports = currentReport.getTestSuiteResultReports();
        if( testSuiteResultReports.size() == 1 ) {
            if( testSuiteResultReports.get(0).getTestCaseResultReports().size()==1) {
                name = "TestCase " + testSuiteResultReports.get(0).getTestCaseResultReports().get(0).getTestCaseName();
            }
            else {
               name = "TestSuite " + testSuiteResultReports.get(0).getTestSuiteName();
            }
        }
        return name;
    }

    private Map<Object,Object> getLogDataForResult(TestStepResult testStepResult) {
        Map<Object,Object> result = Maps.newConcurrentMap();

        result.put( "testStep", testStepResult.getTestStepName());
        result.put( "timeTaken", testStepResult.getTimeTaken() );
        result.put( "status", testStepResult.getAssertionStatus());

        if( testStepResult.getMessages() != null && !testStepResult.getMessages().isEmpty()){
            result.put( "messsages", testStepResult.getMessages());
        }
        if( testStepResult.getHarEntry() != null ) {
            result.put("harEntry", testStepResult.getHarEntry());
        }

        return result;
    }
}
