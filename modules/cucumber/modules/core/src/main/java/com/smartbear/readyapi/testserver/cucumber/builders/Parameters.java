package com.smartbear.readyapi.testserver.cucumber.builders;

import com.smartbear.readyapi.client.model.RestParameter;

/**
 * Utility class with static method for building various types of REST Parameters
 */

public class Parameters {
    public static RestParameter buildParameter(RestParameter.TypeEnum type, String name, String value) {
        RestParameter parameter = new RestParameter();
        parameter.setType(type);
        parameter.setName(name);
        parameter.setValue(value);
        return parameter;
    }

    public static RestParameter queryParameter(String name, String value ){
        return buildParameter( RestParameter.TypeEnum.QUERY, name, value );
    }

    public static RestParameter pathParameter(String name, String value ){
        return buildParameter( RestParameter.TypeEnum.PATH, name, value );
    }

    public static RestParameter headerParameter(String name, String value ){
        return buildParameter( RestParameter.TypeEnum.HEADER, name, value );
    }
}
