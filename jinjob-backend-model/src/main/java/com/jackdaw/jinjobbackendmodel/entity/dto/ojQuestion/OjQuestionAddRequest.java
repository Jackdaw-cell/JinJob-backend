package com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
@Data
public class OjQuestionAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

    /**
     * 主方法
     */
    private String mainMethod;

    /**
     * 提交方法
     */
    private String submitMethod;

    /**
     * 输入输出解释
     */
    private String descript;

    private static final long serialVersionUID = 1L;
}