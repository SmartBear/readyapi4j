package io.swagger.assert4j.testengine.teststeps.datasource.datagen;

import io.swagger.assert4j.client.model.DataGenerator;
import io.swagger.assert4j.client.model.PhoneNumberDataGenerator;

import java.util.Arrays;
import java.util.List;

public class PhoneNumberDataGeneratorBuilder extends AbstractDataGeneratorBuilder<PhoneNumberDataGeneratorBuilder> {
    private final PhoneNumberDataGenerator phoneNumberDataGenerator = new PhoneNumberDataGenerator();

    private final String numberFormatString = "XXX-XXX-XXXX,+1 XXX-XXX-XXXX,+1 (XXX)-XXX-XXXX,+X XXX-XXX-XXXX,+X (XXX)-XXX-XXXX";
    private final List<String> allowedFormatsList = Arrays.asList(numberFormatString.split(","));

    PhoneNumberDataGeneratorBuilder(String property) {
        super(property);
        phoneNumberDataGenerator.setType("Phone Number");
        phoneNumberDataGenerator.setNumberFormat("XXX-XXX-XXXX");
    }

    public PhoneNumberDataGeneratorBuilder withNumberFormat(String numberFormat) {
        if (!allowedFormatsList.contains(numberFormat)) {
            throw new IllegalArgumentException(String.format("Unsupported number format: %s. Allowed values:[%s]", numberFormat, numberFormatString));
        }
        phoneNumberDataGenerator.setNumberFormat(numberFormat);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return phoneNumberDataGenerator;
    }
}
