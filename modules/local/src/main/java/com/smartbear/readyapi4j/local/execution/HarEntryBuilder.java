package com.smartbear.readyapi4j.local.execution;

import com.eviware.soapui.model.iface.Attachment;
import com.eviware.soapui.model.iface.MessageExchange;
import com.eviware.soapui.model.iface.Request;
import com.eviware.soapui.support.types.StringToStringsMap;
import com.smartbear.readyapi.client.model.HarContent;
import com.smartbear.readyapi.client.model.HarEntry;
import com.smartbear.readyapi.client.model.HarHeader;
import com.smartbear.readyapi.client.model.HarPostData;
import com.smartbear.readyapi.client.model.HarRequest;
import com.smartbear.readyapi.client.model.HarResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HarEntryBuilder {

    public HarEntry createHarEntry(MessageExchange messageExchange) {
        HarEntry harEntry = new HarEntry()
                .request(createHarRequest(messageExchange))
                .time(messageExchange.getTimeTaken());

        if (messageExchange.getResponse() != null) {
            harEntry.response(createHarResponse(messageExchange));
        }

        return harEntry;
    }

    private HarRequest createHarRequest(MessageExchange messageExchange) {
        List<HarHeader> headers = createHarHeaders(messageExchange.getRequestHeaders());
        HarRequest harRequest = new HarRequest()
                .headers(headers)
                .method(messageExchange.getProperty("Method"))
                .url(messageExchange.getEndpoint());

        getStatusValue(messageExchange).ifPresent(harRequest::httpVersion);
        if (shouldCreatePostData(messageExchange)) {
            harRequest.postData(createPostData(messageExchange));
        }
        return harRequest;
    }

    private Optional<String> getStatusValue(MessageExchange messageExchange) {
        List<String> statusHeader = messageExchange.getResponseHeaders().get("#status#");
        if (statusHeader != null && !statusHeader.isEmpty()) {
            String[] values = statusHeader.get(0).split(" ");
            return values.length >= 2 ? Optional.of(values[0]) : Optional.empty();
        }
        return Optional.empty();
    }

    private boolean shouldCreatePostData(MessageExchange messageExchange) {
        return messageExchange.getResponse() != null
                && (requestHasAttachment(messageExchange) || requestHasContent(messageExchange));
    }

    private boolean requestHasContent(MessageExchange messageExchange) {
        return !StringUtils.isEmpty(messageExchange.getRequestContent());
    }

    private boolean requestHasAttachment(MessageExchange messageExchange) {
        Attachment[] requestAttachments = messageExchange.getRequestAttachments();
        return requestAttachments != null && requestAttachments.length > 0;
    }

    private HarResponse createHarResponse(MessageExchange messageExchange) {
        HarContent harContent = new HarContent()
                .mimeType(messageExchange.getResponse().getContentType())
                .size(messageExchange.getResponse().getContentLength())
                .text(messageExchange.getResponseContent());
        StringToStringsMap responseHeaders = messageExchange.getResponseHeaders();
        HarResponse harResponse = new HarResponse()
                .bodySize(messageExchange.getResponse().getContentLength())
                .content(harContent)
                .headersSize(-1L)
                .headers(createHarHeaders(responseHeaders));

        List<String> locationValues = responseHeaders != null ? responseHeaders.get("Location") : null;
        String redirectURL = locationValues != null && !locationValues.isEmpty() ? locationValues.get(0) : "";
        harResponse.redirectURL(redirectURL);

        try {
            List<String> statusHeader = responseHeaders.get("#status#");
            String[] values = statusHeader.get(0).split(" ");
            if (values.length >= 2) {
                harResponse.status(Integer.parseInt(values[1]));
                harResponse.statusText(values[2]);
            }
        } catch (Exception ignore) {

        }

        return harResponse.httpVersion(messageExchange.getProperty("HTTP Version"));
    }

    private HarPostData createPostData(MessageExchange messageExchange) {
        Request request = messageExchange.getResponse().getRequest();
        HarPostData harPostData = new HarPostData()
                .mimeType(request != null ? request.getEncoding() : "");

        return harPostData.text(messageExchange.getRequestContent());
    }

    private List<HarHeader> createHarHeaders(StringToStringsMap headersMap) {
        List<HarHeader> headers = new ArrayList<>();
        if (headersMap != null) {
            for (Map.Entry<String, List<String>> entry : headersMap.entrySet()) {
                headers.add(new HarHeader().name(entry.getKey()).value(entry.toString()));
            }
        }
        return headers;
    }
}
