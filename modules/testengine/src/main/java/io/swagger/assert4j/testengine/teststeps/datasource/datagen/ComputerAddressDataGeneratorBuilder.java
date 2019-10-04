package io.swagger.assert4j.testengine.teststeps.datasource.datagen;

import io.swagger.assert4j.client.model.ComputerAddressDataGenerator;
import io.swagger.assert4j.client.model.DataGenerator;

public class ComputerAddressDataGeneratorBuilder extends AbstractDataGeneratorBuilder<ComputerAddressDataGeneratorBuilder> {

    private final ComputerAddressDataGenerator computerAddressDataGenerator = new ComputerAddressDataGenerator();

    ComputerAddressDataGeneratorBuilder(String property) {
        super(property);
        computerAddressDataGenerator.setType("Computer Address");
        computerAddressDataGenerator.setAddressType(ComputerAddressDataGenerator.AddressTypeEnum.IPV4);
    }

    ComputerAddressDataGeneratorBuilder withMac48Format() {
        computerAddressDataGenerator.setAddressType(ComputerAddressDataGenerator.AddressTypeEnum.MAC48);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return computerAddressDataGenerator;
    }
}
