package io.swagger.assert4j.samples.java;

import io.swagger.assert4j.support.AssertionUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;

public class ExecuteProjectTest extends ApiTestBase {

    @Test
    public void simpleProjectTest() throws Exception {
        try {
            File projectFile = new File(ExecuteProjectTest.class.getResource("/TestProject.xml").getFile());
            AssertionUtils.assertExecution(executeProject(projectFile));
            assertFalse(true);
        } catch (AssertionError e) {
            // expect the JSON count assertion in the project to fail
        }
    }

    @Ignore
    @Test
    public void simpleCompositeProjectTest() throws Exception {
        try {
            File projectDirectory = new File(ExecuteProjectTest.class.getResource("/CompositeTestProject").getFile());
            AssertionUtils.assertExecution(executeProject(projectDirectory));
            assertFalse(true);
        } catch (AssertionError e) {
            // expect the JSON count assertion in the project to fail
        }
    }
}
