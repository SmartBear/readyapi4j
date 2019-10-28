package com.smartbear.readyapi4j.teststeps.propertytransfer;

import com.smartbear.readyapi4j.client.model.PropertyTransferTarget;

public class PropertyTransferTargetBuilder {
    private PropertyTransferTarget target = new PropertyTransferTarget();

    public static PropertyTransferTargetBuilder aTarget() {
        return new PropertyTransferTargetBuilder();
    }

    public PropertyTransferTargetBuilder withTargetStep(String targetStepName) {
        target.setTargetName(targetStepName);
        return this;
    }

    public PropertyTransferTargetBuilder withProperty(String property) {
        target.setProperty(property);
        return this;
    }

    public PropertyTransferTargetBuilder withPathLanguage(PathLanguage pathLanguage) {
        target.setPathLanguage(pathLanguage.name());
        return this;
    }

    public PropertyTransferTargetBuilder withPath(String path) {
        target.setPath(path);
        return this;
    }

    PropertyTransferTarget build() {
        return target;
    }
}
