package com.yasser.InventoryTrack.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yasser.InventoryTrack.util.SecurityContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Order(1)
public class RequestResponseLoggingFilter implements Filter {

    private static final int MAX_BODY_LENGTH = 2000;
    private final SecurityContext securityContext;

    public RequestResponseLoggingFilter(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {

        ContentCachingRequestWrapper requestWrapper =
                new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        String token = requestWrapper.getHeader("authorization");

        if (token != null) {

            String jwt = token.split(" ")[1];
            String payload = jwt.split("\\.")[1];

            // Decoding with base64
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String decodedPayload = new String(decoder.decode(payload));

            ObjectMapper mapper = new ObjectMapper();
            HashMap<?, ?> payloadHashmap = mapper.readValue(decodedPayload, HashMap.class);

            this.securityContext.setUserId((String) payloadHashmap.get("sub"));
            this.securityContext.setUserName((String) payloadHashmap.get("name"));

            if (payloadHashmap.containsKey("resource_access")) {

                HashMap<?, ?> resourceAccess = (HashMap<?, ?>) payloadHashmap.get("resource_access");
                if (resourceAccess.containsKey("InventoryManagment")) {
                    HashMap<?, ?> inventoryManagment = (HashMap<?, ?>) resourceAccess.get("InventoryManagment");

                    ArrayList<String> roles = (ArrayList<String>) inventoryManagment.get("roles");
                    this.securityContext.setRoles(roles);

                    System.out.println(roles);
                }
            }

            System.out.println(decodedPayload);
        }

        filterChain.doFilter(servletRequest, servletResponse);

//        log(requestWrapper, responseWrapper);

//        responseWrapper.copyBodyToResponse();
    }

    private void log(ContentCachingRequestWrapper req,
                     ContentCachingResponseWrapper res) {

        StringBuilder sb = new StringBuilder();

        sb.append("\n╔══════════════════════════════════════════════════════╗\n");
        sb.append("║  HTTP ").append(req.getMethod()).append(" ").append(req.getRequestURI());
        if (req.getQueryString() != null) sb.append("?").append(req.getQueryString());
        sb.append("\n╠──────────────────── Request ─────────────────────────╣\n");

        // Request Headers
        appendHeaders(sb, req);

        // Request Body
        appendBody(sb, "Request Body", req.getContentAsByteArray(), req.getContentType());

        sb.append("╠──────────────────── Response ────────────────────────╣\n");
        sb.append("Status: ").append(res.getStatus()).append("\n");

        // Response Headers
        appendHeaders(sb, res);

        // Response Body
        appendBody(sb, "Response Body", res.getContentAsByteArray(), res.getContentType());

        sb.append("╚══════════════════════════════════════════════════════╝\n");

        System.out.println(sb);
    }

    private void appendHeaders(StringBuilder sb, HttpServletRequest req) {
        Set<String> seen = new HashSet<>();
        Enumeration<String> names = req.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (seen.add(name)) {
                sb.append(name).append(": ").append(req.getHeader(name)).append("\n");
            }
        }
    }

    private void appendHeaders(StringBuilder sb, HttpServletResponse res) {
        Set<String> seen = new HashSet<>();
        for (String name : res.getHeaderNames()) {
            if (seen.add(name)) {
                sb.append(name).append(": ").append(res.getHeader(name)).append("\n");
            }
        }
    }

    private void appendBody(StringBuilder sb, String label, byte[] content, String contentType) {
        sb.append(label).append(": ");
        if (content == null || content.length == 0) {
            sb.append("[empty]\n");
            return;
        }
        String body = new String(content, StandardCharsets.UTF_8);
        if (body.length() > MAX_BODY_LENGTH) {
            body = body.substring(0, MAX_BODY_LENGTH) + "... [truncated]";
        }
        sb.append("\n").append(body.trim()).append("\n");
    }


}
