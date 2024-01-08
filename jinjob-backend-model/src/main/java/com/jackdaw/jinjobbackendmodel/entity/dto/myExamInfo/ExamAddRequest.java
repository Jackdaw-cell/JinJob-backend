package com.jackdaw.jinjobbackendmodel.entity.dto.myExamInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExamAddRequest implements Serializable {

    List<String> categoryIds;

    private static final long serialVersionUID = 1L;
}
