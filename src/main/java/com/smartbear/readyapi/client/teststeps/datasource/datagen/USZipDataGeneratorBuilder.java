package com.smartbear.readyapi.client.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.USZIPCodeSetDataGenerator;

public class USZipDataGeneratorBuilder extends AbstractDataGeneratorBuilder<USZipDataGeneratorBuilder> {
    private final USZIPCodeSetDataGenerator usZipCodeSetDataGenerator = new USZIPCodeSetDataGenerator();

    USZipDataGeneratorBuilder(String property) {
        super(property);
        usZipCodeSetDataGenerator.setType("United States ZIP Code");
        usZipCodeSetDataGenerator.setCodeFormat(USZIPCodeSetDataGenerator.CodeFormatEnum.ALL);
    }

    public USZipDataGeneratorBuilder withFormatAll() {
        usZipCodeSetDataGenerator.setCodeFormat(USZIPCodeSetDataGenerator.CodeFormatEnum.ALL);
        return this;
    }

    public USZipDataGeneratorBuilder withFormatXXXXX() {
        usZipCodeSetDataGenerator.setCodeFormat(USZIPCodeSetDataGenerator.CodeFormatEnum.XXXXX);
        return this;
    }

    public USZipDataGeneratorBuilder withFormatXXXXX_XXXX() {
        usZipCodeSetDataGenerator.setCodeFormat(USZIPCodeSetDataGenerator.CodeFormatEnum.XXXXX_XXXX);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return usZipCodeSetDataGenerator;
    }
}
