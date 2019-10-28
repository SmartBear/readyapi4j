package com.smartbear.readyapi4j.attachments;

import com.smartbear.readyapi4j.client.model.RequestAttachment;

/**
 * Builder class for RequestAttachment objects
 */

public class RequestAttachmentBuilder {
    private final RequestAttachment requestAttachment;

    private RequestAttachmentBuilder() {
        requestAttachment = new RequestAttachment();
    }

    public static RequestAttachmentBuilder getInstance() {
        return new RequestAttachmentBuilder();
    }


    public RequestAttachmentBuilder withContentType(String contentType) {
        requestAttachment.setContentType(contentType);
        return this;
    }

    public RequestAttachmentBuilder withName(String name) {
        requestAttachment.setName(name);
        return this;
    }

    public RequestAttachmentBuilder withContentId(String contentId) {
        requestAttachment.setContentId(contentId);
        return this;
    }

    public RequestAttachmentBuilder withContent(byte[] content) {
        requestAttachment.setContent(content);
        return this;
    }

    public RequestAttachment build() {
        return requestAttachment;
    }

}
