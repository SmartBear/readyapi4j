package com.smartbear.readyapi4j.cucumber;

public class CucumberUtils {

    /**
     * Applied to all step parameters for Cucumber Studio compatibility - removes surrounding quotes from specified string if
     * available.
     */

    static public String stripQuotes( String str ){
        if( str == null ){
            return str;
        }

        if( str.length() > 2 && str.charAt(0) == '"' && str.charAt(str.length()-1)== '"'){
            return str.substring(1,str.length()-1);
        }

        return str;
    }
}
