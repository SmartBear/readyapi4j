package com.smartbear.readyapi4j.teststeps;

public enum TestStepTypes {
    REST_REQUEST("REST Request"),
    PROPERTY_TRANSFER("Property Transfer"),
    DATA_SOURCE("DataSource"),
    GROOVY_SCRIPT("Groovy"),
    JDBC_REQUEST("JDBC Request"),
    SOAP_REQUEST("SOAP Request");

    private String name;

    TestStepTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static TestStepTypes fromString(String typeName) {
        for (TestStepTypes value : TestStepTypes.values()) {
            if (value.getName().equals(typeName)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Not a valid enum: " + typeName);
    }
}
