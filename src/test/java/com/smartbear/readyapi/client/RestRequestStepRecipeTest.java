package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.Authentication;
import com.smartbear.readyapi.client.model.RestParameter;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.TestSteps;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.auth.Authentications.basic;
import static com.smartbear.readyapi.client.auth.Authentications.kerberos;
import static com.smartbear.readyapi.client.auth.Authentications.ntlm;
import static com.smartbear.readyapi.client.model.RestParameter.TypeEnum.HEADER;
import static com.smartbear.readyapi.client.model.RestParameter.TypeEnum.MATRIX;
import static com.smartbear.readyapi.client.model.RestParameter.TypeEnum.PATH;
import static com.smartbear.readyapi.client.model.RestParameter.TypeEnum.QUERY;
import static com.smartbear.readyapi.client.teststeps.TestSteps.deleteRequest;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static com.smartbear.readyapi.client.teststeps.TestSteps.postRequest;
import static com.smartbear.readyapi.client.teststeps.TestSteps.putRequest;
import static com.smartbear.readyapi.client.teststeps.TestSteps.restRequest;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RestRequestStepRecipeTest {
    private static final String URI = "http://maps.googleapis.com/maps/api/geocode/xml";
    private static final String REQUEST_BODY = "{ \"values\": [ \"Value 1\", \"Value2 2\"] }";
    public static final String MEDIA_TYPE_APPLICATION_JSON = "application/json";

    @Test
    public void buildsRestRequestTestStepRecipe() throws Exception {

        TestRecipe recipe = newTestRecipe()
                .addStep(getRequest(URI)
                        .named("Rest Request")
                )
                .buildTestRecipe();

        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getName(), is("Rest Request"));
        assertThat(testStep.getType(), is(TestStepTypes.REST_REQUEST.getName()));
        assertThat(testStep.getURI(), is(URI));
        assertThat(testStep.getMethod(), is(TestSteps.HttpMethod.GET.name()));
    }

    @Test
    public void buildsRestRequestTestStepRecipeWithRequestBody() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(postRequest(URI)
                        .named("Rest Request")
                        .withRequestBody(REQUEST_BODY)
                        .withMediaType(MEDIA_TYPE_APPLICATION_JSON)
                        .withEncoding("UTF-8")
                )
                .buildTestRecipe();

        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getMethod(), is(TestSteps.HttpMethod.POST.name()));
        assertThat(testStep.getRequestBody(), is(REQUEST_BODY));
        assertThat(testStep.getMediaType(), is(MEDIA_TYPE_APPLICATION_JSON));
        assertThat(testStep.getEncoding(), is(StandardCharsets.UTF_8.displayName()));
    }

    @Test
    public void buildsRestRequestTestStepRecipeWithRequestHeader() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(getRequest(URI)
                        .addHeader("header", "value")
                )
                .buildTestRecipe();

        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        Map<String, Object> headers = testStep.getHeaders();
        assertThat(headers.size(), is(1));
        assertThat((List<String>) headers.get("header"), is(singletonList("value")));
    }

    @Test
    public void buildsRestRequestTestStepRecipeWithRequestHeaderWithMultipleValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(getRequest(URI)
                        .addHeader("header1", "value1")
                        .addHeader("header1", "value2")
                        .addHeader("header1", "value3")
                )
                .buildTestRecipe();

        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        Map<String, Object> headers = testStep.getHeaders();
        assertThat(headers.size(), is(1));
        assertThat((List<String>) headers.get("header1"), is(Arrays.asList("value1", "value2", "value3")));
    }

    @Test
    public void buildsRestRequestTestStepRecipeWithRequestHeaderWithMultipleValuesInOneGo() throws Exception {
        final List<String> headerValues = Arrays.asList("value1", "value2", "value3");
        TestRecipe recipe = newTestRecipe()
                .addStep(getRequest(URI)
                        .addHeader("header1", headerValues)
                )
                .buildTestRecipe();
        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        Map<String, Object> headers = testStep.getHeaders();
        assertThat(headers.size(), is(1));
        assertThat((List<String>) headers.get("header1"), is(headerValues));
    }

    @Test
    public void buildsRestRequestTestStepWithMethodPut() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(putRequest(URI))
                .buildTestRecipe();

        RestTestRequestStep testStep = ((RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0));
        assertThat(testStep.getMethod(), is(TestSteps.HttpMethod.PUT.name()));
        assertThat(testStep.getURI(), is(URI));
    }

    @Test
    public void buildsRestRequestTestStepWithMethodPost() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(postRequest(URI))
                .buildTestRecipe();

        RestTestRequestStep testStep = ((RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0));
        assertThat(testStep.getMethod(), is(TestSteps.HttpMethod.POST.name()));
        assertThat(testStep.getURI(), is(URI));
    }

    @Test
    public void buildsRestRequestTestStepWithMethodDelete() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(deleteRequest(URI))
                .buildTestRecipe();

        RestTestRequestStep testStep = ((RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0));
        assertThat(testStep.getMethod(), is(TestSteps.HttpMethod.DELETE.name()));
        assertThat(testStep.getURI(), is(URI));
    }

    @Test
    public void buildsRestRequestTestStepWithQueryParameter() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(getRequest(URI)
                        .addQueryParameter("param1", "value1")
                )
                .buildTestRecipe();

        RestParameter parameter = ((RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0)).getParameters().get(0);
        assertThat(parameter.getName(), is("param1"));
        assertThat(parameter.getValue(), is("value1"));
        assertThat(parameter.getType(), is(QUERY));
    }

    @Test
    public void buildsRestRequestTestStepWithHeaderParameter() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(getRequest(URI)
                        .addHeaderParameter("param1", "value1")
                )
                .buildTestRecipe();

        RestParameter parameter = ((RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0)).getParameters().get(0);
        assertThat(parameter.getName(), is("param1"));
        assertThat(parameter.getValue(), is("value1"));
        assertThat(parameter.getType(), is(HEADER));
    }

    @Test
    public void buildsRestRequestTestStepWithPathParameter() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(getRequest(URI)
                        .addPathParameter("param1", "value1")
                )
                .buildTestRecipe();

        RestParameter parameter = ((RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0)).getParameters().get(0);
        assertThat(parameter.getName(), is("param1"));
        assertThat(parameter.getValue(), is("value1"));
        assertThat(parameter.getType(), is(PATH));
    }

    @Test
    public void buildsRestRequestTestStepWithMatrixParameter() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(getRequest(URI)
                        .addMatrixParameter("param1", "value1")
                )
                .buildTestRecipe();

        RestParameter parameter = ((RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0)).getParameters().get(0);
        assertThat(parameter.getName(), is("param1"));
        assertThat(parameter.getValue(), is("value1"));
        assertThat(parameter.getType(), is(MATRIX));
    }

    @Test
    public void buildsRestRequestTestStepRecipeWithRequestTimeout() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(restRequest()
                        .get(URI)
                        .setTimeout(2000)
                )
                .buildTestRecipe();

        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getTimeout(), is(String.valueOf(2000)));
    }

    @Test
    public void buildsRestRequestTestStepRecipeWithRequestTimeoutWithPropertyExpansion() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(restRequest()
                        .get(URI)
                        .setTimeout("${#Project#Timeout")
                )
                .buildTestRecipe();

        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getTimeout(), is("${#Project#Timeout"));
    }

    @Test
    public void buildsRestRequestTestStepRecipeWithRequestOptions() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(getRequest(URI)
                    .postQueryString()
                        .followRedirects()
                        .entitizeParameters()
                )
                .buildTestRecipe();

        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getFollowRedirects(), is(true));
        assertThat(testStep.getEntitizeParameters(), is(true));
        assertThat(testStep.getPostQueryString(), is(true));
    }

    @Test
    public void buildsRestRequestTestStepRecipeWithParameters() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(restRequest()
                        .get(URI)
                        .addQueryParameter("address", "1600+Amphitheatre+Parkway,+Mountain+View,+CA")
                        .addQueryParameter("sensor", "false")
                )
                .buildTestRecipe();

        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        List<RestParameter> parameters = testStep.getParameters();
        assertThat(parameters.size(), is(2));

        assertQueryParameter(parameters.get(0), "address", "1600+Amphitheatre+Parkway,+Mountain+View,+CA");
        assertQueryParameter(parameters.get(1), "sensor", "false");
    }

    @Test
    public void buildsRestRequestTestStepRecipeWithBasicAuthentication() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(restRequest()
                        .get(URI)
                        .setAuthentication(basic("username", "password")
                        )
                )
                .buildTestRecipe();

        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        Authentication authentication = testStep.getAuthentication();

        assertThat(authentication.getUsername(), is("username"));
        assertThat(authentication.getPassword(), is("password"));
        assertThat(authentication.getType(), is("Basic"));
    }

    @Test
    public void buildsRestRequestTestStepRecipeWithNTLMAuthentication() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(restRequest()
                        .get(URI)
                        .setAuthentication(ntlm("username", "password")
                                .setDomain("domain")
                        )
                )
                .buildTestRecipe();

        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        Authentication authentication = testStep.getAuthentication();

        assertThat(authentication.getUsername(), is("username"));
        assertThat(authentication.getPassword(), is("password"));
        assertThat(authentication.getDomain(), is("domain"));
        assertThat(authentication.getType(), is("NTLM"));
    }

    @Test
    public void buildsRestRequestTestStepRecipeWithKerberosAuthentication() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(restRequest()
                        .get(URI)
                        .setAuthentication(kerberos("username", "password")
                                .setDomain("domain1")
                        )
                )
                .buildTestRecipe();

        RestTestRequestStep testStep = (RestTestRequestStep) recipe.getTestCase().getTestSteps().get(0);
        Authentication authentication = testStep.getAuthentication();

        assertThat(authentication.getUsername(), is("username"));
        assertThat(authentication.getPassword(), is("password"));
        assertThat(authentication.getDomain(), is("domain1"));
        assertThat(authentication.getType(), is("SPNEGO/Kerberos"));
    }

    private void assertQueryParameter(RestParameter parameter, String paramName, String value) {
        assertThat(parameter.getName(), is(paramName));
        assertThat(parameter.getValue(), is(value));
        assertThat(parameter.getType(), is(QUERY));
    }
}
