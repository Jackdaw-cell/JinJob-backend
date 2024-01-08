package com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion;

import lombok.Data;

/**
 * 题目配置
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
@Data
public class JudgeConfig {

    /**
     * 时间限制（ms）
     */
    private Long timeLimit;

    /**
     * 内存限制（KB）
     */
    private Long memoryLimit;

    /**
     * 堆栈限制（KB）
     */
    private Long stackLimit;
}
