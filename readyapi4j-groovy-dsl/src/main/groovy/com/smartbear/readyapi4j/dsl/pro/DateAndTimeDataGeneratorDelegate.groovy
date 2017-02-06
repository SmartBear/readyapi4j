package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DateAndTimeDataGeneratorBuilder


/**
 * Delegate for the 'dateAndTimeValues' closure in 'recipe'
 */
class DateAndTimeDataGeneratorDelegate {
    private DateAndTimeDataGeneratorBuilder dateAndTimeDataGeneratorBuilder;

    DateAndTimeDataGeneratorDelegate(DateAndTimeDataGeneratorBuilder dateAndTimeDataGeneratorBuilder) {
        this.dateAndTimeDataGeneratorBuilder = dateAndTimeDataGeneratorBuilder
    }

    void startingAt(Date startDate) {
        dateAndTimeDataGeneratorBuilder.startingAt(startDate)
    }

    void endingAt(Date endDate) {
        dateAndTimeDataGeneratorBuilder.endingAt(endDate)
    }

    /**
     * Not a real getter but a way to use this method without parentheses in a closure
     */
    DateAndTimeDataGeneratorBuilder getFormatHH_MM_AM_PM() {
        dateAndTimeDataGeneratorBuilder.withFormatHH_MM_AM_PM()
    }

    /**
     * Not a real getter but a way to use this method without parentheses in a closure
     */
    DateAndTimeDataGeneratorBuilder getFormatHH_MM_24_HOUR() {
        dateAndTimeDataGeneratorBuilder.withFormatHH_MM_24_HOUR()
    }

    /**
     * Not a real getter but a way to use this method without parentheses in a closure
     */
    DateAndTimeDataGeneratorBuilder getFormatHH_MM_SS_AM_PM() {
        dateAndTimeDataGeneratorBuilder.withFormatHH_MM_SS_AM_PM()
    }

    /**
     * Not a real getter but a way to use this method without parentheses in a closure
     */
    DateAndTimeDataGeneratorBuilder getFormatHH_MM_SS_24_HOUR() {
        dateAndTimeDataGeneratorBuilder.withFormatHH_MM_SS_24_HOUR()
    }

    /**
     * Not a real getter but a way to use this method without parentheses in a closure
     */
    DateAndTimeDataGeneratorBuilder getFormatM_D_YYYY_HH_MM_SS_AM_PM() {
        dateAndTimeDataGeneratorBuilder.withFormatM_D_YYYY_HH_MM_SS_AM_PM()
    }

    /**
     * Not a real getter but a way to use this method without parentheses in a closure
     */
    DateAndTimeDataGeneratorBuilder getFormatM_D_YYYY_HH_MM_SS_24_HOUR() {
        dateAndTimeDataGeneratorBuilder.withFormatM_D_YYYY_HH_MM_SS_24_HOUR()
    }

    /**
     * Not a real getter but a way to use this method without parentheses in a closure
     */
    DateAndTimeDataGeneratorBuilder getFormatM_D_YYYY() {
        dateAndTimeDataGeneratorBuilder.withFormatM_D_YYYY()
    }

    /**
     * Not a real getter but a way to use this method without parentheses in a closure
     */
    DateAndTimeDataGeneratorBuilder getFormatD_MONTH_YYYY() {
        dateAndTimeDataGeneratorBuilder.withFormatD_MONTH_YYYY()
    }

    /**
     * Not a real getter but a way to use this method without parentheses in a closure
     */
    DateAndTimeDataGeneratorBuilder getFormatDAY_OF_WEEK_D_MONTH_YYYY() {
        dateAndTimeDataGeneratorBuilder.withFormatDAY_OF_WEEK_D_MONTH_YYYY()
    }

    /**
     * Not a real getter but a way to use this method without parentheses in a closure
     */
    DateAndTimeDataGeneratorBuilder getFormatISO_8601() {
        dateAndTimeDataGeneratorBuilder.withFormatISO_8601()
    }
}
