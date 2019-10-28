package com.smartbear.readyapi4j.teststeps.propertytransfer;

import com.smartbear.readyapi4j.client.model.PropertyTransfer;
import com.smartbear.readyapi4j.client.model.TestStep;

public class PropertyTransferBuilder {
    private PropertyTransfer transfer = new PropertyTransfer();
    private PropertyTransferSourceBuilder propertyTransferSourceBuilder;
    private PropertyTransferTargetBuilder propertyTransferTargetBuilder;
    private TestStep previousTestStep;

    public static PropertyTransferBuilder from(PropertyTransferSourceBuilder propertyTransferSourceBuilder) {
        return new PropertyTransferBuilder().withSource(propertyTransferSourceBuilder);
    }

    public static PropertyTransferBuilder fromPreviousResponse(String path) {
        return new PropertyTransferBuilder().withSource(new PropertyTransferSourceBuilder()
                .withPathLanguage(languageForPath(path))
                .withPath(path));
    }

    public static PropertyTransferBuilder fromResponse(String testStepName, String path) {
        return new PropertyTransferBuilder().withSource(new PropertyTransferSourceBuilder()
                .withPathLanguage(languageForPath(path))
                .withSourceStep(testStepName)
                .withPath(path));
    }

    private static PathLanguage languageForPath(String path) {
        return path.startsWith("$") ? PathLanguage.JSONPath : PathLanguage.XPath;
    }

    public static PropertyTransferBuilder newTransfer() {
        return new PropertyTransferBuilder();
    }

    public PropertyTransferBuilder withSource(PropertyTransferSourceBuilder propertyTransferSourceBuilder) {
        this.propertyTransferSourceBuilder = propertyTransferSourceBuilder;
        return this;
    }

    public PropertyTransferBuilder withTarget(PropertyTransferTargetBuilder propertyTransferTargetBuilder) {
        return to(propertyTransferTargetBuilder);
    }

    public PropertyTransferBuilder named(String name) {
        transfer.setTransferName(name);
        return this;
    }

    public PropertyTransferBuilder to(PropertyTransferTargetBuilder propertyTransferTargetBuilder) {
        this.propertyTransferTargetBuilder = propertyTransferTargetBuilder;
        return this;
    }

    public PropertyTransferBuilder toNextRequest(String path) {
        this.propertyTransferTargetBuilder = new PropertyTransferTargetBuilder()
                .withProperty("Request").withPathLanguage(languageForPath(path))
                .withPath(path);
        return this;
    }

    public PropertyTransferBuilder toNextRequestProperty(String property) {
        this.propertyTransferTargetBuilder = new PropertyTransferTargetBuilder()
                .withProperty(property);
        return this;
    }

    void setPreviousTestStep(TestStep lastTestStep) {
        previousTestStep = lastTestStep;
    }

    PropertyTransfer build() {
        if (propertyTransferSourceBuilder == null) {
            throw new IllegalStateException("No source defined for property transfer");
        }
        if (propertyTransferTargetBuilder == null) {
            throw new IllegalStateException("No target defined for property transfer");
        }
        if (!propertyTransferSourceBuilder.hasSourceStep() && previousTestStep != null) {
            propertyTransferSourceBuilder.withSourceStep(previousTestStep.getName());
        }
        transfer.setSource(propertyTransferSourceBuilder.build());
        transfer.setTarget(propertyTransferTargetBuilder.build());
        return transfer;
    }

    public PropertyTransferBuilder toRequestStep(String testStepName, String path) {
        toNextRequest(path);
        propertyTransferTargetBuilder.withTargetStep(testStepName);
        return this;
    }
}
