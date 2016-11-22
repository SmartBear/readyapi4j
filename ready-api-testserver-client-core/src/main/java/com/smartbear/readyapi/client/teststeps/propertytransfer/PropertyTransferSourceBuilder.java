package com.smartbear.readyapi.client.teststeps.propertytransfer;

import com.smartbear.readyapi.client.model.PropertyTransferSource;

public class PropertyTransferSourceBuilder {

    private PropertyTransferSource source = new PropertyTransferSource();

    public static PropertyTransferSourceBuilder aSource() {
        return new PropertyTransferSourceBuilder();
    }

    public PropertyTransferSourceBuilder withSourceStep(String sourceStepName) {
        source.setSourceName(sourceStepName);
        return this;
    }

    public PropertyTransferSourceBuilder withProperty(String property) {
        source.setProperty(property);
        return this;
    }

    public PropertyTransferSourceBuilder withPathLanguage(PathLanguage pathLanguage) {
        source.setPathLanguage(pathLanguage.name());
        return this;
    }

    public PropertyTransferSourceBuilder withPath(String path) {
        source.setPath(path);
        return this;
    }

    PropertyTransferSource build() {
        return source;
    }
}
