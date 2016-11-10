package com.smartbear.readyapi.client.properties;

public class PropertyBuilder {
    public class Property {
        private String key;
        private String value;


        public String getKey(){
            return key;
        }

        public String getValue(){
            return value;
        }
    }

    private Property property;

    private PropertyBuilder(){
        property = new Property();
        property.key = "";
        property.value = "";
    }

    public static PropertyBuilder getInstance(){
        return new PropertyBuilder();
    }

    public PropertyBuilder withKey(String key){
        property.key = key;
        return this;
    }

    public PropertyBuilder withValue(String value){
        property.value = value;
        return this;
    }

    public Property build(){
        return property;
    }

}
