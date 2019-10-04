package io.swagger.assert4j;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.assert4j.assertions.AssertionNames;
import io.swagger.assert4j.client.model.*;

/**
 * Jackson type resolver for parsing assertions in JSON recipes into correct Assertion types
 */

class AssertionTypeResolver extends AbstractTypeIdResolver {
    @Override
    JavaType typeFromId(String typeId) {
        switch (typeId) {
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
            case AssertionNames.JSON_EXISTENCE:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, JsonPathExistenceAssertion.class);
            case AssertionNames.GROOVY_SCRIPT:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, GroovyScriptAssertion.class);
            case AssertionNames.RESPONSE_SLA:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, ResponseSLAAssertion.class);
            case AssertionNames.JDBC_STATUS:
                return TypeFactory.defaultInstance().constructSpecializedType(baseType, JdbcStatusAssertion.class);
            case AssertionNames.JDBC_TIMEOUT:
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
}
