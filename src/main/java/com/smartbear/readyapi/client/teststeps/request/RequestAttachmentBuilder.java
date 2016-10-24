package com.smartbear.readyapi.client.teststeps.request;

import com.smartbear.readyapi.client.model.RequestAttachment;

public class RequestAttachmentBuilder {
   private final RequestAttachment requestAttachment;

   public RequestAttachmentBuilder(){
       requestAttachment = new RequestAttachment();
   }

   public RequestAttachmentBuilder withContentType(String contentType){
       requestAttachment.setContentType(contentType);
       return this;
   }

   public RequestAttachmentBuilder withName(String name){
       requestAttachment.setName(name);
       return this;
   }

   public RequestAttachmentBuilder withContentId(String contentId){
       requestAttachment.setContentId(contentId);
       return this;
   }

   public RequestAttachmentBuilder withContent(byte[] content){
       requestAttachment.setContent(content);
       return this;
   }

   public RequestAttachment build(){
       return requestAttachment;
   }

}
