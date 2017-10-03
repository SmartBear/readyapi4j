package io.swagger.assert4j.client.teststeps;

import io.swagger.assert4j.testserver.teststeps.ServerTestSteps;
import io.swagger.assert4j.testserver.teststeps.datasource.DataSourceTestStepBuilder;
import io.swagger.assert4j.testserver.teststeps.datasource.ExcelDataSourceTestStepBuilder;
import io.swagger.assert4j.testserver.teststeps.datasource.FileDataSourceTestStepBuilder;
import io.swagger.assert4j.testserver.teststeps.datasource.GridDataSourceTestStepBuilder;
import io.swagger.assert4j.testserver.teststeps.datasource.datagen.DataGenDataSourceTestStepBuilder;
import io.swagger.assert4j.teststeps.groovyscript.GroovyScriptTestStepBuilder;
import io.swagger.assert4j.teststeps.jdbcrequest.JdbcConnection;
import io.swagger.assert4j.teststeps.plugin.PluginTestStepBuilder;
import io.swagger.assert4j.teststeps.propertytransfer.PropertyTransferTestStepBuilder;
import io.swagger.assert4j.teststeps.restrequest.RestRequestStepBuilder;
import io.swagger.assert4j.teststeps.restrequest.RestRequestStepWithBodyBuilder;
import io.swagger.assert4j.teststeps.soaprequest.SoapRequestStepBuilder;

import java.net.URL;

/**
 * This class exists solely for backward compatibility. The <code>ServerTestSteps</code> class should be used instead.
 *
 * @see ServerTestSteps
 */
@Deprecated
public class TestSteps {

    public static SoapRequestStepBuilder soapRequest(URL wsdlUrl) {
        return ServerTestSteps.soapRequest(wsdlUrl);
    }

    public static RestRequestStepBuilder<RestRequestStepBuilder> restRequest() {
        return ServerTestSteps.restRequest();
    }

    public static RestRequestStepBuilder<RestRequestStepBuilder> getRequest(String uri) {
        return ServerTestSteps.GET(uri);
    }

    public static RestRequestStepWithBodyBuilder postRequest(String uri) {
        return ServerTestSteps.POST(uri);
    }

    public static RestRequestStepWithBodyBuilder putRequest(String uri) {
        return ServerTestSteps.PUT(uri);
    }

    public static RestRequestStepBuilder<RestRequestStepBuilder> deleteRequest(String uri) {
        return ServerTestSteps.DELETE(uri);
    }

    public static PropertyTransferTestStepBuilder propertyTransfer() {
        return ServerTestSteps.propertyTransfer();
    }



    public static GroovyScriptTestStepBuilder groovyScriptStep(String scriptText) {
        return ServerTestSteps.groovyScriptStep(scriptText);
    }

    public static JdbcConnection jdbcConnection(String driver, String connectionString) {
        return ServerTestSteps.jdbcConnection(driver, connectionString);
    }

    /**
     * @param pluginTestStepType test step type defined by plugin. For example one of 'MQTTPublishTestStep',
     *                           'MQTTDropConnectionTestStep' or 'MQTTReceiveTestStep' defined by MQTT ReadyAPI plugin.
     * @return PluginTestStepBuilder
     */
    public static PluginTestStepBuilder pluginTestStep(String pluginTestStepType) {
        return io.swagger.assert4j.teststeps.TestSteps.pluginTestStep(pluginTestStepType);
    }

    public static DataSourceTestStepBuilder dataSource() {
        return ServerTestSteps.dataSource();
    }

    public static GridDataSourceTestStepBuilder gridDataSource() {
        return ServerTestSteps.gridDataSource();
    }

    public static ExcelDataSourceTestStepBuilder excelDataSource() {
        return ServerTestSteps.excelDataSource();
    }

    public static FileDataSourceTestStepBuilder fileDataSource() {
        return ServerTestSteps.fileDataSource();
    }

    public static DataGenDataSourceTestStepBuilder dataGenDataSource() {
        return ServerTestSteps.dataGenDataSource();
    }

    public static GridDataSourceTestStepBuilder gridDataSource(String name) {
        return ServerTestSteps.gridDataSource(name);
    }

    public static ExcelDataSourceTestStepBuilder excelDataSource(String name) {
        return (ExcelDataSourceTestStepBuilder) new ExcelDataSourceTestStepBuilder().named(name);
    }

    public static FileDataSourceTestStepBuilder fileDataSource(String name) {
        return (FileDataSourceTestStepBuilder) new FileDataSourceTestStepBuilder().named(name);
    }

}
