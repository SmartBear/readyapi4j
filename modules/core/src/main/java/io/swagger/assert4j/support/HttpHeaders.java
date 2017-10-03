package io.swagger.assert4j.support;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Taken from com.google.common.net.HttpHeaders
 */

public final class HttpHeaders {

    public final static boolean isKnownHeader(String name) {
        return HEADERS.contains(name);
    }

    public final static Set HEADERS = new HashSet(Arrays.asList(
            "Cache-Control", "Content-Length", "Content-Type", "Date", "Pragma", "Via", "Warning",
            "Accept", "Accept-Charset", "Accept-Encoding", "Accept-Language",
            "Access-Control-Request-Headers", "Access-Control-Request-Method", "Authorization",
            "Connection", "Cookie", "Expect", "From", "Host", "If-Match", "If-Modified-Since",
            "If-None-Match", "If-Range", "If-Unmodified-Since", "Last-Event-ID", "Max-Forwards",
            "Origin", "Proxy-Authorization", "Range", "Referer", "TE", "Upgrade",
            "User-Agent", "Accept-Ranges", "Access-Control-Allow-Headers",
            "Access-Control-Allow-Methods", "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials", "Access-Control-Expose-Headers",
            "Access-Control-Max-Age", "Age", "Allow", "Content-Disposition", "Content-Encoding",
            "Content-Language", "Content-Location", "Content-MD5", "Content-Range",
            "Content-Security-Policy", "Content-Security-Policy-Report-Only", "ETag",
            "Expires", "Last-Modified", "Link", "Location", "P3P",
            "Proxy-Authenticate", "Refresh", "Retry-After", "Server", "Set-Cookie",
            "Set-Cookie2", "Strict-Transport-Security", "Timing-Allow-Origin",
            "Trailer", "Transfer-Encoding", "Vary", "WWW-Authenticate", "DNT",
            "X-Content-Type-Options", "X-Do-Not-Track", "X-Forwarded-For", "X-Forwarded-Proto",
            "X-Frame-Options", "X-Powered-By", "X-Requested-With", "X-User-IP", "X-XSS-Protection"));

    private HttpHeaders() {
    }
}
