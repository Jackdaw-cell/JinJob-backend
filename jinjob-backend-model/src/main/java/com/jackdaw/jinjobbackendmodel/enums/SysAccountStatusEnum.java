package com.jackdaw.jinjobbackendmodel.enums;

public enum SysAccountStatusEnum {
    DISABLE(0, "禁用"), ENABLE(1, "启用");

    private Integer status;
    private String description;

    SysAccountStatusEnum(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public static SysAccountStatusEnum getByStatus(Integer status) {
        for (SysAccountStatusEnum at : SysAccountStatusEnum.values()) {
            if (at.status.equals(status)) {
                return at;
            }
        }
        return null;
    }
}
