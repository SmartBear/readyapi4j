package com.smartbear.readyapi.client.teststeps;

import com.smartbear.readyapi.client.model.TestStep;

public interface TestStepBuilder<TestStepType extends TestStep> {

    TestStepType build();
}
