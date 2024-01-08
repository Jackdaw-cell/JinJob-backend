package com.jackdaw.jinjobbackendmodel.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 考试问题
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppExamQuestion implements Serializable {


    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 考试ID
     */
    private Integer examId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 问题ID
     */
    private Integer questionId;

    /**
     * 用户答案
     */
    private String userAnswer;

    /**
     * 0:未作答 1:正确  2:错误
     */
    private Integer answerResult;


    @Override
    public String toString() {
        return "自增ID:" + (id == null ? "空" : id) + "，考试ID:" + (examId == null ? "空" : examId) + "，用户ID:" + (userId == null ? "空" : userId) + "，问题ID:" + (questionId == null ? "空" : questionId) + "，用户答案:" + (userAnswer == null ? "空" : userAnswer) + "，0:错误 1:正确:" + (answerResult == null ? "空" : answerResult);
    }
}
