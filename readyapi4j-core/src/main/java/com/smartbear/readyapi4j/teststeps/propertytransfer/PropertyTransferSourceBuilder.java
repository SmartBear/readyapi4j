package com.smartbear.readyapi4j.teststeps.propertytransfer;

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
        if (source.getProperty() == null) {
            source.setProperty("Response");
        }

        if (source.getPath() != null && source.getPathLanguage() == null) {
            if (source.getPath().startsWith("$")) {
                source.setPathLanguage(PathLanguage.JSONPath.name());
            } else {
                source.setPathLanguage(PathLanguage.XPath.name());
            }
        }
        return source;
    }

    public boolean hasSourceStep() {
        return source.getSourceName() != null;
    }
}
