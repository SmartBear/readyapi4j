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

package io.swagger.assert4j.maven;

import com.smartbear.readyapi.junit.ErrorDocument;
import com.smartbear.readyapi.junit.FailureDocument;
import com.smartbear.readyapi.junit.Properties;
import com.smartbear.readyapi.junit.Property;
import com.smartbear.readyapi.junit.Testcase;
import com.smartbear.readyapi.junit.Testsuite;
import com.smartbear.readyapi.junit.TestsuiteDocument;
import org.apache.xmlbeans.XmlOptions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper for a number of Test runs
 */

public class JUnitReport {
    private TestsuiteDocument testsuiteDoc;
    private int noofTestCases;
    private int noofFailures;
    private int noofErrors;
    private double totalTime;
    private StringBuilder systemOut;
    private StringBuilder systemErr;

    private boolean includeTestProperties;

    public JUnitReport() {
        systemOut = new StringBuilder();
        systemErr = new StringBuilder();

        testsuiteDoc = TestsuiteDocument.Factory.newInstance();
        Testsuite testsuite = testsuiteDoc.addNewTestsuite();
        Properties properties = testsuite.addNewProperties();
        setSystemProperties(properties);
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
}
