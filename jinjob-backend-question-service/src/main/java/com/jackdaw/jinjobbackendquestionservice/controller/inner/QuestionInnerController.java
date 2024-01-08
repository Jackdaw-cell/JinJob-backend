package com.jackdaw.jinjobbackendquestionservice.controller.inner;

import com.jackdaw.jinjobbackendmodel.entity.po.*;
import com.jackdaw.jinjobbackendmodel.entity.query.*;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.ExamQuestionVO;
import com.jackdaw.jinjobbackendquestionservice.service.*;
import com.jackdaw.jinjobbackendserviceclient.service.question.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private OjQuestionService ojQuestionService;

    @Resource
    private OjQuestionSubmitService ojQuestionSubmitService;

    @Resource
    private AppExamService appExamService;

    @Resource
    private AppExamQuestionService appExamQuestionService;

    @Resource
    private AppFeedbackService appFeedbackService;

    @Resource
    private QuestionInfoService questionInfoService;

    @Resource
    private ShareInfoService shareInfoService;

    @GetMapping("/get/id")
    @Override
    public OjQuestionInfo getQuestionById(@RequestParam("questionId") long questionId) {
        return ojQuestionService.getById(questionId);
    }

    @GetMapping("/question_submit/get/id")
    @Override
    public OjQuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId) {
        return ojQuestionSubmitService.getById(questionSubmitId);
    }

    @PostMapping("/question_submit/update")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody OjQuestionSubmit ojQuestionSubmit) {
        return ojQuestionSubmitService.updateById(ojQuestionSubmit);
    }

    @Override
    @PostMapping("/getAppExamQuestion")
    public List<ExamQuestionVO> getAppExamQuestion(@RequestBody AppExamQuestionQuery appExamQuestionQuery) {
        return appExamService.getAppExamQuestion(appExamQuestionQuery);
    }

    @Override
    @PostMapping("/findListByPageForAppExam")
    public PaginationResultVO<AppExam> findListByPage(@RequestBody AppExamQuery param) {
        return appExamService.findListByPage(param);
    }

    @Override
    @PostMapping("/findListByPageForAppExamQuestion")
    public PaginationResultVO<AppExamQuestion> findListByPage(@RequestBody AppExamQuestionQuery param) {
        return appExamQuestionService.findListByPage(param);
    }

    @Override
    @PostMapping("/findListByPageForAppFeedback")
    public PaginationResultVO<AppFeedback> findListByPage(@RequestBody AppFeedbackQuery param) {
        return appFeedbackService.findListByPage(param);
    }

    @Override
    @PostMapping("/findListByParamForAppFeedback")
    public List<AppFeedback> findListByParam(@RequestBody AppFeedbackQuery param) {
        return appFeedbackService.findListByParam(param);
    }

    @Override
    @PostMapping("/saveFeedBack4Client")
    public Integer saveFeedBack4Client(@RequestBody AppFeedback appFeedback) {
       return appFeedbackService.saveFeedBack4Client(appFeedback);
    }

    @Override
    @PostMapping("/replyFeedback")
    public Integer replyFeedback(@RequestBody AppFeedback appFeedback) {
        return appFeedbackService.replyFeedback(appFeedback);
    }

    @Override
    @GetMapping("/deleteAppFeedbackByFeedbackId")
    public Integer deleteAppFeedbackByFeedbackId(@RequestParam("feedbackId") Integer feedbackId) {
        return appFeedbackService.deleteAppFeedbackByFeedbackId(feedbackId);
    }

    @PostMapping("/findListByParamForQuestionInfo")
    public List<QuestionInfo> findListByParam(@RequestBody QuestionInfoQuery param){
        return questionInfoService.findListByParam(param);
    }

    @GetMapping("/getQuestionInfoByQuestionId")
    public QuestionInfo getQuestionInfoByQuestionId(@RequestParam("questionId") Integer questionId){
        return questionInfoService.getQuestionInfoByQuestionId(questionId);
    }

    @Override
    @PostMapping("/findListByParamForShareInfo")
    public List<ShareInfo> findListByParam(@RequestBody ShareInfoQuery param) {
        return shareInfoService.findListByParam(param);
    }

    @Override
    @GetMapping("/getShareInfoByShareId")
    public ShareInfo getShareInfoByShareId(@RequestParam("ShareId") Integer ShareId) {
        return shareInfoService.getShareInfoByShareId(ShareId);
    }
}
