package com.example.jwt.common.enums;

import lombok.Getter;

/**
 * @Author HeSuiJin
 * @Date 2021/3/10 18:00
 * @Description:
 */
@Getter
public enum RoleType {
    USER("USER", "用户"),
    TEMP_USER("TEMP_USER", "临时用户"),
    MANAGER("MANAGER", "管理者"),
    ADMIN("ADMIN", "Admin");
    private final String name;
    private final String description;

    RoleType(java.lang.String name, java.lang.String description) {
        this.name = name;
        this.description = description;
    }
}