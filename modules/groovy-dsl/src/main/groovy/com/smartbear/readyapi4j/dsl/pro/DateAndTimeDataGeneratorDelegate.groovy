package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.testengine.teststeps.datasource.datagen.DateAndTimeDataGeneratorBuilder


/**
 * Delegate for the 'dateAndTimeValue' closure in 'recipe'
 */
class DateAndTimeDataGeneratorDelegate {
    private DateAndTimeDataGeneratorBuilder dateAndTimeDataGeneratorBuilder;

    DateAndTimeDataGeneratorDelegate(DateAndTimeDataGeneratorBuilder dateAndTimeDataGeneratorBuilder) {
        this.dateAndTimeDataGeneratorBuilder = dateAndTimeDataGeneratorBuilder
    }

    DateAndTimeDataGeneratorDelegate between(Date startDate) {
        dateAndTimeDataGeneratorBuilder.startingAt(startDate)
        return this
    }

    /**
     * Sets start date. The value should be in of three formats. 'yyyy-MM-dd', 'yyyy-MM-dd hh:mm' or 'yyyy-MM-dd hh:mm:ss'
     *  Use <code>between(String startDate, String format)</code> if date is in some other format.
     * @param startDate start date in of these formats: 'yyyy-MM-dd', 'yyyy-MM-dd hh:mm' or 'yyyy-MM-dd hh:mm:ss'
     * @return
     */
    DateAndTimeDataGeneratorDelegate between(String startDate) {
        return between(parseDateUsingDefaultFormat(startDate))
    }

    DateAndTimeDataGeneratorDelegate between(String startDate, String format) {
        return between(Date.parse(format, startDate))
    }

    void and(Date endDate) {
        dateAndTimeDataGeneratorBuilder.endingAt(endDate)
    }

    /**
     * Sets end date. The value should be in of three formats. 'yyyy-MM-dd', 'yyyy-MM-dd hh:mm' or 'yyyy-MM-dd hh:mm:ss'
     *  Use <code>and(String endDate, String format)</code> if date is in some other format.
     * @param endDate end date in of these formats: 'yyyy-MM-dd', 'yyyy-MM-dd hh:mm' or 'yyyy-MM-dd hh:mm:ss'
     * @return
     */
    void and(String endDate) {
        and(parseDateUsingDefaultFormat(endDate))
    }

    void and(String endDate, String format) {
        and(Date.parse(format, endDate))
    }

    private static Date parseDateUsingDefaultFormat(String dateString) {
        //NOTE: Don't change the order of try-catch blocks, otherwise most of dates will end up parsed with 'yyyy-MM-dd'
        try {
            return Date.parse('yyyy-MM-dd hh:mm:ss', dateString)
        } catch (Exception e) {
            //Parse exception, do nothing
        }

        try {
            return Date.parse('yyyy-MM-dd hh:mm', dateString)
        } catch (Exception e) {
            //Parse exception, do nothing
        }
        try {
            return Date.parse('yyyy-MM-dd', dateString)
        } catch (Exception e) {
            //Parse exception, do nothing
        }

        throw new IllegalArgumentException("Could not parse the provided date $dateString with date formats: " +
                "'yyyy-MM-dd', 'yyyy-MM-dd hh:mm', 'yyyy-MM-dd hh:mm:ss'")
    }

    /**
     *
     * @param formaty Date format of the generated values, allowed values:'HH:MM AM/PM','HH:MM (24-hour)',
     *                                              'HH:MM:SS AM/PM','HH:MM:SS (24-hour)', 'M/D/YYYY HH:MM:SS AM/PM',
     *                                              'M/D/YYYY HH:MM:SS (24-hour)','M/D/YYYY', 'D Month YYYY',
     *                                              'DayOfWeek D Month YYYY', 'YYYY-MM-DDTHH:mm:ssZ (ISO-8601)';
     */
    void format(String format) {
        dateAndTimeDataGeneratorBuilder.withFormat(format)
    }
}
