package com.smartbear.readyapi4j.extractor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Holds the data for all extractors included in one TestRecipe
 */
public class ExtractorData {
    /**
     * Key used to identify data id property in testcaseresult
     */
    public static final String EXTRACTOR_DATA_KEY = "ExtractorDataKey";

    /**
     * Generated  UniqueExtractorDataId that is appended to the ExtractorId values
     */
    private String extractorDataId;

    /**
     * Map of ExtractorIds to ExtractorOperators, an extractorId consists of the name of the extracted variable
     * and the ExtractorDataId
     */
    private Map<String, ExtractorOperator> extractorOperatorMap;

    public ExtractorData() {
        extractorOperatorMap = new HashMap<>();
        extractorDataId = UUID.randomUUID().toString();
    }

    public String getExtractorDataId() {
        return extractorDataId;
    }

    /**
     * Adds an extractor operator lambda function to this ExtractorData structure. Throws IllegalArgumentException if any
     * of the arguments are null. Returns the new ExtractorId for the Extractor Property and operator added to this Data Structure.
     * It consist of the ExtractorValue and the Extractor Data Structure Id.
     *
     * @param extractorProperty The name of the extractor Property that is extracted from the test step
     * @param operator          The lambda that is going to be run on the extracted value when the test recipe result comes in
     * @return Combination of the ExtractorValue sent into this method and the id of this data structure to make the key unique
     */
    public String addExtractorOperator(String extractorProperty, ExtractorOperator operator) {
        if (extractorProperty == null || operator == null) {
            throw new IllegalArgumentException("ExtractorValueName and the ExtractorOperator need to be set to add an operator");
        }

        String extractorId = extractorProperty + extractorDataId;
        extractorOperatorMap.put(extractorId, operator);
        return extractorId;
    }

    public ExtractorOperator getExtractorOperator(String extractorId) {
        return extractorOperatorMap.get(extractorId);
    }
}