package com.yasser.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public static String getUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof KeycloakPrincipal) {
            return ((KeycloakPrincipal) principal).getUserName();
        }
        // principal might be a String if not using UserDetails
        return principal.toString();
    }

    public static String getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof KeycloakPrincipal) {
            return ((KeycloakPrincipal) principal).getUserId();
        }
        // fallback: if you stored ID in other way, handle here
        return null;
    }
}

