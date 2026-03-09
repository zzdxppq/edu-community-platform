package com.educp.core.enums;

import lombok.Getter;

@Getter
public enum AdminStatusEnum {

    DISABLED(0, "禁用"),
    ENABLED(1, "启用");

    private final int value;
    private final String label;

    AdminStatusEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static AdminStatusEnum of(int value) {
        for (AdminStatusEnum status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态值: " + value);
    }
}
