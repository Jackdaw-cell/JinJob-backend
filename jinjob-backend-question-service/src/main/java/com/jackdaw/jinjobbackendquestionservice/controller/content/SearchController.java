package com.jackdaw.jinjobbackendquestionservice.controller.content;

import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.common.BaseResponse;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendmodel.entity.po.ExamQuestion;
import com.jackdaw.jinjobbackendmodel.entity.po.QuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.ShareInfo;
import com.jackdaw.jinjobbackendmodel.entity.query.ExamQuestionQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.QuestionInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.ShareInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.enums.PostStatusEnum;
import com.jackdaw.jinjobbackendmodel.enums.RequestFrequencyTypeEnum;
import com.jackdaw.jinjobbackendmodel.enums.SearchTypeEnum;
import com.jackdaw.jinjobbackendquestionservice.controller.ABaseController;
import com.jackdaw.jinjobbackendquestionservice.service.ExamQuestionService;
import com.jackdaw.jinjobbackendquestionservice.service.QuestionInfoService;
import com.jackdaw.jinjobbackendquestionservice.service.ShareInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;

@RestController
@RequestMapping("/search")
public class SearchController extends ABaseController {
    @Resource
    private QuestionInfoService questionInfoService;

    @Resource
    private ExamQuestionService examQuestionService;

    @Resource
    private ShareInfoService shareInfoService;

    @PostMapping("/search")
    @GlobalInterceptor(frequencyType = RequestFrequencyTypeEnum.MINUTE, reqeustFrequencyThreshold = 20)
    public BaseResponse<? extends PaginationResultVO<? extends Serializable>> loadQuestion(@VerifyParam(required = true, min = 3) @RequestParam String keyword,
                                                                                             @VerifyParam(required = true) @RequestParam Integer type,
                                                                                             @RequestParam Integer pageNo) {
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getByType(type);
        if (null == searchTypeEnum) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        switch (searchTypeEnum) {
            case QUESTION:
                QuestionInfoQuery query = new QuestionInfoQuery();
                query.setPageNo(pageNo);
                query.setTitleFuzzy(keyword);
                query.setOrderBy("question_id desc");
                query.setQueryTextContent(false);
                query.setStatus(PostStatusEnum.POST.getStatus());
                PaginationResultVO<QuestionInfo> questionInfoVo = questionInfoService.findListByPage(query);
                for (QuestionInfo item : questionInfoVo.getList()) {
                    item.setQuestion(resetContentImg(item.getQuestion()));
                    item.setAnswerAnalysis(resetContentImg(item.getAnswerAnalysis()));
                }
//                return getSuccessResponseVO(questionInfoVo);
                return ResultUtils.success(questionInfoVo);
            case EXAM_QUESTION:
                ExamQuestionQuery examQuestionQuery = new ExamQuestionQuery();
                examQuestionQuery.setPageNo(pageNo);
                examQuestionQuery.setTitleFuzzy(keyword);
                examQuestionQuery.setOrderBy("question_id desc");
                examQuestionQuery.setStatus(PostStatusEnum.POST.getStatus());
                examQuestionQuery.setQueryQuestionItem(true);
                examQuestionQuery.setQueryAnswer(true);
                PaginationResultVO<ExamQuestion> examQuestion = examQuestionService.findListByPage(examQuestionQuery);
                for (ExamQuestion item : examQuestion.getList()) {
                    item.setQuestion(resetContentImg(item.getQuestion()));
                    item.setAnswerAnalysis(resetContentImg(item.getAnswerAnalysis()));
                }
//                return getSuccessResponseVO(examQuestion);
                return ResultUtils.success(examQuestion);
            case SHARE:
                ShareInfoQuery shareInfoQuery = new ShareInfoQuery();
                shareInfoQuery.setTitleFuzzy(keyword);
                shareInfoQuery.setPageNo(pageNo);
                shareInfoQuery.setOrderBy("share_id desc");
                shareInfoQuery.setStatus(PostStatusEnum.POST.getStatus());
                shareInfoQuery.setQueryTextContent(false);
                PaginationResultVO<ShareInfo> shareVo = shareInfoService.findListByPage(shareInfoQuery);
                for (ShareInfo item : shareVo.getList()) {
                    item.setContent(resetContentImg(item.getContent()));
                }
//                return getSuccessResponseVO(shareVo);
                return ResultUtils.success(shareVo);
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
//                throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }
}
