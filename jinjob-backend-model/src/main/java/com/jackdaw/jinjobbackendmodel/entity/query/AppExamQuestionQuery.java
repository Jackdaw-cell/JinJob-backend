package com.jackdaw.jinjobbackendmodel.entity.query;


import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 考试问题参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppExamQuestionQuery extends BaseParam {


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

    private String userIdFuzzy;

    /**
     * 问题ID
     */
    private Integer questionId;

    /**
     * 用户答案
     */
    private String userAnswer;

    private String userAnswerFuzzy;

    /**
     * 0:未作答 1:正确  2:错误
     */
    private Integer answerResult;

    private Boolean showUserAnswer;

    private List<String> questionIds;


}
