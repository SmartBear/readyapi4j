package com.smartbear.readyapi4j.teststeps.restrequest;

public abstract class ParameterBuilder {
    abstract RestRequestStepBuilder addParameter(RestRequestStepBuilder restRequestStepBuilder);

    protected final String name;
    protected final String value;

    ParameterBuilder(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static class QueryParameterBuilder extends ParameterBuilder {
        QueryParameterBuilder(String name, String value) {
            super(name, value);
        }

        @Override
        public RestRequestStepBuilder addParameter(RestRequestStepBuilder restRequestStepBuilder) {
            return restRequestStepBuilder.addQueryParameter(name, value);
        }
    }

    public static class PathParameterBuilder extends ParameterBuilder {
        PathParameterBuilder(String name, String value) {
            super(name, value);
        }

        @Override
        public RestRequestStepBuilder addParameter(RestRequestStepBuilder restRequestStepBuilder) {
            return restRequestStepBuilder.addPathParameter(name, value);
        }
    }

    public static class MatrixParameterBuilder extends ParameterBuilder {
        MatrixParameterBuilder(String name, String value) {
            super(name, value);
        }

        @Override
        public RestRequestStepBuilder addParameter(RestRequestStepBuilder restRequestStepBuilder) {
            return restRequestStepBuilder.addMatrixParameter(name, value);
        }
    }

    public static class HeaderParameterBuilder extends ParameterBuilder {
        HeaderParameterBuilder(String name, String value) {
            super(name, value);
        }

        @Override
        public RestRequestStepBuilder addParameter(RestRequestStepBuilder restRequestStepBuilder) {
            return restRequestStepBuilder.addHeaderParameter(name, value);
        }
    }

    public static ParameterBuilder query(String name, String value) {
        return new QueryParameterBuilder(name, value);
    }

    public static ParameterBuilder header(String name, String value) {
        return new HeaderParameterBuilder(name, value);
    }

    public static ParameterBuilder path(String name, String value) {
        return new PathParameterBuilder(name, value);
    }

    public static ParameterBuilder matrix(String name, String value) {
        return new MatrixParameterBuilder(name, value);
    }
}
