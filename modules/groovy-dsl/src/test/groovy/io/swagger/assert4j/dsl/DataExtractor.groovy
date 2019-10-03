package io.swagger.assert4j.dsl

import io.swagger.assert4j.TestRecipe
import io.swagger.assert4j.client.model.TestStep
import org.junit.Assert

/**
 * Utility methods to extract data for the benefit of unit tests.
 */
class DataExtractor {
    static TestStep extractFirstTestStep(TestRecipe recipe) {
        List<TestStep> testSteps = recipe?.testCase?.testSteps
        if (!testSteps) {
            Assert.fail('No test case or test steps created')
        }
        return testSteps.first()
    }
}
