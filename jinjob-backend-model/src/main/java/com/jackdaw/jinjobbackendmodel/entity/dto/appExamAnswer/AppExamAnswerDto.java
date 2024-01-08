package com.jackdaw.jinjobbackendmodel.entity.dto.appExamAnswer;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName app_exam_answer
 */
@TableName(value ="app_exam_answer")
@Data
public class AppExamAnswerDto extends BaseParam implements Serializable {
    /**
     * 
     */
    private String id;

    private Integer categoryId;


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
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}