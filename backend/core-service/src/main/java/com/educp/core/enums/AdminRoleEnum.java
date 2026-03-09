package com.educp.core.enums;

import lombok.Getter;

@Getter
public enum AdminRoleEnum {

    SUPER_ADMIN(1, "超级管理员"),
    SCHOOL_ADMIN(2, "学校管理员");

    private final int value;
    private final String label;

    AdminRoleEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static AdminRoleEnum of(int value) {
        for (AdminRoleEnum role : values()) {
            if (role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("无效的角色值: " + value);
    }
}
