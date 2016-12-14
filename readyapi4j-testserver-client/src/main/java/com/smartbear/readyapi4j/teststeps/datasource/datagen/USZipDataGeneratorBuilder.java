package com.smartbear.readyapi4j.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.USZIPCodeDataGenerator;

public class USZipDataGeneratorBuilder extends AbstractDataGeneratorBuilder<USZipDataGeneratorBuilder> {
    private final USZIPCodeDataGenerator usZipCodeSetDataGenerator = new USZIPCodeDataGenerator();

    USZipDataGeneratorBuilder(String property) {
        super(property);
        usZipCodeSetDataGenerator.setType("United States ZIP Code");
        usZipCodeSetDataGenerator.setCodeFormat(USZIPCodeDataGenerator.CodeFormatEnum.ALL);
    }

    public USZipDataGeneratorBuilder withFormatAll() {
        usZipCodeSetDataGenerator.setCodeFormat(USZIPCodeDataGenerator.CodeFormatEnum.ALL);
        return this;
    }

    public USZipDataGeneratorBuilder withFormatXXXXX() {
        usZipCodeSetDataGenerator.setCodeFormat(USZIPCodeDataGenerator.CodeFormatEnum.XXXXX);
        return this;
    }

    public USZipDataGeneratorBuilder withFormatXXXXX_XXXX() {
        usZipCodeSetDataGenerator.setCodeFormat(USZIPCodeDataGenerator.CodeFormatEnum.XXXXX_XXXX);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return usZipCodeSetDataGenerator;
    }
}
