package io.swagger.assert4j.cucumber.codegen;

import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.ast.Background;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.Scenario;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import io.swagger.codegen.SwaggerCodegen;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCodegen {
    @Test
    public void testCodegen() throws Exception {
        File target = new File("target/generated-test-resources");

        SwaggerCodegen.main(new String[]{
            "generate",
            "-l", "Assert4jCucumberFeatureGenerator",
            "-i", "swagger.json",
            "-o", target.getPath()
        });

        // validate existence of feature files
        File featuresFolder = new File(target, "features");
        assertTrue(featuresFolder.exists());
        String[] featureFiles = featuresFolder.list((dir, name) -> name.endsWith(".feature"));
        assertTrue(featureFiles.length > 0);

        // validate content of each feature file
        for (String featureFile : featureFiles) {
            Reader reader = new FileReader(new File( featuresFolder, featureFile));
            Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
            GherkinDocument doc = parser.parse(reader);
            Feature feature = doc.getFeature();

            assertTrue( feature.getChildren().size() > 1 );
            assertTrue( feature.getChildren().get(0) instanceof Background );
            assertTrue( feature.getChildren().get(1) instanceof Scenario );

            for (ScenarioDefinition scenarioDefinition : feature.getChildren()) {
                assertNotNull(scenarioDefinition.getName());

                List<Step> steps = scenarioDefinition.getSteps();
                assertNotNull(steps);

                if( scenarioDefinition instanceof Background ){
                    assertEquals("Given", steps.get(0).getKeyword().trim());
                }
                else if( scenarioDefinition instanceof Scenario ){
                    assertEquals( "When", steps.get(0).getKeyword().trim());
                    assertEquals( "Then", steps.get(steps.size()-1).getKeyword().trim());

                    if( steps.size() > 2 ){
                        for( int c = 1; c < steps.size()-1; c++ ){
                            assertEquals( "And", steps.get(c).getKeyword().trim());
                        }
                    }
                }
                else {
                    throw new Exception( "Unexpected scenarioDefinition in [" + featureFile + "]");
                }
            }
        }
    }
}
