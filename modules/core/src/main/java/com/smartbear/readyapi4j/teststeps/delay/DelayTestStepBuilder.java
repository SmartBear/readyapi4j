package com.smartbear.readyapi4j.teststeps.delay;

import com.smartbear.readyapi4j.client.model.DelayTestStep;
import com.smartbear.readyapi4j.teststeps.TestStepBuilder;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;

public class DelayTestStepBuilder implements TestStepBuilder<DelayTestStep> {
    private int delay;
    private String name;

    public DelayTestStepBuilder(int delay) {
        this.delay = delay;
    }

    public DelayTestStepBuilder named(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DelayTestStep build() {
        DelayTestStep delayTestStep = new DelayTestStep();
        delayTestStep.setType(TestStepTypes.DELAY.getName());
        delayTestStep.setName(name);
        delayTestStep.setDelay(delay);
        return delayTestStep;
    }
}
