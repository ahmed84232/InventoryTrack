package com.yasser.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    OWNER("owner"),
    DATA_ENTRY("dataEntry"),
    EMPLOYEE("employee");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }
}
