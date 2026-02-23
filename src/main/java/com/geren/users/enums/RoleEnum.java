package com.geren.users.enums;

public enum RoleEnum {
    ADMIN("ADMIN"),
    USER("USER");

    private String role;

    RoleEnum(String role){
        this.role = role;
    }
}
