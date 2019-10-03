package io.swagger.assert4j.extractor;

import io.swagger.assert4j.teststeps.propertytransfer.PathLanguage;

public class Extractors {

    /**
     * Extracts a property from the teststep it was added on with the function "withProperties". The property must exist
     * on the given teststep. Runs the lambda operation given in the ExtractorOperation field after the testserver run
     *
     * @param property The property to be extracted
     * @param operator The lambda function to be run on the extracted property
     * @return The extractor that is added to the testserver run
     */
    public static Extractor fromProperty(String property, ExtractorOperator operator) {
        return new Extractor(property, operator);
    }

    /**
     * Extracts a property from the response of the teststep it was added on with the function "withProperties",
     * extracted by the path that was given. Observer that the response fo the given teststep is often given in an array,
     * in that case the path should give which element it wants to extract values from. For example: The first test name
     * value of a given response: "$[0].testName", Runs the lambda operation given in the ExtractorOperation field after
     * the testserver run
     *
     * @param path     The path for extracting the property in the response of the given teststep, can be both JsonPath or XPath
     * @param operator The lambda function to be run on the extracted property
     * @return The extractor that is added to the testserver run
     */
    public static Extractor fromResponse(String path, ExtractorOperator operator) {
        Extractor extractor;
        if (path.startsWith("$")) {
            extractor = new Extractor("Response", path, operator);
            extractor.setPathLanguage(PathLanguage.JSONPath);
            return extractor;
        } else {
            extractor = new Extractor("ResponseAsXml", path, operator);
            extractor.setPathLanguage(PathLanguage.XPath);
        }
        return extractor;
    }
}
