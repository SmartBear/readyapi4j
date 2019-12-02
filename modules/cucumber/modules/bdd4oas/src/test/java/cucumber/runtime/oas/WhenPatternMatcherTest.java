package cucumber.runtime.oas;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WhenPatternMatcherTest {
    @Test
    public void testMatcherWithoutArguments() {
        WhenPatternMatcher matcher = new WhenPatternMatcher("a search for something");

        assertTrue(matcher.matches("a search for something"));
        assertFalse(matcher.matches("a search for test is not done"));
    }

    @Test
    public void testMatcherWithSingleArgument() {
        WhenPatternMatcher matcher = new WhenPatternMatcher("a search for {owner} is done");

        assertTrue(matcher.matches("a search for owner is done"));
        assertTrue(matcher.matches("a search for \"test\" is done"));
        assertTrue(matcher.matches("a search for \"value with space\" is done"));
        assertFalse(matcher.matches("a search for test is not done"));
        assertFalse(matcher.matches("a search \" for  test is not done"));
        assertFalse(matcher.matches("another search for test is not done"));
        assertFalse(matcher.matches("test is not done"));
        assertFalse(matcher.matches("a search for test"));
    }
}
