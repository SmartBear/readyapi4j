package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.PropertyTransfer
import com.smartbear.readyapi.client.model.PropertyTransferTestStep
import com.smartbear.readyapi.client.model.TestStep
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static TestDsl.recipe
import static com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferBuilder.fromPreviousResponse
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
            GET(URI, {
                name FIRST_STEP
            })
            transfer(fromPreviousResponse(sourcePath).toNextRequest(targetPath))
            POST(URI, {
                name LAST_STEP
            })
        }

        verifyPropertyTransferStep(recipe)
    }

    @Test
    void buildsPropertyTransferWithSpecialSyntax() throws Exception {
        TestRecipe recipe = recipe {
            GET (URI, {
                name FIRST_STEP
            })
            transfer sourcePath from response to targetPath of request
            POST(URI, {
                name LAST_STEP
            })
        }

        verifyPropertyTransferStep(recipe)
    }

    @Test
    void buildsPropertyTransferWithMultipleTargetMaps() throws Exception {
        TestRecipe recipe = recipe {
            GET (URI, {
                name FIRST_STEP
            })
            transfer sourcePath from response to targetPath of (step: 'TheStep', property: 'Username')
            POST(URI, {
                name LAST_STEP
            })
        }

        PropertyTransfer transferStep = extractPropertyTransferStep(recipe)
        assert transferStep.target?.targetName == 'TheStep'
        assert transferStep.target?.property == 'Username'
        assert transferStep.target?.path == targetPath
    }

    @Test
    void buildsCorrectSourceWithSpecialSyntaxAndMaps() throws Exception {
        TestRecipe recipe = recipe {
            transfer(step: 'someStep', property: 'SomeProperty', path: sourcePath) to targetPath
        }

        PropertyTransfer transferStep = extractPropertyTransferStep(recipe)
        assert transferStep.source?.sourceName == 'someStep'
        assert transferStep.source?.property == 'SomeProperty'
        assert transferStep.source?.path == sourcePath
    }

    @Test
    void buildsCorrectTargetWithSpecialSyntaxAndMaps() throws Exception {
        TestRecipe recipe = recipe {
            transfer sourcePath from response to (step: 'someStep', property: 'SomeProperty', path: targetPath)
        }

        PropertyTransfer transferStep = extractPropertyTransferStep(recipe)
        assert transferStep.target?.targetName == 'someStep'
        assert transferStep.target?.property == 'SomeProperty'
        assert transferStep.target?.path == targetPath
    }

    private static PropertyTransfer extractPropertyTransferStep(TestRecipe recipe) {
        List<TestStep> testSteps = recipe?.testCase?.testSteps
        if (!testSteps) {
            fail('No test case or test steps created')
        }
        return (testSteps.find ({ it instanceof PropertyTransferTestStep}) as PropertyTransferTestStep).transfers[0]
    }

    private static void verifyPropertyTransferStep(TestRecipe recipe) {
        PropertyTransfer transferStep = extractPropertyTransferStep(recipe)
        assert transferStep.source?.sourceName == FIRST_STEP
        assert transferStep.source?.property == 'Response'
        assert transferStep.source?.path == sourcePath
        assert transferStep.target?.targetName == LAST_STEP
        assert transferStep.target?.property == 'Request'
        assert transferStep.target?.path == targetPath
    }

}
