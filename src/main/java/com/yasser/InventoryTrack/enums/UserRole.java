package com.yasser.InventoryTrack.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    OWNER("owner"),
    DATA_ENTRY("dataEntry"),
    EMPLOYEE("employee");

    public final String value;

    UserRole(String value) {
        this.value = value;
    }
}
