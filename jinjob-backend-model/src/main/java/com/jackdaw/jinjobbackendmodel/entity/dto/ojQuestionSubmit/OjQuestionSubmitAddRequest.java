package com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
@Data
public class OjQuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}