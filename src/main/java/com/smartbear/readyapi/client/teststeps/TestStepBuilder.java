package com.smartbear.readyapi.client.teststeps;

import io.swagger.client.model.TestStep;

public interface TestStepBuilder<TestStepType extends TestStep> {

    TestStepType build();
}
