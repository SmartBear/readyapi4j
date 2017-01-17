package com.smartbear.readyapi4j;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.smartbear.readyapi.client.model.GroovyScriptAssertion;
import com.smartbear.readyapi.client.model.InvalidHttpStatusCodesAssertion;
import com.smartbear.readyapi.client.model.JdbcStatusAssertion;
import com.smartbear.readyapi.client.model.JdbcTimeoutAssertion;
import com.smartbear.readyapi.client.model.JsonPathContentAssertion;
import com.smartbear.readyapi.client.model.JsonPathCountAssertion;
import com.smartbear.readyapi.client.model.NotSoapFaultAssertion;
import com.smartbear.readyapi.client.model.ResponseSLAAssertion;
import com.smartbear.readyapi.client.model.SchemaComplianceAssertion;
import com.smartbear.readyapi.client.model.SimpleContainsAssertion;
import com.smartbear.readyapi.client.model.SimpleNotContainsAssertion;
import com.smartbear.readyapi.client.model.SoapFaultAssertion;
import com.smartbear.readyapi.client.model.ValidHttpStatusCodesAssertion;
import com.smartbear.readyapi.client.model.XPathContainsAssertion;
import com.smartbear.readyapi.client.model.XQueryContainsAssertion;

class AssertionTypeResolver implements TypeIdResolver {
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
            case AssertionNames.VALID_HTTP_STATUS_CODES:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, ValidHttpStatusCodesAssertion.class);
            case AssertionNames.INVALID_HTTP_STATUS_CODES:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, InvalidHttpStatusCodesAssertion.class);
            case AssertionNames.SIMPLE_CONTAINS:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, SimpleContainsAssertion.class);
            case AssertionNames.SIMPLE_NOT_CONTAINS:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, SimpleNotContainsAssertion.class);
            case AssertionNames.XPATH_MATCH:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, XPathContainsAssertion.class);
            case AssertionNames.XQUERY_MATCH:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, XQueryContainsAssertion.class);
            case AssertionNames.JSON_PATH_MATCH:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, JsonPathContentAssertion.class);
            case AssertionNames.JSON_PATH_COUNT:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, JsonPathCountAssertion.class);
            case AssertionNames.GROOVY_SCRIPT:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, GroovyScriptAssertion.class);
            case AssertionNames.RESPONSE_SLA:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, ResponseSLAAssertion.class);
            case AssertionNames.JDBC_STATUS:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, JdbcStatusAssertion.class);
            case AssertionNames.JDBC_TIME_OUT:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, JdbcTimeoutAssertion.class);
            case AssertionNames.SCHEMA_COMPLIANCE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, SchemaComplianceAssertion.class);
            case AssertionNames.SOAP_FAULT:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, SoapFaultAssertion.class);
            case AssertionNames.NOT_SOAP_FAULT:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, NotSoapFaultAssertion.class);
            default:
                return null; //TypeFactory.defaultInstance().constructSpecializedType(baseType, PluginAssertion.class);
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
