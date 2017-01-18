package com.smartbear.readyapi4j.attachments;

import com.sun.jersey.core.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;

/**
 * Utility class for creating a RequestAttachmentBuilder for various types of attachments
 */

public class Attachments {

    private static Logger logger = LoggerFactory.getLogger(Attachments.class);

    public static RequestAttachmentBuilder file(File file, String contentType) {
        RequestAttachmentBuilder builder = null;
        try {
            builder = RequestAttachmentBuilder
                    .getInstance()
                    .withName(file.getName())
                    .withContent(Base64.encode(Files.readAllBytes(file.toPath())))
                    .withContentId(file.getName())
                    .withContentType(contentType);
        } catch (IOException ex) {
            logger.error("Could not add file attachment: " + file.getName() + " due to: " + ex.getMessage());
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
            if(readBytes > 0) {
                return byteArray(data, contentType);
            } else {
                throw new IOException("Could not read inputStream");
            }
        } catch (IOException | NullPointerException e) {
            logger.error("Could not add stream attachment since " + e.getMessage());
            return null;
        }
    }

    public static RequestAttachmentBuilder byteArray(byte[] bytes, String contentType) {
        if (bytes != null && contentType != null) {
            return RequestAttachmentBuilder
                    .getInstance()
                    .withContentType(contentType)
                    .withContent(Base64.encode(bytes));
        } else {
            logger.error("Could not add byte array attachment since a required field was not set");
            return null;
        }
    }

    public static RequestAttachmentBuilder string(String content, String contentType){
        if(content != null && contentType != null){
            return RequestAttachmentBuilder
                    .getInstance()
                    .withContent(Base64.encode(content.getBytes()))
                    .withContentType(contentType);
        } else {
            logger.error("Could not add string attachment since a required field was not set");
            return null;
        }
    }

    public static RequestAttachmentBuilder string(String content){
        return string(content, "text/plain");
    }
}
