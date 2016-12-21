package com.smartbear.readyapi4j.teststeps.propertytransfer;

import com.smartbear.readyapi.client.model.PropertyTransfer;
import com.smartbear.readyapi.client.model.PropertyTransferTestStep;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi4j.teststeps.TestStepBuilder;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;

import java.util.ArrayList;
import java.util.List;

public class PropertyTransferTestStepBuilder implements TestStepBuilder<PropertyTransferTestStep> {

    private PropertyTransferTestStep testStep = new PropertyTransferTestStep();
    private List<PropertyTransferBuilder> propertyTransferBuilders = new ArrayList<>();
    private TestStep previousTestStep;

    public PropertyTransferTestStepBuilder named(String name) {
        testStep.setName(name);
        return this;
    }

    public PropertyTransferTestStepBuilder addTransfer(PropertyTransferBuilder propertyTransferBuilder) {
        this.propertyTransferBuilders.add(propertyTransferBuilder);
        return this;
    }

    @Override
    public PropertyTransferTestStep build() {
        testStep.setType(TestStepTypes.PROPERTY_TRANSFER.getName());
        List<PropertyTransfer> transfers = new ArrayList<>();
        for (PropertyTransferBuilder propertyTransferBuilder : propertyTransferBuilders) {
            propertyTransferBuilder.setPreviousTestStep(previousTestStep);
            transfers.add(propertyTransferBuilder.build());
        }
        testStep.setTransfers(transfers);
        return testStep;
    }

    @Override
    public void setPreviousTestStep(TestStep testStep) {
        this.previousTestStep = testStep;
    }
}
