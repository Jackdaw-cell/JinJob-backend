package com.jackdaw.jinjobbackendmodel.enums;

public enum AnswerResultEnum {
    INIT(0, "未作答"), RIGHT(1, "正确"), WRONG(2, "错误");

    private Integer result;
    private String description;

    AnswerResultEnum(int result, String description) {
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
