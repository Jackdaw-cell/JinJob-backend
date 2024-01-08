package com.jackdaw.jinjobbackendmodel.enums;

public enum AppExamStatusEnum {
    INIT(0, "未完成"), FINISHED(1, "完成");

    private Integer status;
    private String description;

    AppExamStatusEnum(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

}
