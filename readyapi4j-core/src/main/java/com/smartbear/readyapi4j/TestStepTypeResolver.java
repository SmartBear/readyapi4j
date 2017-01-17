package com.smartbear.readyapi4j;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.DelayTestStep;
import com.smartbear.readyapi.client.model.GroovyScriptTestStep;
import com.smartbear.readyapi.client.model.JdbcRequestTestStep;
import com.smartbear.readyapi.client.model.PluginTestStep;
import com.smartbear.readyapi.client.model.PropertiesTestStep;
import com.smartbear.readyapi.client.model.PropertyTransferTestStep;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.model.SoapMockResponseTestStep;
import com.smartbear.readyapi.client.model.SoapRequestTestStep;

class TestStepTypeResolver implements TypeIdResolver {
    private static final String REST_REQUEST_TYPE = "REST Request";
    private static final String SOAP_REQUEST_TYPE = "SOAP Request";
    private static final String DATA_SOURCE_TYPE = "DataSource";
    private static final String PROPERTY_TRANSFER_TYPE = "Property Transfer";
    private static final String GROOVY_SCRIPT_TYPE = "Groovy";
    private static final String JDBC_REQUEST_TYPE = "JDBC Request";
    private static final String DELAY_TYPE = "Delay";
    private static final String PROPERTIES_TYPE = "Properties";
    private static final String SOAP_MOCK_RESPONSE_TYPE = "SOAPMockResponse";

    private JavaType baseType;

    @Override
    public void init(JavaType javaType) {
        baseType = javaType;
    }

    @Override
    public String idFromValue(Object object) {
        return idFromValueAndType(object, object.getClass());
    }

    @Override
    public String idFromValueAndType(Object object, Class<?> clazz) {
        try {
            return (String) clazz.getField("type").get(object);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String idFromBaseType() {
        return idFromValueAndType(null, baseType.getRawClass());
    }

    private JavaType typeFromId(String type) {
        switch (type) {
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

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String type) {
        return typeFromId(type);
    }

    @Override
    public String getDescForKnownTypeIds() {
        return null;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}

