package com.smartbear.readyapi4j.samples.java;

import com.smartbear.readyapi4j.result.RecipeExecutionResult;
import org.junit.Test;

import java.net.URL;

import static com.smartbear.readyapi4j.assertions.Assertions.*;
import static com.smartbear.readyapi4j.extractor.Extractors.fromResponse;
import static com.smartbear.readyapi4j.facade.execution.RecipeExecutionFacade.executeRecipe;
import static com.smartbear.readyapi4j.support.AssertionUtils.assertExecutionResult;
import static com.smartbear.readyapi4j.teststeps.TestSteps.soapRequest;

public class SimpleSoapTest {

    @Test
    public void simpleSoapTest() throws Exception {
        RecipeExecutionResult result = executeRecipe("Simple SOAP Test",
                soapRequest(new URL("http://www.dneonline.com/calculator.asmx?WSDL"))
                        .forBinding("CalculatorSoap12")
                        .forOperation("Add")
                        .withParameter("intA", "2")
                        .withPathParameter("//*:intB", "3")
                        .withAssertions(
                                schemaCompliance(),
                                notSoapFault()
                        )
        );

        assertExecutionResult(result);
    }

    @Test
    public void simpleSoapWithExtractorTest() throws Exception {
        RecipeExecutionResult result = executeRecipe("Simple SOAP Test",
                soapRequest(new URL("http://www.dneonline.com/calculator.asmx?WSDL"))
                        .forBinding("CalculatorSoap12")
                        .forOperation("Add")
                        .named( "Add")
                        .withParameter("intA", "1")
                        .withParameter("intB", "1")
                        .withAssertions(
                                schemaCompliance(),
                                notSoapFault(),
                                xPathContains("//*:AddResult/text()", "2")
                        )
                        .withExtractors(
                                fromResponse("//*:AddResult/text()", v -> {
                                    System.out.println("Result is [" + v + "]");
                                })
                        )
        );

        assertExecutionResult(result);
    }
}
