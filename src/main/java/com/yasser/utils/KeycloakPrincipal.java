package com.yasser.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeycloakPrincipal {
    private final String userId;
    private final String userName;
}
