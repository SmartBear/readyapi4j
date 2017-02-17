package com.smartbear.readyapi4j.swagger;

import com.smartbear.readyapi4j.teststeps.TestSteps;
import com.smartbear.readyapi4j.teststeps.restrequest.RestRequestStepBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created on 2017-02-15.
 */
public class SwaggerTestStepBuilderTest {

    private static SwaggerTestStepBuilder petstore;

    @BeforeClass
    public static void setup() {
        petstore = new SwaggerTestStepBuilder("src/test/resources/petstore-swagger.json");
    }

    @Test
    public void testExistingOperation() throws Exception {
        RestRequestStepBuilder<? extends RestRequestStepBuilder> builder = petstore.operation("addPet");

        assertEquals(builder.build().getMethod(), "POST");
        assertEquals(builder.build().getURI(), "http://petstore.swagger.io/v2/pet");
    }

    @Test
    public void testMissingOperation() throws Exception {
        try {
            petstore.operation("tjoho");
            assertFalse("Expected IllegalArgumentException for invalid operationId", true);
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testRequest() throws Exception {
        RestRequestStepBuilder<? extends RestRequestStepBuilder> builder = petstore.request("/some/endpoint", TestSteps.HttpMethod.GET);

        assertEquals(builder.build().getMethod(), "GET");
        assertEquals(builder.build().getURI(), "http://petstore.swagger.io/v2/some/endpoint");
    }
}