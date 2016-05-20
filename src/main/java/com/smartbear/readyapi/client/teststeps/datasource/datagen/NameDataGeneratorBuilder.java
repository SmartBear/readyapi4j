package com.smartbear.readyapi.client.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.NameDataGenerator;

public class NameDataGeneratorBuilder extends AbstractDataGeneratorBuilder<NameDataGeneratorBuilder> {
    private final NameDataGenerator nameDataGenerator = new NameDataGenerator();

    NameDataGeneratorBuilder(String property) {
        super(property);
        nameDataGenerator.setType("Name");
        nameDataGenerator.setGender(NameDataGenerator.GenderEnum.ANY);
        nameDataGenerator.setNameType(NameDataGenerator.NameTypeEnum.FULL);
    }

    NameDataGeneratorBuilder withGenderAny() {
        nameDataGenerator.setGender(NameDataGenerator.GenderEnum.ANY);
        return this;
    }

    NameDataGeneratorBuilder withGenderMale() {
        nameDataGenerator.setGender(NameDataGenerator.GenderEnum.MALE);
        return this;
    }

    NameDataGeneratorBuilder withGenderFemale() {
        nameDataGenerator.setGender(NameDataGenerator.GenderEnum.FEMALE);
        return this;
    }

    NameDataGeneratorBuilder withFullNames() {
        nameDataGenerator.setNameType(NameDataGenerator.NameTypeEnum.FULL);
        return this;
    }

    NameDataGeneratorBuilder withFirstNames() {
        nameDataGenerator.setNameType(NameDataGenerator.NameTypeEnum.FIRSTNAME);
        return this;
    }

    NameDataGeneratorBuilder withLatsNames() {
        nameDataGenerator.setNameType(NameDataGenerator.NameTypeEnum.LASTNAME);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return nameDataGenerator;
    }
}
