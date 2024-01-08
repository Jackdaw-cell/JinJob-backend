package com.jackdaw.jinjobbackendmodel.entity.po;

import com.jackdaw.jinjobbackendcommon.annotation.AuthCheck;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 分类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {


    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 名称
     */
    @VerifyParam(required = true)
    private String categoryName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图标
     */
    private String iconPath;

    /**
     * 背景颜色
     */
    private String bgColor;

    /**
     * 0:问题分类 1:考题分类 2:问题分类和考题分类
     */
    @VerifyParam(required = true)
    private Integer type;


    @Override
    public String toString() {
        return "分类ID:" + (categoryId == null ? "空" : categoryId) + "，名称:" + (categoryName == null ? "空" : categoryName) + "，排序:" + (sort == null ? "空" : sort) + "，图标:" + (iconPath == null ? "空" : iconPath) + "，背景颜色:" + (bgColor == null ? "空" : bgColor) + "，0:问题分类 1:考题分类 2:问题分类和考题分类:" + (type == null ? "空" : type);
    }
}
