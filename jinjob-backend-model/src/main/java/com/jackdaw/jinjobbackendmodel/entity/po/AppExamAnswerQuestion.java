package com.jackdaw.jinjobbackendmodel.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 面试题回答纪录
 * @TableName app_exam_answer_question
 */
@TableName(value ="app_exam_answer_question")
@Data
public class AppExamAnswerQuestion implements Serializable {
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

    /**
     * 分数
     */
    private Double score;

    /**
     * 准确度
     */
    private Double accuracy;

    /**
     * 
     */
    private String userAnswer;

    private String answer;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    private Integer status;

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
        AppExamAnswerQuestion other = (AppExamAnswerQuestion) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getQuestionId() == null ? other.getQuestionId() == null : this.getQuestionId().equals(other.getQuestionId()))
            && (this.getAppExamAnswerId() == null ? other.getAppExamAnswerId() == null : this.getAppExamAnswerId().equals(other.getAppExamAnswerId()))
            && (this.getScore() == null ? other.getScore() == null : this.getScore().equals(other.getScore()))
            && (this.getAccuracy() == null ? other.getAccuracy() == null : this.getAccuracy().equals(other.getAccuracy()))
            && (this.getUserAnswer() == null ? other.getUserAnswer() == null : this.getUserAnswer().equals(other.getUserAnswer()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getQuestionId() == null) ? 0 : getQuestionId().hashCode());
        result = prime * result + ((getAppExamAnswerId() == null) ? 0 : getAppExamAnswerId().hashCode());
        result = prime * result + ((getScore() == null) ? 0 : getScore().hashCode());
        result = prime * result + ((getAccuracy() == null) ? 0 : getAccuracy().hashCode());
        result = prime * result + ((getUserAnswer() == null) ? 0 : getUserAnswer().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
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
        sb.append(", questionId=").append(questionId);
        sb.append(", appExamAnswerId=").append(appExamAnswerId);
        sb.append(", score=").append(score);
        sb.append(", accuracy=").append(accuracy);
        sb.append(", userAnswer=").append(userAnswer);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}