/*
 * Copyright 2004-2015 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.assert4j.junitreport;

import com.smartbear.readyapi.junit.Properties;
import com.smartbear.readyapi.junit.*;
import io.swagger.assert4j.client.model.TestCaseResultReport;
import io.swagger.assert4j.client.model.TestJobReport;
import io.swagger.assert4j.client.model.TestStepResultReport;
import io.swagger.assert4j.client.model.TestSuiteResultReport;
import org.apache.xmlbeans.XmlOptions;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static io.swagger.assert4j.client.model.TestStepResultReport.AssertionStatusEnum.FAILED;

/**
 * Wrapper for a number of Test runs
 */

public class JUnitReport {
    private final Map properties;
    private TestsuiteDocument testsuiteDoc;
    private int noofTestCases;
    private int noofFailures;
    private int noofErrors;
    private double totalTime;
    private StringBuilder systemOut;
    private StringBuilder systemErr;

    private boolean includeTestProperties;
    private final ErrorLog errorLog;

    public JUnitReport(Map properties) {
        this(properties, new SystemErrErrorLog());
    }

    public JUnitReport(Map properties, ErrorLog errorLog) {
        this.properties = properties;
        this.errorLog = errorLog;
        systemOut = new StringBuilder();
        systemErr = new StringBuilder();

        testsuiteDoc = TestsuiteDocument.Factory.newInstance();
        Testsuite testsuite = testsuiteDoc.addNewTestsuite();
        setSystemProperties(testsuite.addNewProperties());
    }

    public void setIncludeTestProperties(boolean includeTestProperties) {
        this.includeTestProperties = includeTestProperties;
    }

    public void setTotalTime(double time) {
        testsuiteDoc.getTestsuite().setTime(Double.toString(Math.round(time * 1000) / 1000d));
    }

    public void setTestSuiteName(String name) {
        testsuiteDoc.getTestsuite().setName(name);
    }

    public void setPackage(String pkg) {
        testsuiteDoc.getTestsuite().setPackage(pkg);
    }

    public void setNoofErrorsInTestSuite(int errors) {
        testsuiteDoc.getTestsuite().setErrors(errors);
    }

    public void setNoofFailuresInTestSuite(int failures) {
        testsuiteDoc.getTestsuite().setFailures(failures);
    }

    public void systemOut(String systemout) {
        systemOut.append(systemout);
    }

    public void systemErr(String systemerr) {
        systemErr.append(systemerr);
    }

    public void setSystemOut(String systemout) {
        testsuiteDoc.getTestsuite().setSystemOut(systemout);
    }

    public void setSystemErr(String systemerr) {
        testsuiteDoc.getTestsuite().setSystemErr(systemerr);
    }

    public Testcase addTestCase(String name, double time, Map<String, String> testProperties) {
        Testcase testcase = testsuiteDoc.getTestsuite().addNewTestcase();
        testcase.setName(name);
        testcase.setTime(String.valueOf(time / 1000));
        noofTestCases++;
        totalTime += time;

        setTestProperties(testProperties, testcase);

        return testcase;
    }

    private void setTestProperties(Map<String, String> testProperties, Testcase testcase) {
        if (!this.includeTestProperties) {
            return;
        }

        Properties properties = testcase.addNewProperties();
        setProperties(properties, testProperties);
    }

    public Testcase addTestCaseWithFailure(String name, double time, String failure, String stacktrace, Map<String, String> testProperties) {
        Testcase testcase = testsuiteDoc.getTestsuite().addNewTestcase();
        testcase.setName(name);
        testcase.setTime(String.valueOf(time / 1000));
        FailureDocument.Failure fail = testcase.addNewFailure();
        fail.setType(failure);
        fail.setMessage(failure);
        fail.setStringValue(stacktrace);
        noofTestCases++;
        noofFailures++;
        totalTime += time;

        setTestProperties(testProperties, testcase);

        return testcase;
    }

    public Testcase addTestCaseWithError(String name, double time, String error, String stacktrace, Map<String, String> testProperties) {
        Testcase testcase = testsuiteDoc.getTestsuite().addNewTestcase();
        testcase.setName(name);
        testcase.setTime(String.valueOf(time / 1000));
        ErrorDocument.Error err = testcase.addNewError();
        err.setType(error);
        err.setMessage(error);
        err.setStringValue(stacktrace);
        noofTestCases++;
        noofErrors++;
        totalTime += time;

        setTestProperties(testProperties, testcase);

        return testcase;
    }

    private void setSystemProperties(Properties properties) {
        Set<?> keys = System.getProperties().keySet();
        for (Object keyO : keys) {
            String key = keyO.toString();
            String value = System.getProperty(key);
            Property prop = properties.addNewProperty();
            prop.setName(key);
            prop.setValue(value);
        }
    }

    private void setProperties(Properties properties, Map<String, String> propertiesToSet) {
        for (Map.Entry<String, String> stringStringEntry : propertiesToSet.entrySet()) {
            Property prop = properties.addNewProperty();
            prop.setName(stringStringEntry.getKey());
            prop.setValue(stringStringEntry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    public void save(File file) throws IOException {
        finishReport();

        @SuppressWarnings("rawtypes")
        Map prefixes = new HashMap();
        prefixes.put("", "http://smartbear.com/readyapi/junit");

        testsuiteDoc.save(file, new XmlOptions().setSaveOuter().setCharacterEncoding("utf-8").setUseDefaultNamespace()
                .setSaveImplicitNamespaces(prefixes));
    }

    public TestsuiteDocument finishReport() {
        testsuiteDoc.getTestsuite().setTests(noofTestCases);
        testsuiteDoc.getTestsuite().setFailures(noofFailures);
        testsuiteDoc.getTestsuite().setErrors(noofErrors);
        testsuiteDoc.getTestsuite().setTime(String.valueOf(totalTime / 1000));

        return testsuiteDoc;
    }

    public void handleResponse(TestJobReport result, String recipeFileName) throws TestFailureException {

        if (result.getStatus() == TestJobReport.StatusEnum.FAILED) {

            String message = logErrorsToConsole(result);
            addTestCaseWithFailure(recipeFileName, result.getTotalTime(),
                    message, "<missing stacktrace>", new HashMap<String, String>(properties));

            throw new TestFailureException("Recipe failed, recipe file: " + recipeFileName);
        } else {
            addTestCase(recipeFileName, result.getTotalTime(), new HashMap<String, String>(properties));
        }
    }

    private String logErrorsToConsole(TestJobReport result) {

        List<String> messages = new ArrayList<>();

        result.getTestSuiteResultReports().stream()
                .map(TestSuiteResultReport::getTestCaseResultReports) // creates List<List<TestCaseResultReport>>
                .flatMap(Collection::stream) //converts List<List<TestCaseResultReport>> to List<TestCaseResultReport>
                .map(TestCaseResultReport::getTestStepResultReports) // List<List<TestStepResultReport>>
                .flatMap(Collection::stream) // flattens List<List<TestStepResultReport>> to List<TestStepResultReport>
                .filter(testStepResult -> testStepResult.getAssertionStatus() == FAILED) //keep only failed tests
                .map(TestStepResultReport::getMessages) // creates List<List<String>>
                .flatMap(Collection::stream) // flattens List<List<String>> to List<String>
                .forEach(message -> { //process each message from List<String>
                    messages.add(message);
                    errorLog.logError("- " + message);
                });
        return Arrays.toString(messages.toArray());
    }

    public interface ErrorLog {

        void logError(String message);
    }

    private static class SystemErrErrorLog implements ErrorLog {
        @Override
        public void logError(String message) {
            System.err.println(message);
        }
    }
}
