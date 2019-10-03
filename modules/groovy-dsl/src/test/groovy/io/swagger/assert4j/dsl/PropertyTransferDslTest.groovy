package io.swagger.assert4j.dsl

import io.swagger.assert4j.TestRecipe
import io.swagger.assert4j.client.model.PropertyTransfer
import io.swagger.assert4j.client.model.PropertyTransferTestStep
import io.swagger.assert4j.client.model.TestStep
import org.junit.Test

import static io.swagger.assert4j.dsl.TestDsl.recipe
import static io.swagger.assert4j.teststeps.propertytransfer.PropertyTransferBuilder.fromPreviousResponse
import static org.junit.Assert.fail

class PropertyTransferDslTest {

    public static final String URI = "/uri_to_get"
    public static final String FIRST_STEP = 'theGet'
    public static final String LAST_STEP = 'thePost'
    public static final String sourcePath = '$.customer.address'
    public static final String targetPath = '$.customer'
    private static final String TEST_STEP_NAME = 'PropertyTransferStep'

    @Test
    void buildsPropertyTransfer() throws Exception {
        TestRecipe recipe = recipe {
            get URI, {
                name FIRST_STEP
            }
            transfer(fromPreviousResponse(sourcePath).toNextRequest(targetPath))
            post URI, {
                name LAST_STEP
            }
        }

        verifyPropertyTransferStep(recipe)
    }

    @Test
    void buildsPropertyTransferWithSpecialSyntax() throws Exception {
        TestRecipe recipe = recipe {
            get URI, {
                name FIRST_STEP
            }
            transfer sourcePath from response to targetPath of request
            post URI, {
                name LAST_STEP
            }
        }

        verifyPropertyTransferStep(recipe)
    }

    @Test
    void buildsPropertyTransferStepWithName() throws Exception {
        TestRecipe recipe = recipe {
            transfer(fromPreviousResponse(sourcePath).toNextRequest(targetPath), TEST_STEP_NAME)
        }
        PropertyTransferTestStep propertyTransferStep = recipe.testCase.testSteps[0] as PropertyTransferTestStep
        assert propertyTransferStep.name == TEST_STEP_NAME
    }

    @Test
    void buildsPropertyTransferWithNameInClosure() throws Exception {
        TestRecipe recipe = recipe {
            transfer sourcePath name TEST_STEP_NAME from response to targetPath of request
        }
        PropertyTransferTestStep propertyTransferStep = recipe.testCase.testSteps[0] as PropertyTransferTestStep
        assert propertyTransferStep.name == TEST_STEP_NAME
    }

    @Test
    void buildsPropertyTransferWithMultipleTargetMaps() throws Exception {
        TestRecipe recipe = recipe {
            get URI, {
                name FIRST_STEP
            }
            transfer sourcePath from response to targetPath of(step: 'TheStep', property: 'Username')
            post URI, {
                name LAST_STEP
            }
        }

        PropertyTransfer transferStep = extractPropertyTransferFromTestStep(recipe)
        assert transferStep.target?.targetName == 'TheStep'
        assert transferStep.target?.property == 'Username'
        assert transferStep.target?.path == targetPath
    }

    @Test
    void buildsCorrectSourceWithSpecialSyntaxAndMaps() throws Exception {
        TestRecipe recipe = recipe {
            transfer(step: 'someStep', property: 'SomeProperty', path: sourcePath) to targetPath
        }

        PropertyTransfer transferStep = extractPropertyTransferFromTestStep(recipe)
        assert transferStep.source?.sourceName == 'someStep'
        assert transferStep.source?.property == 'SomeProperty'
        assert transferStep.source?.path == sourcePath
    }

    @Test
    void buildsCorrectTargetWithSpecialSyntaxAndMaps() throws Exception {
        TestRecipe recipe = recipe {
            transfer sourcePath from response to(step: 'someStep', property: 'SomeProperty', path: targetPath)
        }

        PropertyTransfer transferStep = extractPropertyTransferFromTestStep(recipe)
        assert transferStep.target?.targetName == 'someStep'
        assert transferStep.target?.property == 'SomeProperty'
        assert transferStep.target?.path == targetPath
    }

    private static PropertyTransfer extractPropertyTransferFromTestStep(TestRecipe recipe) {
        List<TestStep> testSteps = recipe?.testCase?.testSteps
        if (!testSteps) {
            fail('No test case or test steps created')
        }
        return (testSteps.find({ it instanceof PropertyTransferTestStep }) as PropertyTransferTestStep).transfers[0]
    }

    private static void verifyPropertyTransferStep(TestRecipe recipe) {
        PropertyTransfer transferStep = extractPropertyTransferFromTestStep(recipe)
        assert transferStep.source?.sourceName == FIRST_STEP
        assert transferStep.source?.property == 'Response'
        assert transferStep.source?.path == sourcePath
        assert transferStep.target?.targetName == LAST_STEP
        assert transferStep.target?.property == 'Request'
        assert transferStep.target?.path == targetPath
    }

}
