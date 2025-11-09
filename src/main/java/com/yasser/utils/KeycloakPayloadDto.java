package com.yasser.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class KeycloakPayloadDto {
    private long exp;
    private long iat;

    @JsonProperty("auth_time")
    private long authTime;

    private String jti;
    private String iss;
    private String aud;
    private String sub;
    private String typ;
    private String azp;
    private String sid;
    private String acr;

    @JsonProperty("allowed-origins")
    private List<String> allowedOrigins;

    @JsonProperty("realm_access")
    private Map<String, List<String>> realmAccess;

    @JsonProperty("resource_access")
    private Map<String, Map<String, List<String>>> resourceAccess;

    private String scope;

    @JsonProperty("email_verified")
    private boolean emailVerified;

    private String name;

    @JsonProperty("preferred_username")
    private String preferredUsername;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String email;
}