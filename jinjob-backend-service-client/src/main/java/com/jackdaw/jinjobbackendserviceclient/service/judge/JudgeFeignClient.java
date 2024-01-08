package com.jackdaw.jinjobbackendserviceclient.service.judge;


import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 判题服务
 */
@FeignClient(name = "jinjob-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    OjQuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
