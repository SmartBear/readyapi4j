package com.smartbear.readyapi.client.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.UKPostCodeSetDataGenerator;

public class UKPostCodeDataGeneratorBuilder extends AbstractDataGeneratorBuilder<UKPostCodeDataGeneratorBuilder> {
    private final UKPostCodeSetDataGenerator ukPostCodeSetDataGenerator = new UKPostCodeSetDataGenerator();

    UKPostCodeDataGeneratorBuilder(String property) {
        super(property);
        ukPostCodeSetDataGenerator.setType("United Kingdom Postcode");
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeSetDataGenerator.CodeFormatEnum.ALL);
    }

    public UKPostCodeDataGeneratorBuilder withFormatAll() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeSetDataGenerator.CodeFormatEnum.ALL);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatA9_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeSetDataGenerator.CodeFormatEnum.A9_9AA);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatA99_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeSetDataGenerator.CodeFormatEnum.A99_9AA);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatAA9_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeSetDataGenerator.CodeFormatEnum.AA9_9AA);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatA9A_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeSetDataGenerator.CodeFormatEnum.A9A_9AA);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatAA99_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeSetDataGenerator.CodeFormatEnum.AA99_9AA);
        return this;
    }

    public UKPostCodeDataGeneratorBuilder withFormatAA9A_9AA() {
        ukPostCodeSetDataGenerator.setCodeFormat(UKPostCodeSetDataGenerator.CodeFormatEnum.AA9A_9AA);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return ukPostCodeSetDataGenerator;
    }
}
