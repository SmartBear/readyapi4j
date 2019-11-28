package cucumber.runtime.oas;

import com.google.common.collect.Lists;
import io.cucumber.stepexpression.Argument;
import io.swagger.v3.oas.models.Operation;

import java.util.List;
import java.util.Map;

public class WhenOperationWrapper {
    private final List<Argument> operationArguments;
    private final WhenPatternMatcher matcher;
    private String when;
    private Operation operation;
    private Map<String, String> parameters;

    public WhenOperationWrapper(String when, Operation operation, Map<String, String> parameters) {
        this.when = when;
        this.operation = operation;
        this.parameters = parameters;

        operationArguments = extractOperationArguments();
        matcher = new WhenPatternMatcher(when);
    }

    public Operation getOperation() {
        return operation;
    }

    public boolean matches(String text) {
        return matcher.matches(text);
    }

    public String getWhen() {
        return when;
    }

    public List<Argument> getOperationArguments(String stepText) {
        if (matcher.hasArguments()) {
            Map<String, String> args = matcher.getOperationArguments(stepText);
            StringBuilder builder = new StringBuilder();
            for (String name : args.keySet()) {
                builder.append(name).append('=').append(args.get(name)).append('\n');
            }

            return Lists.newArrayList(operationArguments.get(0),
                    new OASBackend.StringArgument(operationArguments.get(1).getValue().toString() + builder.toString()));
        } else {
            return operationArguments;
        }
    }

    private List<Argument> extractOperationArguments() {
        if (parameters != null && !parameters.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String name : parameters.keySet()) {
                builder.append(name).append('=').append(parameters.get(name)).append('\n');
            }
            return Lists.newArrayList(new OASBackend.StringArgument(operation.getOperationId()),
                    new OASBackend.StringArgument(builder.toString()));
        } else {
            return Lists.newArrayList(new OASBackend.StringArgument(operation.getOperationId()),
                    new OASBackend.StringArgument(""));
        }
    }
}
