package com.smartbear.readyapi4j.teststeps;

import com.smartbear.readyapi4j.client.model.TestStep;

public interface TestStepBuilder<TestStepType extends TestStep> {

    TestStepType build();

    default void setPreviousTestStep(TestStep testStep) {
    }
}
