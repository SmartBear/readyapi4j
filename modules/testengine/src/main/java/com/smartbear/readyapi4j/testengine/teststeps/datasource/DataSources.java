package com.smartbear.readyapi4j.testengine.teststeps.datasource;


public class DataSources {
    public static ExcelDataSourceBuilder excel() {
        return new ExcelDataSourceBuilder();
    }

    public static GridDataSourceBuilder grid() {
        return new GridDataSourceBuilder();
    }

    public static FileDataSourceBuilder file() {
        return new FileDataSourceBuilder();
    }
}
