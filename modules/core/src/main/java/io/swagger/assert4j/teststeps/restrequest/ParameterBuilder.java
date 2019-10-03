package io.swagger.assert4j.teststeps.restrequest;

import io.swagger.assert4j.client.model.RestParameter;

public abstract class ParameterBuilder {
    abstract RestRequestStepBuilder addParameter(RestRequestStepBuilder restRequestStepBuilder);

    protected final String name;
    protected final String value;
    protected final RestParameter.TypeEnum type;

    ParameterBuilder(String name, String value, RestParameter.TypeEnum type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public RestParameter build() {
        return new RestParameter().type(type).name(name).value(value);
    }

    public static class QueryParameterBuilder extends ParameterBuilder {
        QueryParameterBuilder(String name, String value) {
            super(name, value, RestParameter.TypeEnum.QUERY);
        }


        @Override
        public RestRequestStepBuilder addParameter(RestRequestStepBuilder restRequestStepBuilder) {
            return restRequestStepBuilder.addQueryParameter(name, value);
        }
    }

    public static class PathParameterBuilder extends ParameterBuilder {
        PathParameterBuilder(String name, String value) {
            super(name, value, RestParameter.TypeEnum.PATH);
        }

        @Override
        public RestRequestStepBuilder addParameter(RestRequestStepBuilder restRequestStepBuilder) {
            return restRequestStepBuilder.addPathParameter(name, value);
        }
    }

    public static class MatrixParameterBuilder extends ParameterBuilder {
        MatrixParameterBuilder(String name, String value) {
            super(name, value, RestParameter.TypeEnum.MATRIX);
        }

        @Override
        public RestRequestStepBuilder addParameter(RestRequestStepBuilder restRequestStepBuilder) {
            return restRequestStepBuilder.addMatrixParameter(name, value);
        }
    }

    public static class HeaderParameterBuilder extends ParameterBuilder {
        HeaderParameterBuilder(String name, String value) {
            super(name, value, RestParameter.TypeEnum.HEADER);
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
