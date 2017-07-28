package io.swagger.assert4j.teststeps;

import io.swagger.assert4j.client.model.TestStep;

public interface TestStepBuilder<TestStepType extends TestStep> {

    TestStepType build();

    default void setPreviousTestStep(TestStep testStep) { }
}
