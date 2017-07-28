package io.swagger.assert4j.teststeps.delay;

import io.swagger.assert4j.client.model.DelayTestStep;
import io.swagger.assert4j.teststeps.TestStepBuilder;
import io.swagger.assert4j.teststeps.TestStepTypes;

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
