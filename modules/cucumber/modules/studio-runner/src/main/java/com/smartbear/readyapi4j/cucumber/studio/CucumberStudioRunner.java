package com.smartbear.readyapi4j.cucumber.studio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 *  Cucumber Studio CLI
 */

@CommandLine.Command(subcommands = {
        ClearActionWords.class,
        ImportActionWords.class,
        DownloadAndRunScenario.class,
        CommandLine.HelpCommand.class
}, mixinStandardHelpOptions = true, description = "Cucumber Studio Command-line Interface"
)
public class CucumberStudioRunner extends CommandBase {
    private static final Logger LOG = LoggerFactory.getLogger(CucumberStudioRunner.class);

    @CommandLine.Command(description = "Cucumber Studio CLI")
    public static void main(String[] args) throws Throwable {

        String propertiesPath = System.getenv("studio.properties" );
        if( propertiesPath == null ){
            propertiesPath = "studio.properties";
        }

        if (new File(propertiesPath ).exists()) {
            loadProperties( propertiesPath );
        }

        if (studioToken == null) {
            studioToken = System.getProperty(STUDIO_TOKEN, System.getenv(STUDIO_TOKEN));
            if (studioToken == null) {
                throw new Exception("Missing studio.token system property or environment variable");
            }
        }

        if (studioClientId == null) {
            studioClientId = System.getProperty(STUDIO_CLIENTID, System.getenv(STUDIO_CLIENTID));
            if (studioClientId == null) {
                throw new Exception("Missing studio.clientid system property or environment variable");
            }
        }

        if (studioUid == null) {
            studioUid = System.getProperty(STUDIO_UID, System.getenv(STUDIO_UID));
            if (studioUid == null) {
                throw new Exception("Missing studio.uid system property or environment variable");
            }
        }

        CommandLine commandLine = new CommandLine(new CucumberStudioRunner());
        if (args.length == 0) {
            commandLine.usage(System.out);
        }

        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }

    private static void loadProperties(String propertiesPath) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileReader(propertiesPath));
        System.out.println( "Reading properties from " + propertiesPath);

        studioToken = properties.getProperty(STUDIO_TOKEN, System.getProperty(STUDIO_TOKEN));
        studioClientId = properties.getProperty(STUDIO_CLIENTID, System.getProperty(STUDIO_CLIENTID));
        studioUid = properties.getProperty(STUDIO_UID, System.getProperty(STUDIO_UID));
        studioEndpoint = properties.getProperty(STUDIO_ENDPOINT,
                System.getProperty(STUDIO_ENDPOINT, HTTPS_STUDIO_CUCUMBER_IO_API));
        studioAccept = properties.getProperty(STUDIO_ACCEPT,
                System.getProperty(STUDIO_ACCEPT, STUDIO_DEFAULT_ACCEPT));
    }
}
