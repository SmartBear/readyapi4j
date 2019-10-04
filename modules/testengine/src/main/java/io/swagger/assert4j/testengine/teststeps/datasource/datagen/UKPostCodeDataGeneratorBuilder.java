package io.swagger.assert4j.testengine.teststeps.datasource.datagen;

import io.swagger.assert4j.client.model.DataGenerator;
import io.swagger.assert4j.client.model.UKPostCodeDataGenerator;

public class UKPostCodeDataGeneratorBuilder extends AbstractDataGeneratorBuilder<UKPostCodeDataGeneratorBuilder> {
    private final UKPostCodeDataGenerator ukPostCodeSetDataGenerator = new UKPostCodeDataGenerator();

    UKPostCodeDataGeneratorBuilder(String property) {
        super(property);
        ukPostCodeSetDataGenerator.setType("United Kingdom Postcode");
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeDataGenerator.CodeFormatEnum.ALL);
    }

    public UKPostCodeDataGeneratorBuilder withFormatAll() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeDataGenerator.CodeFormatEnum.ALL);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatA9_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeDataGenerator.CodeFormatEnum.A9_9AA);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatA99_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeDataGenerator.CodeFormatEnum.A99_9AA);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatAA9_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeDataGenerator.CodeFormatEnum.AA9_9AA);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatA9A_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeDataGenerator.CodeFormatEnum.A9A_9AA);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatAA99_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeDataGenerator.CodeFormatEnum.AA99_9AA);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatAA9A_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeDataGenerator.CodeFormatEnum.AA9A_9AA);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder setFormat(String format) {
        for (UKPostCodeDataGenerator.CodeFormatEnum formatEnum : UKPostCodeDataGenerator.CodeFormatEnum.values()) {
            if (formatEnum.toString().equals(format)) {
                ukPostCodeSetDataGenerator.setCodeFormat(formatEnum);
                return this;
            }
        }
        throw new IllegalArgumentException(String.format("Unsupported post code format: %s. Allowed values:[%s]",
                format, "'ALL', 'A9 9AA', 'A99 9AA', 'AA9 9AA', 'A9A 9AA', 'AA99 9AA', 'AA9A 9AA'"));
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return ukPostCodeSetDataGenerator;
    }
}
