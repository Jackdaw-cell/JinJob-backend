package com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion;

import lombok.Data;

/**
 * 题目用例
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
@Data
public class JudgeCase {

    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;
}
