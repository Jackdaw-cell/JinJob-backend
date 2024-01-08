package com.jackdaw.jinjobbackendmodel.entity.vo.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 用户在线考试
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppExamVO implements Serializable {


    /**
     * 自增ID
     */
    private Integer examId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 0:未完成 1:已完成
     */
    private Integer status;

    /**
     * 分数 30分制
     */
    private Float score;

    /**
     * 回答结果位图
     */
    private String answerList;

    /**
     * 用时分钟
     */
    private BigDecimal useTimeMin;

    public BigDecimal getUseTimeMin() {
        if (endTime != null && startTime != null) {
            return new BigDecimal(endTime.getTime() - startTime.getTime()).divide(new BigDecimal(1000 * 60), 2, BigDecimal.ROUND_HALF_UP);
        }
        return new BigDecimal(0);
    }

    private List<ExamQuestionVO> examQuestionList;

}
