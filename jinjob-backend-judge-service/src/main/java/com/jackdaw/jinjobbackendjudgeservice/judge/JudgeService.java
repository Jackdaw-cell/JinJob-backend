package com.jackdaw.jinjobbackendjudgeservice.judge;

import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    OjQuestionSubmit doJudge(long questionSubmitId);
}
