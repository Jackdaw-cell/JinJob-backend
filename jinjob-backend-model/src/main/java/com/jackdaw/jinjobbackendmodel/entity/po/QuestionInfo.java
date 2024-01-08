package com.jackdaw.jinjobbackendmodel.entity.po;

import com.jackdaw.jinjobbackendmodel.enums.DateTimePatternEnum;
import com.jackdaw.jinjobbackendcommon.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * 问题
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionInfo implements Serializable {


    /**
     * ID
     */
    private Integer questionId;

    /**
     * 标题
     */
    private String title;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 难度
     */
    private Integer difficultyLevel;

    /**
     * 问题描述
     */
    private String question;

    /**
     * 回答解释
     */
    private String answerAnalysis;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 0:未发布 1:已发布
     */
    private Integer status;

    /**
     * 用户ID
     */
    private String createUserId;

    /**
     * 姓名
     */
    private String createUserName;

    /**
     * 阅读数量
     */
    private Integer readCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 0:内部 1:外部投稿
     */
    private Integer postUserType;

    /**
     * 是否已收藏
     */
    private Boolean haveCollect;

    private Integer collectId;

    @Override
    public String toString() {
        return "ID:" + (questionId == null ? "空" : questionId) + "，标题:" + (title == null ? "空" : title) + "，分类ID:" + (categoryId == null ? "空" : categoryId) + "，分类名称:" + (categoryName == null ? "空" : categoryName) + "，难度:" + (difficultyLevel == null ? "空" : difficultyLevel) + "，问题描述:" + (question == null ? "空" : question) + "，回答解释:" + (answerAnalysis == null ? "空" : answerAnalysis) + "，创建时间:" + (createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "，0:未发布 1:已发布:" + (status == null ? "空" : status) + "，用户ID:" + (createUserId == null ? "空" : createUserId) + "，姓名:" + (createUserName == null ? "空" : createUserName) + "，阅读数量:" + (readCount == null ? "空" : readCount) + "，收藏数:" + (collectCount == null ? "空" : collectCount) + "，0:内部 1:外部投稿:" + (postUserType == null ? "空" : postUserType);
    }
}
