package com.jackdaw.jinjobbackendjudgeservice.judge.impl;

import com.jackdaw.jinjobbackendjudgeservice.judge.strategy.impl.DefaultJudgeStrategy;
import com.jackdaw.jinjobbackendjudgeservice.judge.strategy.impl.JavaLanguageJudgeStrategy;
import com.jackdaw.jinjobbackendjudgeservice.model.JudgeContext;
import com.jackdaw.jinjobbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.jackdaw.jinjobbackendmodel.entity.dto.codesandbox.JudgeInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 *  根据传入判题内容决定判题策略：默认/Java
 *
 */
@Service
@Deprecated
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        OjQuestionSubmit ojQuestionSubmit = judgeContext.getOjQuestionSubmit();
        String language = ojQuestionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
