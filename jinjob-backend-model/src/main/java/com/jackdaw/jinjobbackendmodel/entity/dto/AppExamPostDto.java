package com.jackdaw.jinjobbackendmodel.entity.dto;

import com.jackdaw.jinjobbackendmodel.entity.po.AppExamQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppExamPostDto {
    private Integer examId;
    private String remark;

    private List<AppExamQuestion> appExamQuestionList;


}
