package com.smartbear.readyapi.testserver.cucumber.builders;

/**
 * General support methods
 */

public class Support {

    /**
     * turns a simple format such as 'json' or 'xml' into its corresponding content-type
     * by prefixing it with 'application/' if needed.
     *
     * @param format the format to expand
     * @return the expanded format
     */

    public static String expandContentType(String format) {
        if (format.indexOf('/') == -1) {
            return "application/" + format;
        }

        return format;
    }
}
