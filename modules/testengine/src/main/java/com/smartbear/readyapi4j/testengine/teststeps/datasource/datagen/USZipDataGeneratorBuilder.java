package com.smartbear.readyapi4j.testengine.teststeps.datasource.datagen;

import com.smartbear.readyapi4j.client.model.DataGenerator;
import com.smartbear.readyapi4j.client.model.USZIPCodeDataGenerator;

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

    public USZipDataGeneratorBuilder setFormat(String format) {
        for (USZIPCodeDataGenerator.CodeFormatEnum formatEnum : USZIPCodeDataGenerator.CodeFormatEnum.values()) {
            if (formatEnum.toString().equals(format)) {
                usZipCodeSetDataGenerator.setCodeFormat(formatEnum);
                return this;
            }
        }
        throw new IllegalArgumentException(String.format("Unsupported ZIP code format: %s. Allowed values:[%s]",
                format, "'ALL', 'XXXXX', 'XXXXX-XXXX'"));
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return usZipCodeSetDataGenerator;
    }
}
