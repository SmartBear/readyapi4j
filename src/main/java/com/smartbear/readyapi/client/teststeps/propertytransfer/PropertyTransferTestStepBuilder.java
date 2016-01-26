package com.smartbear.readyapi.client.teststeps.propertytransfer;

import com.smartbear.readyapi.client.model.PropertyTransfer;
import com.smartbear.readyapi.client.model.PropertyTransferTestStep;
import com.smartbear.readyapi.client.teststeps.TestStepBuilder;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;

import java.util.ArrayList;
import java.util.List;

public class PropertyTransferTestStepBuilder implements TestStepBuilder<PropertyTransferTestStep> {

    private PropertyTransferTestStep testStep = new PropertyTransferTestStep();
    private List<PropertyTransferBuilder> propertyTransferBuilders = new ArrayList<>();

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
            transfers.add(propertyTransferBuilder.build());
        }
        testStep.setTransfers(transfers);
        return testStep;
    }
}
