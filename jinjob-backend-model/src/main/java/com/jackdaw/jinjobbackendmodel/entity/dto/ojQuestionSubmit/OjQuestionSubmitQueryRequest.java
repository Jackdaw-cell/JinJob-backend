package com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit;

import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import com.jackdaw.jinjobbackendmodel.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OjQuestionSubmitQueryRequest extends BaseParam implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;


    /**
     * 用户 id
     */
    private String userId;

    private static final long serialVersionUID = 1L;
}