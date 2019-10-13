package com.smartbear.readyapi4j.oas;

import com.smartbear.readyapi4j.client.model.RestTestRequestStep;
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

public class OASTestStepBuilderTest {

    private static OASTestStepBuilder petstore;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setup() throws Exception {
        final URL url = OASTestStepBuilderTest.class.getResource("/petstore-swagger.json");
        petstore = new OASTestStepBuilder(Paths.get(url.toURI()).toString());
    }

    @Test
    public void testExistingOperation() throws Exception {
        RestRequestStepBuilder<? extends RestRequestStepBuilder> builder = petstore.operation("addPet");

        assertEquals("POST", builder.build().getMethod());
        assertEquals("http://petstore.swagger.io/v2/pet", builder.build().getURI());
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
    public void testParseOASFromInputStream() throws IOException {
        final InputStream is = OASTestStepBuilderTest.class.getResourceAsStream("/petstore-swagger.json");
        final OASTestStepBuilder OASTestStepBuilder = new OASTestStepBuilder(is);
        assertEquals("http://petstore.swagger.io/v2", OASTestStepBuilder.getOpenAPI().getServers().get(0).getUrl());
    }

    @Test
    public void testParseOASFromInputStreamWithTargetEndpoint() throws IOException {
        final InputStream is = OASTestStepBuilderTest.class.getResourceAsStream("/petstore-swagger.json");
        final OASTestStepBuilder OASTestStepBuilder = new OASTestStepBuilder(is, "http://apan.com");
        assertEquals("http://apan.com", OASTestStepBuilder.getTargetEndpoint());
    }
}
