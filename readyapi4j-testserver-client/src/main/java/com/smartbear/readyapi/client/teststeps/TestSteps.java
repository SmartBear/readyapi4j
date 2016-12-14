package com.smartbear.readyapi.client.teststeps;

import com.smartbear.readyapi.client.teststeps.groovyscript.GroovyScriptTestStepBuilder;
import com.smartbear.readyapi.client.teststeps.jdbcrequest.JdbcConnection;
import com.smartbear.readyapi.client.teststeps.plugin.PluginTestStepBuilder;
import com.smartbear.readyapi.client.teststeps.propertytransfer.PropertyTransferTestStepBuilder;
import com.smartbear.readyapi.client.teststeps.restrequest.RestRequestStepBuilder;
import com.smartbear.readyapi.client.teststeps.restrequest.RestRequestStepWithBodyBuilder;
import com.smartbear.readyapi.client.teststeps.soaprequest.SoapRequestStepBuilder;

import java.net.URL;

public class TestSteps {

    public enum HttpMethod {
        GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE, PATCH
    }

    public static SoapRequestStepBuilder soapRequest(URL wsdlUrl) {
        return new SoapRequestStepBuilder().withWsdlAt(wsdlUrl);
    }

    public static RestRequestStepBuilder<RestRequestStepBuilder> restRequest() {
        return new RestRequestStepBuilder<>(null, TestSteps.HttpMethod.GET);
    }

    public static RestRequestStepBuilder<RestRequestStepBuilder> getRequest(String uri) {
        return new RestRequestStepBuilder<>(uri, TestSteps.HttpMethod.GET);
    }

    public static RestRequestStepWithBodyBuilder postRequest(String uri) {
        return new RestRequestStepWithBodyBuilder(uri, TestSteps.HttpMethod.POST);
    }

    public static RestRequestStepWithBodyBuilder putRequest(String uri) {
        return new RestRequestStepWithBodyBuilder(uri, TestSteps.HttpMethod.PUT);
    }

    public static RestRequestStepBuilder<RestRequestStepBuilder> deleteRequest(String uri) {
        return new RestRequestStepBuilder<>(uri, TestSteps.HttpMethod.DELETE);
    }

    public static PropertyTransferTestStepBuilder propertyTransfer() {
        return new PropertyTransferTestStepBuilder();
    }


    public static GroovyScriptTestStepBuilder groovyScriptStep(String scriptText) {
        return new GroovyScriptTestStepBuilder(scriptText);
    }

    public static JdbcConnection jdbcConnection(String driver, String connectionString) {
        return new JdbcConnection(driver, connectionString);
    }

    /**
     * @param pluginTestStepType test step type defined by plugin. For example one of 'MQTTPublishTestStep',
     *                           'MQTTDropConnectionTestStep' or 'MQTTReceiveTestStep' defined by MQTT Ready! API plugin.
     * @return PluginTestStepBuilder
     */
    public static PluginTestStepBuilder pluginTestStep(String pluginTestStepType) {
        return new PluginTestStepBuilder(pluginTestStepType);
    }
}
