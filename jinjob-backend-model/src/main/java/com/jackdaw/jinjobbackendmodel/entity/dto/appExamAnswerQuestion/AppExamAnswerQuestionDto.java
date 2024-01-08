package com.jackdaw.jinjobbackendmodel.entity.dto.appExamAnswerQuestion;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jackdaw.jinjobbackendmodel.entity.dto.appExamAnswer.AppExamAnswerDto;
import lombok.Data;

import java.io.Serializable;

/**
 * 面试题回答纪录
 * @TableName app_exam_answer_question
 */
@TableName(value ="app_exam_answer_question")
@Data
public class AppExamAnswerQuestionDto implements Serializable {
    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 问题id
     */
    private Integer questionId;

    /**
     * 面试题答卷id
     */
    private String appExamAnswerId;

    private String userAnswer;

    private String QuestionAnswer;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AppExamAnswerDto other = (AppExamAnswerDto) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}