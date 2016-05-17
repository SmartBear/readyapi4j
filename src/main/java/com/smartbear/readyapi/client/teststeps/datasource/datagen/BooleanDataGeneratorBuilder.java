package com.smartbear.readyapi.client.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.BooleanDataGenerator;
import com.smartbear.readyapi.client.model.DataGenerator;

import static com.smartbear.readyapi.client.teststeps.datasource.datagen.BooleanDataGeneratorBuilder.Format.TRUE_FALSE;

public class BooleanDataGeneratorBuilder extends AbstractDataGeneratorBuilder<BooleanDataGeneratorBuilder> {
    public enum Format {
        DIGITS("1/0"), TRUE_FALSE("True/False"), YES_NO("Yes/No");
        private final String formatString;

        Format(String formatString) {
            this.formatString = formatString;
        }

        public String getFormatString() {
            return formatString;
        }
    }

    private Format format = TRUE_FALSE;

    public BooleanDataGeneratorBuilder(String property) {
        super(property);
    }

    public BooleanDataGeneratorBuilder setFormat(Format format) {
        this.format = format;
        return this;
    }

    @Override
    protected DataGenerator createDataGenerator() {
        BooleanDataGenerator booleanDataGenerator = new BooleanDataGenerator();
        booleanDataGenerator.setType("Boolean");
        booleanDataGenerator.setFormat(getFormatEnum());
        return booleanDataGenerator;
    }

    private BooleanDataGenerator.FormatEnum getFormatEnum() {
        for (BooleanDataGenerator.FormatEnum formatEnum : BooleanDataGenerator.FormatEnum.values()) {
            if (formatEnum.toString().equals(format.getFormatString())) {
                return formatEnum;
            }
        }
        return BooleanDataGenerator.FormatEnum.TRUE_FALSE; //default value
    }
}
