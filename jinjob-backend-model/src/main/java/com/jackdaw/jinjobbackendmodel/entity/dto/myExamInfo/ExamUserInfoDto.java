package com.jackdaw.jinjobbackendmodel.entity.dto.myExamInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamUserInfoDto {

    private String userId;

    /**
     * OJ通过数量
     */
    private Integer ojAcSum;

    /**
     * 考试通过数量
     */
    private Integer examAcSum;

    /**
     * 考试通过均分
     */
    private Float examAcScore;

}
