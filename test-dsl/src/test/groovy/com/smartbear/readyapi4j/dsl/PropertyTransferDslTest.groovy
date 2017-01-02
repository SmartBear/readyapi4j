package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.PropertyTransfer
import com.smartbear.readyapi.client.model.PropertyTransferTestStep
import com.smartbear.readyapi.client.model.TestStep
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static TestDsl.recipe
import static com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferBuilder.fromPreviousResponse
import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.fail

class PropertyTransferDslTest {

    public static final String URI = "/uri_to_get"
    public static final String FIRST_STEP = 'theGet'
    public static final String LAST_STEP = 'thePost'
    public static final String sourcePath = '$.customer.address'
    public static final String targetPath = '$.customer'

    @Test
    void buildsPropertyTransfer() throws Exception {
        TestRecipe recipe = recipe {
            GET(URI, name: FIRST_STEP)
            propertyTransfer(fromPreviousResponse(sourcePath).toNextRequest(targetPath))
            POST(URI, name: LAST_STEP)
        }

        verifyCorrectPropertyTransfer(recipe)
    }

    @Test
    void buildsPropertyTransferWithSpecialSyntax() throws Exception {
        TestRecipe recipe = recipe {
            GET (URI, name: FIRST_STEP)
            transfer sourcePath from response to targetPath of request
            POST(URI, name: LAST_STEP)
        }

        verifyCorrectPropertyTransfer(recipe)
    }

    @Test
    void buildsCorrectSourceWithSpecialSyntaxAndMaps() throws Exception {
        TestRecipe recipe = recipe {
            transfer(step: 'someStep', property: 'SomeProperty', path: sourcePath) to targetPath
        }

        PropertyTransfer transferStep = extractStep(recipe, 0)
        assertThat(transferStep.source?.sourceName, is('someStep'))
        assertThat(transferStep.source?.property, is('SomeProperty'))
        assertThat(transferStep.source?.path, is(sourcePath))
    }

    @Test
    void buildsCorrectTargetWithSpecialSyntaxAndMaps() throws Exception {
        TestRecipe recipe = recipe {
            transfer sourcePath from response to (step: 'someStep', property: 'SomeProperty', path: targetPath)
        }

        PropertyTransfer transferStep = extractStep(recipe, 0)
        assertThat(transferStep.target?.targetName, is('someStep'))
        assertThat(transferStep.target?.property, is('SomeProperty'))
        assertThat(transferStep.target?.path, is(targetPath))
    }

    private static PropertyTransfer extractStep(TestRecipe recipe, int index = 1) {
        List<TestStep> testSteps = recipe?.testCase?.testSteps
        if (!testSteps) {
            fail('No test case or test steps created')
        }
        return (testSteps[index] as PropertyTransferTestStep).transfers[0]
    }

    private static void verifyCorrectPropertyTransfer(TestRecipe recipe) {
        PropertyTransfer transferStep = extractStep(recipe)
        assertThat(transferStep.source?.sourceName, is(FIRST_STEP))
        assertThat(transferStep.source?.property, is('Response'))
        assertThat(transferStep.source?.path, is(sourcePath))
        assertThat(transferStep.target?.targetName, is(LAST_STEP))
        assertThat(transferStep.target?.property, is('Request'))
        assertThat(transferStep.target?.path, is(targetPath))
    }

}
