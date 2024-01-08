package com.jackdaw.jinjobbackendserviceclient.service.question;


import com.jackdaw.jinjobbackendmodel.entity.po.*;
import com.jackdaw.jinjobbackendmodel.entity.query.*;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.ExamQuestionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
* @author Jackdaw
* @description@description 针对表【question(题目)】的数据库操作Service
* @createDate 2023-08-07 20:58:00
*/
@FeignClient(name = "jinjob-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {


    @PostMapping("/getAppExamQuestion")
    List<ExamQuestionVO> getAppExamQuestion(@RequestBody AppExamQuestionQuery appExamQuestionQuery);

    @PostMapping("/findListByPageForAppExam")
    PaginationResultVO<AppExam> findListByPage(@RequestBody AppExamQuery param);

    //
    @PostMapping("/findListByPageForAppExamQuestion")
    PaginationResultVO<AppExamQuestion> findListByPage(@RequestBody AppExamQuestionQuery param);

    //
    @GetMapping("/findListByPageForAppFeedback")
    PaginationResultVO<AppFeedback> findListByPage(@RequestBody AppFeedbackQuery param);

    @PostMapping("/findListByParamForAppFeedback")
    List<AppFeedback> findListByParam(@RequestBody AppFeedbackQuery param);

    @PostMapping("/saveFeedBack4Client")
    Integer saveFeedBack4Client(@RequestBody AppFeedback appFeedback);

    @GetMapping("/deleteAppFeedbackByFeedbackId")
    Integer deleteAppFeedbackByFeedbackId(@RequestParam("feedbackId") Integer feedbackId);

    @PostMapping("/replyFeedback")
    Integer replyFeedback(@RequestBody AppFeedback appFeedback);
    //
    @GetMapping("/get/id")
    OjQuestionInfo getQuestionById(@RequestParam("questionId") long questionId);

    @GetMapping("/question_submit/get/id")
    OjQuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody OjQuestionSubmit ojQuestionSubmit);

    //
    @PostMapping("/findListByParamForQuestionInfo")
    List<QuestionInfo> findListByParam(@RequestBody QuestionInfoQuery param);

    @GetMapping("/getQuestionInfoByQuestionId")
    QuestionInfo getQuestionInfoByQuestionId(@RequestParam("questionId") Integer questionId);

    //
    @PostMapping("/findListByParamForShareInfo")
    List<ShareInfo> findListByParam(@RequestBody ShareInfoQuery param);

    @GetMapping("/getShareInfoByShareId")
    ShareInfo getShareInfoByShareId(@RequestParam("ShareId") Integer ShareId);

}
