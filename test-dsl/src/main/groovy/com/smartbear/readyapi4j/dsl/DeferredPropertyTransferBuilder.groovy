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
    private TestRecipeBuilder recipeBuilder
    private Map targetOptions
    private PropertyTransferBuilder transfer

    DeferredPropertyTransferBuilder(Map sourceProperties, TestRecipeBuilder recipeBuilder) {
        extractSourceProperties(sourceProperties)
        this.recipeBuilder = recipeBuilder
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

    DeferredPropertyTransferBuilder(String sourcePath, TestRecipeBuilder recipeBuilder) {
        this([path: sourcePath], recipeBuilder)
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
        recipeBuilder.addStep(new PropertyTransferTestStepBuilder().addTransfer(this.transfer))
        return this
    }

    private PropertyTransferTargetBuilder makeTarget(Map targetProperties) {
        PropertyTransferTargetBuilder target = new PropertyTransferTargetBuilder()
                .withTargetStep(targetProperties['step'] as String)
                .withProperty(targetProperties['property'] as String)
                .withPath(targetProperties['path'] as String)
        target
    }

    void of(Map targetOptions) {
        this.targetOptions.putAll(targetOptions)
        transfer.withTarget(makeTarget(this.targetOptions))
    }
}
