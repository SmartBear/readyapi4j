package com.smartbear.readyapi.client.teststeps;

import com.smartbear.readyapi.client.teststeps.datasource.DataSourceTestStepBuilder;
import com.smartbear.readyapi.client.teststeps.datasource.ExcelDataSourceTestStepBuilder;
import com.smartbear.readyapi.client.teststeps.datasource.FileDataSourceTestStepBuilder;
import com.smartbear.readyapi.client.teststeps.datasource.GridDataSourceTestStepBuilder;
import com.smartbear.readyapi.client.teststeps.groovyscript.GroovyScriptTestStepBuilder;
import com.smartbear.readyapi.client.teststeps.jdbcrequest.JdbcConnection;
import com.smartbear.readyapi.client.teststeps.propertytransfer.PropertyTransferTestStepBuilder;
import com.smartbear.readyapi.client.teststeps.restrequest.BaseRestRequestStepBuilder;
import com.smartbear.readyapi.client.teststeps.restrequest.RestRequestStepBuilder;
import com.smartbear.readyapi.client.teststeps.restrequest.RestRequestStepWithBodyBuilder;
import com.smartbear.readyapi.client.teststeps.restrequest.RestRequestWithBodyBuilder;
import com.smartbear.readyapi.client.teststeps.soaprequest.SoapRequestStepBuilder;

public class TestSteps {

    public enum HttpMethod {
        GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE, PATCH
    }

    public static SoapRequestStepBuilder soapRequest(String wsdl) {
        return new SoapRequestStepBuilder().withWsdl(wsdl);
    }

    public static RestRequestStepBuilder restRequest() {
        return new BaseRestRequestStepBuilder(null, TestSteps.HttpMethod.GET);
    }

    public static RestRequestStepBuilder getRequest(String uri) {
        return new BaseRestRequestStepBuilder(uri, TestSteps.HttpMethod.GET);
    }

    public static RestRequestStepWithBodyBuilder postRequest(String uri) {
        return new RestRequestWithBodyBuilder(uri, TestSteps.HttpMethod.POST);
    }

    public static RestRequestStepWithBodyBuilder putRequest(String uri) {
        return new RestRequestWithBodyBuilder(uri, TestSteps.HttpMethod.PUT);
    }

    public static RestRequestStepBuilder deleteRequest(String uri) {
        return new BaseRestRequestStepBuilder(uri, TestSteps.HttpMethod.DELETE);
    }

    public static PropertyTransferTestStepBuilder propertyTransfer() {
        return new PropertyTransferTestStepBuilder();
    }

    public static DataSourceTestStepBuilder dataSource() {
        return new DataSourceTestStepBuilder();
    }

    public static GridDataSourceTestStepBuilder gridDataSource() {
        return new GridDataSourceTestStepBuilder();
    }

    public static ExcelDataSourceTestStepBuilder excelDataSource() {
        return new ExcelDataSourceTestStepBuilder();
    }

    public static FileDataSourceTestStepBuilder fileDataSource() {
        return new FileDataSourceTestStepBuilder();
    }

    public static GridDataSourceTestStepBuilder gridDataSource(String name) {
        return (GridDataSourceTestStepBuilder) new GridDataSourceTestStepBuilder().named(name);
    }

    public static ExcelDataSourceTestStepBuilder excelDataSource(String name) {
        return (ExcelDataSourceTestStepBuilder) new ExcelDataSourceTestStepBuilder().named(name);
    }

    public static FileDataSourceTestStepBuilder fileDataSource(String name) {
        return (FileDataSourceTestStepBuilder) new FileDataSourceTestStepBuilder().named(name);
    }


    public static GroovyScriptTestStepBuilder groovyScriptStep(String scriptText) {
        return new GroovyScriptTestStepBuilder(scriptText);
    }

    public static JdbcConnection jdbcConnection(String driver, String connectionString) {
        return new JdbcConnection(driver, connectionString);
    }

}
