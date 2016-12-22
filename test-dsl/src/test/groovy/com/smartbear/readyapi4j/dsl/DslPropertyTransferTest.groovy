package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.PropertyTransfer
import com.smartbear.readyapi.client.model.PropertyTransferTestStep
import com.smartbear.readyapi.client.model.RestTestRequestStep
import com.smartbear.readyapi.client.model.TestStep
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static TestDsl.recipe
import static com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferBuilder.fromPreviousResponse
import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail

class DslPropertyTransferTest {

    public static final String URI = "/uri_to_get"
    public static final String FIRST_STEP = 'theGet'
    public static final String LAST_STEP = 'thePost'

    @Test
    void buildsPropertyTransfer() throws Exception {
        String sourcePath = '$.customer.address'
        String targetPath = '$.customer'
        TestRecipe recipe = recipe {
            GET(URI, name: FIRST_STEP)
            propertyTransfer(fromPreviousResponse(sourcePath).toNextRequest(targetPath))
            POST(URI, name: LAST_STEP)
        }

        PropertyTransfer transferStep = extractStep(recipe)
        assertThat(transferStep.source?.sourceName, is(FIRST_STEP))
        assertThat(transferStep.source?.property, is('Response'))
        assertThat(transferStep.source?.path, is(sourcePath))
        assertThat(transferStep.target?.targetName, is(LAST_STEP))
        assertThat(transferStep.target?.property, is('Request'))
        assertThat(transferStep.target?.path, is(targetPath))
    }

    private static PropertyTransfer extractStep(TestRecipe recipe) {
        List<TestStep> testSteps = recipe?.testCase?.testSteps
        if (!testSteps) {
            fail('No test case or test steps created')
        }
        return (testSteps[1] as PropertyTransferTestStep).transfers[0]
    }

}
