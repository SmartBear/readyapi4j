package io.swagger.assert4j.util.rest;

public class JsonTestObject {

    private String alternatePath;
    private String message;

    public JsonTestObject(String message, String alternatePath) {
        this.alternatePath = alternatePath;
        this.message = message;
    }

    public String getAlternatePath() {
        return alternatePath;
    }

    public void setAlternatePath(String alternatePath) {
        this.alternatePath = alternatePath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        return message.hashCode() ^ alternatePath.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof JsonTestObject)) return false;
        JsonTestObject json = (JsonTestObject) o;
        return this.message.equals(json.getMessage()) &&
                this.alternatePath.equals(json.getAlternatePath());
    }
}