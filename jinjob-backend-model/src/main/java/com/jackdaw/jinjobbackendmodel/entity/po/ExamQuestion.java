package com.jackdaw.jinjobbackendmodel.entity.po;

import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.enums.DateTimePatternEnum;
import com.jackdaw.jinjobbackendcommon.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 考试题目
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestion implements Serializable {


    private static final long serialVersionUID = 3897390685876955205L;
    /**
     * 问题ID
     */
    private Integer questionId;

    /**
     * 标题
     */
    @VerifyParam(required = true)
    private String title;

    /**
     * 分类ID
     */
    @VerifyParam(required = true)
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 难度
     */
    @VerifyParam(required = true)
    private Integer difficultyLevel;

    /**
     * 问题类型 0:判断 1:单选题 2:多选
     */
    @VerifyParam(required = true)
    private Integer questionType;

    /**
     * 问题描述
     */
    private String question;

    /**
     * 答案
     */
    @VerifyParam(required = true)
    private String questionAnswer;

    /**
     * 回答解释
     */
    @VerifyParam(required = true)
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
     * 0:内部 1:外部投稿
     */
    private Integer postUserType;

    private List<ExamQuestionItem> questionItemList;



    @Override
    public String toString() {
        return "问题ID:" + (questionId == null ? "空" : questionId) + "，标题:" + (title == null ? "空" : title) + "，分类ID:" + (categoryId == null ? "空" : categoryId) +
                "，分类名称:" + (categoryName == null ? "空" : categoryName) + "，难度:" + (difficultyLevel == null ? "空" : difficultyLevel) + "，问题类型 0:判断 1:单选题 2:多选:" + (questionType == null ? "空" : questionType) + "，问题描述:" + (question == null ? "空" : question) + "，答案:" + (questionAnswer == null ? "空" : questionAnswer) + "，回答解释:" + (answerAnalysis == null ? "空" : answerAnalysis) + "，创建时间:" + (createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "，0:未发布 1:已发布:" + (status == null ? "空" : status) + "，用户ID:" + (createUserId == null ? "空" : createUserId) + "，姓名:" + (createUserName == null ? "空" : createUserName) + "，0:内部 1:外部投稿:" + (postUserType == null ? "空" : postUserType);
    }
}
