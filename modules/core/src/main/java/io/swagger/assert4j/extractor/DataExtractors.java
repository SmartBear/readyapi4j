package io.swagger.assert4j.extractor;

import io.swagger.assert4j.client.model.TestCaseResultReport;
import io.swagger.assert4j.client.model.TestJobReport;
import io.swagger.assert4j.client.model.TestSuiteResultReport;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class to execute the data extractions after recipe execution.
 */
public class DataExtractors {
    public static void runDataExtractors(TestJobReport executionStatus, List<ExtractorData> extractorDataList) {
        List<String> extractorDataIdList = extractorDataList
                .stream()
                .map(ExtractorData::getExtractorDataId)
                .collect(Collectors.toList());
        Optional<TestCaseResultReport> resultReport = executionStatus
                .getTestSuiteResultReports()
                .stream()
                .map(TestSuiteResultReport::getTestCaseResultReports)
                .flatMap(Collection::stream)
                .filter(testCaseResultReport ->
                            testCaseResultReport.getProperties() != null &&
                            extractorDataIdList.contains(testCaseResultReport.getProperties().get(ExtractorData.EXTRACTOR_DATA_KEY)))
                .findAny();

        resultReport.ifPresent(testCaseResultReport -> {
            Map<String, String> properties = testCaseResultReport.getProperties();
            runExtractorFunctions(extractorDataList, properties);

            // After run, remove all unnecessary properties
            properties.entrySet().removeIf(entry -> entry.getKey().contains(properties.get(ExtractorData.EXTRACTOR_DATA_KEY)));
            properties.remove(ExtractorData.EXTRACTOR_DATA_KEY);
        });
    }


    private static void runExtractorFunctions(List<ExtractorData> extractorDataList, Map<String, String> properties) {
        Optional<ExtractorData> extractorData = extractorDataList
                .stream()
                .filter(ed -> ed.getExtractorDataId().equals(properties.get(ExtractorData.EXTRACTOR_DATA_KEY)))
                .findAny();
        extractorData.ifPresent(extractorData1 -> properties.forEach((key, value) -> {
            ExtractorOperator operator = extractorData1.getExtractorOperator(key);
            if (operator != null) {
                operator.extractValue(value);
            }
        }));
    }
}
