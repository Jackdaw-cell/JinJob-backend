package com.jackdaw.jinjobbackendmodel.enums;

public enum CollectTypeEnum {
    SHARE(0, "文章收藏"), QUESTION(1, "八股文"), EXAM(2, "考题");

    private Integer type;
    private String description;

    CollectTypeEnum(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static CollectTypeEnum getByType(Integer type) {
        for (CollectTypeEnum at : CollectTypeEnum.values()) {
            if (at.type.equals(type)) {
                return at;
            }
        }
        return null;
    }
}
