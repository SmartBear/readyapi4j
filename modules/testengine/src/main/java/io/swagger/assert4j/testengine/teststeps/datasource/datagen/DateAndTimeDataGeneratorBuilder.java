package io.swagger.assert4j.testengine.teststeps.datasource.datagen;

import io.swagger.assert4j.client.model.DataGenerator;
import io.swagger.assert4j.client.model.DateAndTimeDataGenerator;
import io.swagger.assert4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum;

import java.util.Date;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class DateAndTimeDataGeneratorBuilder extends AbstractDataGeneratorBuilder<DateAndTimeDataGeneratorBuilder> {
    private static final int ONE_MINUTE_MILLISECONDS = 60000;
    private static final int ONE_HOUR_MILLISECONDS = 60 * ONE_MINUTE_MILLISECONDS;
    private final long ONE_DAY_MILLISECONDS = 24 * ONE_HOUR_MILLISECONDS;
    private final DateAndTimeDataGenerator dateAndTimeDataGenerator = new DateAndTimeDataGenerator();

    DateAndTimeDataGeneratorBuilder(String property) {
        super(property);
        dateAndTimeDataGenerator.setType("Date and Time");
        dateAndTimeDataGenerator.setDateTimeFormat(DateTimeFormatEnum.HH_MM_AM_PM);
        dateAndTimeDataGenerator.setGenerationMode(DateAndTimeDataGenerator.GenerationModeEnum.RANDOM);
        dateAndTimeDataGenerator.setIncrementValueDay(1);
        dateAndTimeDataGenerator.setIncrementValueHour(0);
        dateAndTimeDataGenerator.setIncrementValueMinute(0);
        dateAndTimeDataGenerator.setIncrementValueSecond(0);
        try {
//            dateAndTimeDataGenerator.setMinimumValue(new ISO8601DateFormat().parse("1984-02-12T17:26:20Z"));
//            dateAndTimeDataGenerator.setMaximumValue(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DateAndTimeDataGeneratorBuilder withSequentialValues() {
        dateAndTimeDataGenerator.setGenerationMode(DateAndTimeDataGenerator.GenerationModeEnum.SEQUENTIAL);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder withFormat(String format) {
        for (DateTimeFormatEnum dateTimeFormat : DateTimeFormatEnum.values()) {
            if (dateTimeFormat.toString().equals(format)) {
                dateAndTimeDataGenerator.setDateTimeFormat(dateTimeFormat);
                return this;
            }
        }
        throw new IllegalArgumentException(String.format("Unsupported date format: %s. Allowed values:[%s]", format,
                "'HH:MM AM/PM','HH:MM (24-hour)','HH:MM:SS AM/PM','HH:MM:SS (24-hour)','M/D/YYYY HH:MM:SS AM/PM',"
                        + "'M/D/YYYY HH:MM:SS (24-hour)','M/D/YYYY','D Month YYYY','DayOfWeek D Month YYYY'," +
                        "'YYYY-MM-DDTHH:mm:ssZ (ISO-8601)'"));
    }

    public DateAndTimeDataGeneratorBuilder startingAt(Date startDate) {
        // dateAndTimeDataGenerator.setMinimumValue(  startDate);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder endingAt(Date endDate) {
        //  dateAndTimeDataGenerator.setMaximumValue(endDate);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder withFormatHH_MM_AM_PM() {
        dateAndTimeDataGenerator.setDateTimeFormat(DateTimeFormatEnum.HH_MM_AM_PM);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder withFormatHH_MM_24_HOUR() {
        dateAndTimeDataGenerator.setDateTimeFormat(DateTimeFormatEnum.HH_MM_24_HOUR_);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder withFormatHH_MM_SS_AM_PM() {
        dateAndTimeDataGenerator.setDateTimeFormat(DateTimeFormatEnum.HH_MM_SS_AM_PM);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder withFormatHH_MM_SS_24_HOUR() {
        dateAndTimeDataGenerator.setDateTimeFormat(DateTimeFormatEnum.HH_MM_SS_24_HOUR_);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder withFormatM_D_YYYY_HH_MM_SS_AM_PM() {
        dateAndTimeDataGenerator.setDateTimeFormat(DateTimeFormatEnum.M_D_YYYY_HH_MM_SS_AM_PM);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder withFormatM_D_YYYY_HH_MM_SS_24_HOUR() {
        dateAndTimeDataGenerator.setDateTimeFormat(DateTimeFormatEnum.M_D_YYYY_HH_MM_SS_24_HOUR_);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder withFormatM_D_YYYY() {
        dateAndTimeDataGenerator.setDateTimeFormat(DateTimeFormatEnum.M_D_YYYY);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder withFormatD_MONTH_YYYY() {
        dateAndTimeDataGenerator.setDateTimeFormat(DateTimeFormatEnum.D_MONTH_YYYY);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder withFormatDAY_OF_WEEK_D_MONTH_YYYY() {
        dateAndTimeDataGenerator.setDateTimeFormat(DateTimeFormatEnum.DAYOFWEEK_D_MONTH_YYYY);
        return this;
    }

    public DateAndTimeDataGeneratorBuilder withFormatISO_8601() {
        dateAndTimeDataGenerator.setDateTimeFormat(DateTimeFormatEnum.YYYY_MM_DDTHH_MM_SSZ_ISO_8601_);
        return this;
    }

    /**
     * Used for generating sequential values, ignored if generation mode is Random. Default value: 1 day (24 * 60 * 60 * 1000 milliseconds)
     *
     * @param milliSeconds increment value in milliseconds
     * @return DateAndTimeDataGeneratorBuilder
     */
    public DateAndTimeDataGeneratorBuilder incrementBy(long milliSeconds) {
        if (milliSeconds < 1000) {
            throw new IllegalArgumentException("Increment value should be greater than or equal to 1000 (1 second), actual: " + milliSeconds);
        }
        int dayIncrement = (int) MILLISECONDS.toDays(milliSeconds);
        int hourIncrement = (int) MILLISECONDS.toHours(milliSeconds - dayIncrement * ONE_DAY_MILLISECONDS);

        int minuteIncrement = (int) MILLISECONDS.toMinutes(milliSeconds - (dayIncrement * ONE_DAY_MILLISECONDS
                + hourIncrement * ONE_HOUR_MILLISECONDS));

        int secondIncrement = (int) MILLISECONDS.toSeconds(milliSeconds - (dayIncrement * ONE_DAY_MILLISECONDS +
                hourIncrement * ONE_HOUR_MILLISECONDS + minuteIncrement * ONE_MINUTE_MILLISECONDS));

        dateAndTimeDataGenerator.setIncrementValueDay(dayIncrement);
        dateAndTimeDataGenerator.setIncrementValueHour(hourIncrement);
        dateAndTimeDataGenerator.setIncrementValueMinute(minuteIncrement);
        dateAndTimeDataGenerator.setIncrementValueSecond(secondIncrement);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return dateAndTimeDataGenerator;
    }
}
