package com.jackdaw.jinjobbackendmodel.enums;


public enum SearchTypeEnum {
    QUESTION(0, "搜索问题"), EXAM_QUESTION(1, "搜索考题"), SHARE(2, "分享");

    private Integer type;
    private String description;

    SearchTypeEnum(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static SearchTypeEnum getByType(Integer type) {
        for (SearchTypeEnum at : SearchTypeEnum.values()) {
            if (at.getType().equals(type)) {
                return at;
            }
        }
        return null;
    }
}
