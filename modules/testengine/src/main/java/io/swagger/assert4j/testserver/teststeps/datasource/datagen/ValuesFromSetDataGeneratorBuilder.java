package io.swagger.assert4j.testserver.teststeps.datasource.datagen;

import io.swagger.assert4j.client.model.DataGenerator;
import io.swagger.assert4j.client.model.ValuesFromSetDataGenerator;

import java.util.*;

public class ValuesFromSetDataGeneratorBuilder extends AbstractDataGeneratorBuilder<ValuesFromSetDataGeneratorBuilder> {
    private final ValuesFromSetDataGenerator valuesFromSetDataGenerator = new ValuesFromSetDataGenerator();
    private Set<String> values = new HashSet<>();

    ValuesFromSetDataGeneratorBuilder(String property) {
        super(property);
        valuesFromSetDataGenerator.setType("Value from Set");
        valuesFromSetDataGenerator.setGenerationMode(ValuesFromSetDataGenerator.GenerationModeEnum.RANDOM);
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

    ValuesFromSetDataGeneratorBuilder withSequentialValues() {
        valuesFromSetDataGenerator.setGenerationMode(ValuesFromSetDataGenerator.GenerationModeEnum.SEQUENTIAL);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        valuesFromSetDataGenerator.setValues(new ArrayList<>(values));
        return valuesFromSetDataGenerator;
    }
}
