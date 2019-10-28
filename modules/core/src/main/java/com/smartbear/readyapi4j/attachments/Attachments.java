package com.smartbear.readyapi4j.attachments;

import com.smartbear.readyapi4j.execution.RecipeExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Utility class for adding attachments to a request
 */

public class Attachments {

    private static Logger logger = LoggerFactory.getLogger(Attachments.class);

    public static RequestAttachmentBuilder file(File file, String contentType) {
        RequestAttachmentBuilder builder;
        try {
            builder = RequestAttachmentBuilder
                    .getInstance()
                    .withName(file.getName())
                    .withContent(Base64.getEncoder().encode(Files.readAllBytes(file.toPath())))
                    .withContentId(file.getName())
                    .withContentType(contentType);
        } catch (IOException ex) {
            throw new RecipeExecutionException("Could not add file attachment: " + file.getName(), ex);
        }
        return builder;
    }

    public static RequestAttachmentBuilder file(File file) {
        return file(file, URLConnection.guessContentTypeFromName(file.getName()));
    }

    public static RequestAttachmentBuilder file(String filePath) {
        return file(new File(filePath));
    }

    public static RequestAttachmentBuilder file(String filePath, String contentType) {
        return file(new File(filePath), contentType);
    }

    public static RequestAttachmentBuilder stream(InputStream inputStream, String contentType) {
        try {
            byte[] data = new byte[inputStream.available()];
            int readBytes = inputStream.read(data);
            if (readBytes > 0) {
                return byteArray(data, contentType);
            } else {
                throw new IOException("Could not read inputStream");
            }
        } catch (IOException | NullPointerException e) {
            throw new RecipeExecutionException("Could not add stream attachment", e);
        }
    }

    public static RequestAttachmentBuilder byteArray(byte[] bytes, String contentType) {
        if (bytes != null && contentType != null) {
            return RequestAttachmentBuilder
                    .getInstance()
                    .withContentType(contentType)
                    .withContent(Base64.getEncoder().encode(bytes));
        } else {
            logger.error("Could not add byte array attachment since a required field was not set");
            return null;
        }
    }

    public static RequestAttachmentBuilder string(String content, String contentType) {
        if (content != null && contentType != null) {
            return RequestAttachmentBuilder
                    .getInstance()
                    .withContent(Base64.getEncoder().encode(content.getBytes()))
                    .withContentType(contentType);
        } else {
            logger.error("Could not add string attachment since a required field was not set");
            return null;
        }
    }

    public static RequestAttachmentBuilder string(String content) {
        return string(content, "text/plain");
    }
}
