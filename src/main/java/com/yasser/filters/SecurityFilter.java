package com.yasser.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yasser.utils.KeycloakPayloadDto;
import com.yasser.utils.KeycloakPrincipal;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Order(1)
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException {
        String token = ((HttpServletRequest) servletRequest).getHeader("authorization");

        if (token != null) {
            String jwt = token.split(" ")[1];
            String payload = jwt.split("\\.")[1];

            // Decoding with base64
            String decodedPayload = new String(Base64.getUrlDecoder().decode(payload));
            KeycloakPayloadDto keycloakPayload = (new ObjectMapper()).readValue(decodedPayload, KeycloakPayloadDto.class);

            Map<String, Map<String, List<String>>> resourceAccess = keycloakPayload.getResourceAccess();
            if (!resourceAccess.isEmpty()) {
                Map<String, List<String>> resource = resourceAccess.get("InventoryManagement");

                if (!resource.isEmpty()) {
                    // Add extracted roles to spring security ecosystem
                    List<GrantedAuthority> authorities = resource.get("roles").stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                    KeycloakPrincipal principal = KeycloakPrincipal
                        .builder()
                        .userName(keycloakPayload.getName())
                        .userId(keycloakPayload.getSub())
                        .build();

                    Authentication auth = new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }


}
