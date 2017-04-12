package com.smartbear.readyapi4j.samples.java;

import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.JsonPathContentAssertion;
import com.smartbear.readyapi.client.model.RestParameter;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.model.ValidHttpStatusCodesAssertion;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;
import org.junit.Test;

import java.util.Arrays;

import static com.smartbear.readyapi4j.facade.execution.RecipeExecutionFacade.executeRecipe;
import static com.smartbear.readyapi4j.support.AssertionUtils.assertExecutionResult;

/**
 * same tests as in SimpleTest but without using the fluent interfaces
 */

public class SimpleNonFluentTest extends ApiTestBase {

    @Test
    public void simpleCountTest() throws Exception {

        RestTestRequestStep restTestRequestStep = new RestTestRequestStep();
        restTestRequestStep.setURI("https://api.swaggerhub.com/specs");
        restTestRequestStep.setMethod("GET");
        restTestRequestStep.setType(TestStepTypes.REST_REQUEST.getName());

        RestParameter parameter = new RestParameter();
        parameter.setName("query");
        parameter.setValue("testserver");
        parameter.setType(RestParameter.TypeEnum.QUERY);
        restTestRequestStep.setParameters(Arrays.asList(parameter));

        JsonPathContentAssertion assertion = new JsonPathContentAssertion();
        assertion.setJsonPath("$.totalCount");
        assertion.setExpectedContent("4");
        assertion.setType("JsonPath Match");

        restTestRequestStep.setAssertions(Arrays.<Assertion>asList(assertion));

        TestCase testCase = new TestCase();
        testCase.setFailTestCaseOnError(true);
        testCase.setTestSteps(Arrays.<TestStep>asList(restTestRequestStep));

        TestRecipe recipe = new TestRecipe(testCase);
        assertExecutionResult(executeRecipe(recipe));
    }

    @Test
    public void simpleTest() throws Exception {

        RestTestRequestStep restTestRequestStep = new RestTestRequestStep();
        restTestRequestStep.setURI("https://api.swaggerhub.com/apis");
        restTestRequestStep.setMethod("GET");
        restTestRequestStep.setType(TestStepTypes.REST_REQUEST.getName());

        ValidHttpStatusCodesAssertion assertion = new ValidHttpStatusCodesAssertion();
        assertion.setValidStatusCodes(Arrays.asList("303"));
        assertion.setType("Valid HTTP Status Codes");

        restTestRequestStep.setAssertions(Arrays.<Assertion>asList(assertion));

        TestCase testCase = new TestCase();
        testCase.setFailTestCaseOnError(true);
        testCase.setTestSteps(Arrays.<TestStep>asList(restTestRequestStep));

        TestRecipe recipe = new TestRecipe(testCase);
        assertExecutionResult(executeRecipe(recipe));
    }
}
