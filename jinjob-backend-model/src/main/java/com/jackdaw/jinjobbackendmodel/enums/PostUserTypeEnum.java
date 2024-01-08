package com.jackdaw.jinjobbackendmodel.enums;

public enum PostUserTypeEnum {
    INNER(0, "内部用户"), OUTER(1, "外部访客");

    private Integer result;
    private String description;

    PostUserTypeEnum(int result, String description) {
        this.result = result;
        this.description = description;
    }

    public Integer getResult() {
        return result;
    }

    public String getDescription() {
        return description;
    }
}
