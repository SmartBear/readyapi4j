package com.smartbear.readyapi4j.swagger;

import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi4j.teststeps.TestSteps;
import com.smartbear.readyapi4j.teststeps.TestSteps.HttpMethod;
import com.smartbear.readyapi4j.teststeps.restrequest.RestRequestStepBuilder;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class SwaggerTestStepBuilderTest {

    private static SwaggerTestStepBuilder petstore;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setup() throws Exception {
        final URL url = SwaggerTestStepBuilderTest.class.getResource("/petstore-swagger.json");
        petstore = new SwaggerTestStepBuilder(Paths.get(url.toURI()).toString());
    }

    @Test
    public void testExistingOperation() throws Exception {
        RestRequestStepBuilder<? extends RestRequestStepBuilder> builder = petstore.operation("addPet");

        assertEquals(builder.build().getMethod(), "POST");
        assertEquals(builder.build().getURI(), "http://petstore.swagger.io/v2/pet");
    }

    @Test
    public void testMissingOperation() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("operationId [tjoho] not found in Swagger definition");
        petstore.operation("tjoho");
    }

    @Test
    public void testOperationWithBody() {
        final RestTestRequestStep step = petstore.operationWithBody("addPet")
                .withRequestBody("body").build();
        assertEquals("body", step.getRequestBody());

    }

    @Test
    public void testGetAsOperationWithoutBody() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Body parameter is not allowed for [GET] methods");
        petstore.operationWithBody("findPetsByTags");
    }

    @Test
    public void testOperationWithoutBody() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Body parameter is not defined for the [updatePetWithForm] operation");
        petstore.operationWithBody("updatePetWithForm");
    }

    @Test
    public void testRequest() throws Exception {
        RestRequestStepBuilder<? extends RestRequestStepBuilder> builder = petstore.request("/some/endpoint", TestSteps.HttpMethod.GET);

        assertEquals(builder.build().getMethod(), "GET");
        assertEquals(builder.build().getURI(), "http://petstore.swagger.io/v2/some/endpoint");
    }

    @Test
    public void testRequestWithBody() throws Exception {
        RestTestRequestStep step = petstore.requestWithBody("/some/endpoint", HttpMethod.POST)
                .withRequestBody("body").build();
        assertEquals("body", step.getRequestBody());
    }

    @Test
    public void testGetRequestWithBody() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Body parameter is not allowed for [GET] methods");
        petstore.requestWithBody("/some/endpoint", HttpMethod.GET);
    }

    @Test
    public void testParseSwaggerFromInputStream() throws IOException {
        final InputStream is = SwaggerTestStepBuilderTest.class.getResourceAsStream("/petstore-swagger.json");
        final SwaggerTestStepBuilder swaggerTestStepBuilder = new SwaggerTestStepBuilder(is);
        assertEquals("petstore.swagger.io", swaggerTestStepBuilder.getSwagger().getHost());
    }

    @Test
    public void testParseSwaggerFromInputStreamWithTargetEndpoint() throws IOException {
        final InputStream is = SwaggerTestStepBuilderTest.class.getResourceAsStream("/petstore-swagger.json");
        final SwaggerTestStepBuilder swaggerTestStepBuilder = new SwaggerTestStepBuilder(is, "http://apan.com");
        assertEquals("http://apan.com", swaggerTestStepBuilder.getTargetEndpoint());
    }
}
