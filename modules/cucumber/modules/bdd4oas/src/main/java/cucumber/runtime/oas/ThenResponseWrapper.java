package cucumber.runtime.oas;

import io.swagger.v3.oas.models.responses.ApiResponse;

import java.util.List;
import java.util.Map;

public class ThenResponseWrapper {

    private ApiResponse apiResponse;
    private List<Map<String, Object>> assertions;

    public ThenResponseWrapper(ApiResponse apiResponse, List<Map<String, Object>> assertions) {

        this.apiResponse = apiResponse;
        this.assertions = assertions;
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    public List<Map<String, Object>> getAssertions() {
        return assertions;
    }
}
