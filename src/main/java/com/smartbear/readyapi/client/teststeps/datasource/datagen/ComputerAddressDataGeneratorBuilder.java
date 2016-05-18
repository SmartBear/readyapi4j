package com.smartbear.readyapi.client.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.ComputerAddressDataGenerator;
import com.smartbear.readyapi.client.model.DataGenerator;

public class ComputerAddressDataGeneratorBuilder extends AbstractDataGeneratorBuilder<ComputerAddressDataGeneratorBuilder> {

    private final ComputerAddressDataGenerator computerAddressDataGenerator = new ComputerAddressDataGenerator();

    ComputerAddressDataGeneratorBuilder(String property) {
        super(property);
        computerAddressDataGenerator.setType("Computer Address");
        computerAddressDataGenerator.setFormat(ComputerAddressDataGenerator.FormatEnum.IPV4);
    }

    @Override
    protected DataGenerator createDataGenerator() {
        return computerAddressDataGenerator;
    }
}
