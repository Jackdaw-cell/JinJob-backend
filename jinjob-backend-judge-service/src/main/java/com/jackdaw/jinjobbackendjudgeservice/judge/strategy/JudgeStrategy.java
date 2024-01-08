package com.jackdaw.jinjobbackendjudgeservice.judge.strategy;


import com.jackdaw.jinjobbackendjudgeservice.model.JudgeContext;
import com.jackdaw.jinjobbackendmodel.entity.dto.codesandbox.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
