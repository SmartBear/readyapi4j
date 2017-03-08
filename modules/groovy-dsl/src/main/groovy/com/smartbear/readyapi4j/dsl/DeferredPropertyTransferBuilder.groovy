package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.TestRecipeBuilder
import com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferBuilder
import com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferSourceBuilder
import com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferTargetBuilder
import com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferTestStepBuilder

/**
 * This class captures information about a property transfer to be built but defers building it, so that a more
 * natural-language like syntax can be used in the DSL.
 */
class DeferredPropertyTransferBuilder {

    private String sourceStepName
    private String sourcePropertyName
    private String sourcePath
    private TestRecipeBuilder testRecipeBuilder
    private Map targetOptions
    private PropertyTransferBuilder transfer
    private String testStepName

    DeferredPropertyTransferBuilder(Map sourceProperties, TestRecipeBuilder testRecipeBuilder) {
        extractSourceProperties(sourceProperties)
        this.testRecipeBuilder = testRecipeBuilder
    }

    private void extractSourceProperties(Map sourceProperties) {
        if (sourceProperties['step']) {
            sourceStepName = sourceProperties['step'] as String
        }
        if (sourceProperties['property']) {
            sourcePropertyName = sourceProperties['property'] as String
        }
        if (sourceProperties['path']) {
            sourcePath = sourceProperties['path'] as String
        }
    }

    DeferredPropertyTransferBuilder name(String testStepName) {
        this.testStepName = testStepName
        return this
    }

    DeferredPropertyTransferBuilder from(Map sourceOptions) {
        extractSourceProperties(sourceOptions)
        return this
    }

    DeferredPropertyTransferBuilder to(String targetPath) {
        return to([path: targetPath, property: 'Request'])
    }

    DeferredPropertyTransferBuilder to(Map targetProperties) {
        this.targetOptions = targetProperties
        PropertyTransferSourceBuilder source = new PropertyTransferSourceBuilder()
                .withSourceStep(sourceStepName)
                .withProperty(sourcePropertyName)
                .withPath(sourcePath)
        PropertyTransferTargetBuilder target = makeTarget(targetProperties)
        transfer = new PropertyTransferBuilder().withSource(source).withTarget(target)
        PropertyTransferTestStepBuilder testStepBuilder = new PropertyTransferTestStepBuilder().addTransfer(this.transfer)
        testStepBuilder.named(testStepName)
        testRecipeBuilder.addStep(testStepBuilder)
        return this
    }

    private static PropertyTransferTargetBuilder makeTarget(Map targetProperties) {
        return new PropertyTransferTargetBuilder()
                .withTargetStep(targetProperties['step'] as String)
                .withProperty(targetProperties['property'] as String)
                .withPath(targetProperties['path'] as String)
    }

    void of(Map targetOptions) {
        this.targetOptions.putAll(targetOptions)
        transfer.withTarget(makeTarget(this.targetOptions))
    }
}
