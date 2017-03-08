package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.testserver.teststeps.datasource.FileDataSourceTestStepBuilder

/**
 * Delegate for the 'usingCsvFile' closure in 'recipe'
 */
class FileDataSourceTestStepDelegate extends DataSourceTestStepDelegate<FileDataSourceTestStepBuilder> {
    FileDataSourceTestStepDelegate(String filePath, String testStepName) {
        super(new FileDataSourceTestStepBuilder(), testStepName)
        dataSourceTestStepBuilder.withFilePath(filePath)
    }

    void charSet(String charSet) {
        dataSourceTestStepBuilder.withCharSet(charSet)
    }

    void separator(String separator) {
        dataSourceTestStepBuilder.withSeparator(separator)
    }

    FileDataSourceTestStepBuilder getTrim() {
        dataSourceTestStepBuilder.trim()
    }

    FileDataSourceTestStepBuilder getQuotedValues() {
        dataSourceTestStepBuilder.quotedValues()
    }

    void propertyNames(List<String> properties = []) {
        properties.each { propertyName -> dataSourceTestStepBuilder.addProperty(propertyName) }
    }
}
