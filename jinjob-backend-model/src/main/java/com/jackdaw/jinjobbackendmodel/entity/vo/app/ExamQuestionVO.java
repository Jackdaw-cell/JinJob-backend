package com.jackdaw.jinjobbackendmodel.entity.vo.app;

import com.jackdaw.jinjobbackendmodel.entity.po.ExamQuestionItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * 考试题目
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionVO implements Serializable {

    /**
     * 考试ID
     */
    private Integer examId;
    /**
     * 问题ID
     */
    private Integer questionId;

    /**
     * 标题
     */
    private String title;

    /**
     * 难度
     */
    private Integer difficultyLevel;

    /**
     * 问题类型 0:判断 1:单选题 2:多选
     */
    private Integer questionType;

    /**
     * 问题描述
     */
    private String question;

    /**
     * 答案
     */
    private String questionAnswer;

    /**
     * 回答解释
     */
    private String answerAnalysis;

    /**
     * 用户答案
     */
    private String userAnswer;

    /**
     * 答案结果
     */
    private Integer answerResult;


    private List<ExamQuestionItem> questionItemList;

    private Boolean haveCollect;

}
