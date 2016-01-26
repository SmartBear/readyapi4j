package com.smartbear.readyapi.client.execution;

import com.google.common.collect.Lists;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestCaseResultReport;
import com.smartbear.readyapi.client.model.TestStepResultReport;
import com.smartbear.readyapi.client.model.TestSuiteResultReport;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Represents an execution, synchronous or asynchronous, and encapsulates all available information about the execution.
 */
public class Execution {
    private final Deque<ProjectResultReport> executionStatusReports = new ConcurrentLinkedDeque<>();
    private final String id;

    public Execution(ProjectResultReport projectResultReport) {
        executionStatusReports.add(projectResultReport);
        this.id = projectResultReport.getExecutionID();
    }

    public String getId() {
        return id;
    }

    public ProjectResultReport.StatusEnum getCurrentStatus() {
        return executionStatusReports.getLast().getStatus();
    }

    public ProjectResultReport getCurrentReport() {
        return executionStatusReports.getLast();
    }

    void addResultReport(ProjectResultReport newReport) {
        executionStatusReports.add(newReport);
    }

    public List<String> getErrorMessages(){
        List<String> result = Lists.newArrayList();

        if( executionStatusReports.getLast() != null ){
            for( TestSuiteResultReport testSuiteReport : executionStatusReports.getLast().getTestSuiteResultReports()){
                for(TestCaseResultReport testCaseResultReport : testSuiteReport.getTestCaseResultReports()){
                    for(TestStepResultReport testStepResultReport : testCaseResultReport.getTestStepResultReports()){
                        if( testStepResultReport.getAssertionStatus() == TestStepResultReport.AssertionStatusEnum.FAILED){
                            result.addAll( testStepResultReport.getMessages());
                        }
                    }
                }
            }
        }


        return result;
    }
}
