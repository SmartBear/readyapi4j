package com.smartbear.readyapi4j.support;

import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.execution.RecipeFilter;
import io.swagger.util.Json;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * RecipeFilter that writes all recipes as files to the specified folder
 */
public class RecipeLogger implements RecipeFilter {

    private final static Logger LOG = LoggerFactory.getLogger(RecipeLogger.class);

    public static final String DEFAULT_PREFIX = "recipe";
    public static final String DEFAULT_EXTENSION = "json";

    private final String targetFolder;
    private final String prefix;
    private final String extension;

    public RecipeLogger(String targetFolder, String prefix, String extension) {
        this.targetFolder = targetFolder;
        this.prefix = prefix;
        this.extension = extension;
    }

    public RecipeLogger(String targetFolder) {
        this(targetFolder, DEFAULT_PREFIX, DEFAULT_EXTENSION);
    }

    @Override
    public void filterRecipe(TestRecipe testRecipe) {
        try {
            File directory = new File(targetFolder);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file;
            String name = testRecipe.getName();
            if(StringUtils.isNotBlank(name)){
                file = new File( directory, createFileName( name, '_') + "." + extension );
            }
            else {
                file = File.createTempFile(prefix, "." + extension, directory);
            }
            try ( FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(prettyPrintRecipe(testRecipe).getBytes());
            }
        } catch (Exception e) {
            LOG.error("Failed to write recipe to file", e);
        }
    }

    private String prettyPrintRecipe(TestRecipe testRecipe) {
        try {
            return Json.pretty( Json.mapper().readTree(testRecipe.toString()));
        } catch (IOException e) {
            return testRecipe.toString();
        }
    }

    public static String createFileName(String str, char whitespaceChar) {
        return str.replaceAll("\\s", String.valueOf(whitespaceChar));
    }
}
