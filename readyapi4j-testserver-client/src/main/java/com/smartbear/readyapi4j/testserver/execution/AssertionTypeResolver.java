package com.smartbear.readyapi4j.testserver.execution;

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

import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.GROOVY_SCRIPT;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.INVALID_HTTP_STATUS_CODES;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.JDBC_STATUS;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.JDBC_TIME_OUT;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.JSON_PATH_COUNT;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.JSON_PATH_MATCH;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.NOT_SOAP_FAULT;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.RESPONSE_SLA;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.SCHEMA_COMPLIANCE;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.SIMPLE_CONTAINS;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.SIMPLE_NOT_CONTAINS;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.SOAP_FAULT;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.VALID_HTTP_STATUS_CODES;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.X_PATH_MATCH;
import static com.smartbear.readyapi4j.testserver.execution.AssertionNames.X_QUERY_MATCH;

public class AssertionTypeResolver implements TypeIdResolver {
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
            case VALID_HTTP_STATUS_CODES:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, ValidHttpStatusCodesAssertion.class);
            case INVALID_HTTP_STATUS_CODES:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, InvalidHttpStatusCodesAssertion.class);
            case SIMPLE_CONTAINS:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, SimpleContainsAssertion.class);
            case SIMPLE_NOT_CONTAINS:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, SimpleNotContainsAssertion.class);
            case X_PATH_MATCH:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, XPathContainsAssertion.class);
            case X_QUERY_MATCH:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, XQueryContainsAssertion.class);
            case JSON_PATH_MATCH:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, JsonPathContentAssertion.class);
            case JSON_PATH_COUNT:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, JsonPathCountAssertion.class);
            case GROOVY_SCRIPT:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, GroovyScriptAssertion.class);
            case RESPONSE_SLA:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, ResponseSLAAssertion.class);
            case JDBC_STATUS:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, JdbcStatusAssertion.class);
            case JDBC_TIME_OUT:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, JdbcTimeoutAssertion.class);
            case SCHEMA_COMPLIANCE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, SchemaComplianceAssertion.class);
            case SOAP_FAULT:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, SoapFaultAssertion.class);
            case NOT_SOAP_FAULT:
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
