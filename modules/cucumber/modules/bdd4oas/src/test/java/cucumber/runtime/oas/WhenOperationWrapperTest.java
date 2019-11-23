package cucumber.runtime.oas;

import io.cucumber.stepexpression.Argument;
import io.swagger.v3.oas.models.Operation;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class WhenOperationWrapperTest {
    @Test
    public void testSimpleWhen() {
        Operation operation = new Operation();
        operation.setOperationId("myOperation");
        WhenOperationWrapper wrapper = new WhenOperationWrapper("a search for owner is done", operation, null);

        assertTrue(wrapper.matches("a search for owner is done"));
        assertFalse(wrapper.matches("a search for test is done"));

        List<Argument> arguments = wrapper.getOperationArguments("a search for owner is done");
        assertEquals(2, arguments.size());
        assertEquals("myOperation", arguments.get(0).getValue());
        assertEquals("", arguments.get(1).getValue());
    }

    @Test
    public void testWhenWithParameters() {
        Operation operation = new Operation();
        operation.setOperationId("myOperation");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("name", "john");
        parameters.put("age", "42");

        WhenOperationWrapper wrapper = new WhenOperationWrapper("a search for owner is done", operation, parameters);

        assertTrue(wrapper.matches("a search for owner is done"));
        assertFalse(wrapper.matches("a search for test is done"));

        List<Argument> arguments = wrapper.getOperationArguments("a search for owner is done");
        assertEquals(2, arguments.size());
        assertEquals("myOperation", arguments.get(0).getValue());
        assertEquals("name=john\nage=42", arguments.get(1).getValue().toString().trim());
    }

    @Test
    public void testParameterizedWhen() {
        Operation operation = new Operation();
        operation.setOperationId("myOperation");
        WhenOperationWrapper wrapper = new WhenOperationWrapper("a search for {owner} is done", operation, null);

        assertTrue(wrapper.matches("a search for owner is done"));
        assertTrue(wrapper.matches("a search for test is done"));
        assertFalse(wrapper.matches("a search for test is not done"));
        assertFalse(wrapper.matches("another search for test is not done"));

        List<Argument> arguments = wrapper.getOperationArguments("a search for test is done");
        assertNotNull(arguments);
        assertEquals(2, arguments.size());
        assertEquals("myOperation", arguments.get(0).getValue());
        assertEquals("owner=test", arguments.get(1).getValue().toString().trim());

        arguments = wrapper.getOperationArguments("a search for \"test with spaces\" is done");
        assertNotNull(arguments);
        assertEquals(2, arguments.size());
        assertEquals("myOperation", arguments.get(0).getValue());
        assertEquals("owner=test with spaces", arguments.get(1).getValue().toString().trim());

        arguments = wrapper.getOperationArguments("a search for \"test with \\\"quotes\\\"\" is done");
        assertNotNull(arguments);
        assertEquals(2, arguments.size());
        assertEquals("myOperation", arguments.get(0).getValue());
        assertEquals("owner=test with \"quotes\"", arguments.get(1).getValue().toString().trim());
    }

    @Test
    public void testMultipleParameterizedWhen() {
        Operation operation = new Operation();
        operation.setOperationId("myOperation");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("test", "test");

        WhenOperationWrapper wrapper = new WhenOperationWrapper("a search for {owner} and {scope} is done", operation, parameters);

        assertFalse(wrapper.matches("a search for owner is done"));
        assertTrue(wrapper.matches("a search for test and test is done"));

        List<Argument> arguments = wrapper.getOperationArguments("a search for test and session is done");
        assertNotNull(arguments);
        assertEquals(2, arguments.size());
        assertEquals("myOperation", arguments.get(0).getValue());
        assertEquals("test=test\nowner=test\nscope=session", arguments.get(1).getValue().toString().trim());
    }

}
