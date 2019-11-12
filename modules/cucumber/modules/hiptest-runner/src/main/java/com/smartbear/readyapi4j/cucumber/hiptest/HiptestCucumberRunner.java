package com.smartbear.readyapi4j.cucumber.hiptest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * HipTest Cucumber Runner that reads scenarios from HipTest for execution
 */

@CommandLine.Command(subcommands = {
        ImportActionWords.class,
        DownloadAndRunScenario.class,
        CommandLine.HelpCommand.class
}, mixinStandardHelpOptions = true, description = "HipTest Command-line Interface"
)
public class HiptestCucumberRunner extends CommandBase {
    private static final Logger LOG = LoggerFactory.getLogger(HiptestCucumberRunner.class);

    /**
     * Downloads Hiptest scenarios and adds them to the command-line before passing along to CucumberRunner
     *
     * @param args command line arguments
     * @throws Throwable
     */

    @CommandLine.Command(description = "HipTest Cucumber CLI")
    public static void main(String[] args) throws Throwable {

        if (new File("hiptest.properties").exists()) {
            loadHipTestProperties();
        }

        if (hiptestToken == null) {
            hiptestToken = System.getProperty(HIPTEST_TOKEN, System.getenv(HIPTEST_TOKEN));
            if (hiptestToken == null) {
                throw new Exception("Missing hiptest.token system property or environment variable");
            }
        }

        if (hiptestClientId == null) {
            hiptestClientId = System.getProperty(HIPTEST_CLIENTID, System.getenv(HIPTEST_CLIENTID));
            if (hiptestClientId == null) {
                throw new Exception("Missing hiptest.clientid system property or environment variable");
            }
        }

        if (hiptestUid == null) {
            hiptestUid = System.getProperty(HIPTEST_UID, System.getenv(HIPTEST_UID));
            if (hiptestUid == null) {
                throw new Exception("Missing hiptest.uid system property or environment variable");
            }
        }

        CommandLine commandLine = new CommandLine(new HiptestCucumberRunner());
        if (args.length == 0) {
            commandLine.usage(System.out);
        }

        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }

    private static void loadHipTestProperties() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileReader("hiptest.properties"));

        hiptestToken = properties.getProperty(HIPTEST_TOKEN, System.getProperty(HIPTEST_TOKEN));
        hiptestClientId = properties.getProperty(HIPTEST_CLIENTID, System.getProperty(HIPTEST_CLIENTID));
        hiptestUid = properties.getProperty(HIPTEST_UID, System.getProperty(HIPTEST_UID));
        hipTestEndpoint = properties.getProperty(HIPTEST_ENDPOINT,
                System.getProperty(HIPTEST_ENDPOINT, HIPTEST_DEFAULT_ENDPOINT));
        hipTestAccept = properties.getProperty(HIPTEST_ACCEPT,
                System.getProperty(HIPTEST_ACCEPT, HIPTEST_DEFAULT_ACCEPT));
    }
}
