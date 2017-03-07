package com.smartbear.readyapi4j.support;

import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.execution.RecipeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * RecipeFilter that writes all recipes as files to the specified folder
 */
public class FileLoggingRecipeFilter implements RecipeFilter {

    private final static Logger LOG = LoggerFactory.getLogger(FileLoggingRecipeFilter.class);

    private final String targetFolder;
    private final String prefix;
    private final String extension;

    public FileLoggingRecipeFilter(String targetFolder, String prefix, String extension) {
        this.targetFolder = targetFolder;
        this.prefix = prefix;
        this.extension = extension;
    }

    public FileLoggingRecipeFilter(String targetFolder) {
        this(targetFolder, "recipe", "json");
    }

    @Override
    public void filterRecipe(TestRecipe testRecipe) {
        try {
            File directory = new File(targetFolder);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = File.createTempFile(prefix, "." + extension, directory);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(testRecipe.toString().getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            LOG.error("Failed to write recipe to file", e);
        }
    }
}
