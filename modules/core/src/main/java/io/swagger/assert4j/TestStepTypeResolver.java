package io.swagger.assert4j;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.assert4j.client.model.DataSourceTestStep;
import io.swagger.assert4j.client.model.DelayTestStep;
import io.swagger.assert4j.client.model.GroovyScriptTestStep;
import io.swagger.assert4j.client.model.JdbcRequestTestStep;
import io.swagger.assert4j.client.model.PluginTestStep;
import io.swagger.assert4j.client.model.PropertiesTestStep;
import io.swagger.assert4j.client.model.PropertyTransferTestStep;
import io.swagger.assert4j.client.model.RestTestRequestStep;
import io.swagger.assert4j.client.model.SoapMockResponseTestStep;
import io.swagger.assert4j.client.model.SoapRequestTestStep;

/**
 * Jackson type resolver for parsing teststeps in JSON recipes into correct TestStep types
 */

class TestStepTypeResolver extends AbstractTypeIdResolver {
    private static final String REST_REQUEST_TYPE = "REST Request";
    private static final String SOAP_REQUEST_TYPE = "SOAP Request";
    private static final String DATA_SOURCE_TYPE = "DataSource";
    private static final String PROPERTY_TRANSFER_TYPE = "Property Transfer";
    private static final String GROOVY_SCRIPT_TYPE = "Groovy";
    private static final String JDBC_REQUEST_TYPE = "JDBC Request";
    private static final String DELAY_TYPE = "Delay";
    private static final String PROPERTIES_TYPE = "Properties";
    private static final String SOAP_MOCK_RESPONSE_TYPE = "SOAPMockResponse";

    @Override
    JavaType typeFromId(String typeId) {
        switch (typeId) {
            case DATA_SOURCE_TYPE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, DataSourceTestStep.class);
            case REST_REQUEST_TYPE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, RestTestRequestStep.class);
            case SOAP_REQUEST_TYPE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, SoapRequestTestStep.class);
            case PROPERTY_TRANSFER_TYPE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, PropertyTransferTestStep.class);
            case GROOVY_SCRIPT_TYPE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, GroovyScriptTestStep.class);
            case DELAY_TYPE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, DelayTestStep.class);
            case PROPERTIES_TYPE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, PropertiesTestStep.class);
            case SOAP_MOCK_RESPONSE_TYPE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, SoapMockResponseTestStep.class);
            case JDBC_REQUEST_TYPE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, JdbcRequestTestStep.class);
            default:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, PluginTestStep.class);
        }
    }
}

