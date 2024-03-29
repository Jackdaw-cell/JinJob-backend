package com.jackdaw.jinjobbackendmodel.entity.dto.appExamAnswer;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import com.jackdaw.jinjobbackendmodel.entity.dto.appExamAnswerQuestion.AppExamAnswerQuestionDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @TableName app_exam_answer
 */
@TableName(value ="app_exam_answer")
@Data
public class AppExamAnswerPostRequest extends BaseParam implements Serializable {
    /**
     *
     */
    private String id;

    /**
     *
     */
    private String userId;

    private Integer categoryId;

    private List<AppExamAnswerQuestionDto> appExamAnswerQuestionDtoList;

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
        AppExamAnswerPostRequest other = (AppExamAnswerPostRequest) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()));
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