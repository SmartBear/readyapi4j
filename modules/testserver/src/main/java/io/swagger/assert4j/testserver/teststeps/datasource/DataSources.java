package io.swagger.assert4j.testserver.teststeps.datasource;


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
