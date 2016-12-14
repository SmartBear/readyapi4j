package com.smartbear.readyapi4j.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.StateNameDataGenerator;

public class StateNameDataGeneratorBuilder extends AbstractDataGeneratorBuilder<StateNameDataGeneratorBuilder> {
    private final StateNameDataGenerator stateNameDataGenerator = new StateNameDataGenerator();

    StateNameDataGeneratorBuilder(String property) {
        super(property);
        stateNameDataGenerator.setType("State");
        stateNameDataGenerator.setNameFormat(StateNameDataGenerator.NameFormatEnum.FULL);
    }

    StateNameDataGeneratorBuilder withAbbreviatedNames() {
        stateNameDataGenerator.setNameFormat(StateNameDataGenerator.NameFormatEnum.ABBREVIATED);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return stateNameDataGenerator;
    }
}
