package com.smartbear.readyapi.client.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.ValuesFromSetDataGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ValuesFromSetDataGeneratorBuilder extends AbstractDataGeneratorBuilder<ValuesFromSetDataGeneratorBuilder> {
    private final ValuesFromSetDataGenerator valuesFromSetDataGenerator = new ValuesFromSetDataGenerator();
    private Set<String> values = new HashSet<>();

    ValuesFromSetDataGeneratorBuilder(String property) {
        super(property);
        valuesFromSetDataGenerator.setType("Value from Set");
        valuesFromSetDataGenerator.setGenrationMode(ValuesFromSetDataGenerator.GenrationModeEnum.RANDOM);
    }

    public ValuesFromSetDataGeneratorBuilder withValues(Collection<String> values) {
        this.values = new HashSet<>(values);
        return this;
    }

    public ValuesFromSetDataGeneratorBuilder withValues(String... values) {
        this.values = new HashSet<>(Arrays.asList(values));
        return this;
    }

    public ValuesFromSetDataGeneratorBuilder addValue(String value) {
        this.values.add(value);
        return this;
    }

    ValuesFromSetDataGeneratorBuilder withRandomValues() {
        valuesFromSetDataGenerator.setGenrationMode(ValuesFromSetDataGenerator.GenrationModeEnum.RANDOM);
        return this;
    }

    ValuesFromSetDataGeneratorBuilder withSequentialValues() {
        valuesFromSetDataGenerator.setGenrationMode(ValuesFromSetDataGenerator.GenrationModeEnum.SEQUENTIAL);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        valuesFromSetDataGenerator.setValues(new ArrayList<>(values));
        return valuesFromSetDataGenerator;
    }
}
