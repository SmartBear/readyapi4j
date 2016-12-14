package com.smartbear.readyapi4j.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.UKPostCodeDataGenerator;

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

    @Override
    protected DataGenerator buildDataGenerator() {
        return ukPostCodeSetDataGenerator;
    }
}
