package com.jackdaw.jinjobbackendquestionservice.controller.content;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendcommon.utils.Compare2String;
import com.jackdaw.jinjobbackendcommon.utils.HtmlParser;
import com.jackdaw.jinjobbackendmodel.common.BaseResponse;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendmodel.entity.dto.appExamAnswer.AppExamAnswerDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.appExamAnswerQuestion.AppExamAnswerQuestionDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import com.jackdaw.jinjobbackendmodel.entity.po.*;
import com.jackdaw.jinjobbackendmodel.entity.query.QuestionInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.SimplePage;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.AppExamAnswerResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.AppExamAnswerVO;
import com.jackdaw.jinjobbackendmodel.enums.CollectTypeEnum;
import com.jackdaw.jinjobbackendmodel.enums.PageSize;
import com.jackdaw.jinjobbackendmodel.enums.PostStatusEnum;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendmodel.exception.ThrowUtils;
import com.jackdaw.jinjobbackendquestionservice.controller.ABaseController;
import com.jackdaw.jinjobbackendquestionservice.service.AppExamAnswerQuestionService;
import com.jackdaw.jinjobbackendquestionservice.service.AppExamAnswerService;
import com.jackdaw.jinjobbackendquestionservice.service.QuestionInfoService;
import com.jackdaw.jinjobbackendserviceclient.service.user.UserFeignClient;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController extends ABaseController {

    @Resource
    private QuestionInfoService questionInfoService;

    @Resource
    private AppExamAnswerService appExamAnswerService;

    @Resource
    private AppExamAnswerQuestionService appExamAnswerQuestionService;

    @Resource
    private UserFeignClient userFeignClient;

    @PostMapping("/loadQuestion")
    @GlobalInterceptor
    public BaseResponse<PaginationResultVO<QuestionInfo>> loadQuestion(@RequestParam Integer pageNo,
                                                                       @RequestParam Integer pageSize,
                                                                       @RequestParam Integer categoryId) {
        QuestionInfoQuery query = new QuestionInfoQuery();
        query.setPageNo(pageNo);
        query.setPageSize(pageSize);
        query.setCategoryId(categoryId);
        query.setOrderBy("RAND()");
        if (query.getCategoryId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请选择面试题类型");
        }
        query.setQueryTextContent(false);
        query.setStatus(PostStatusEnum.POST.getStatus());
//        return getSuccessResponseVO(questionInfoService.findListByPage(query));
        return ResultUtils.success(questionInfoService.findListByPage(query));
    }

    @PostMapping("/getQuestionDetail")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<QuestionInfo> getQuestionDetailNext(@RequestHeader(value = "Authorization", required = false) String token,
                                                            @VerifyParam(required = true) @RequestParam(required = true) Integer currentId,
                                                            @RequestParam(required = false) Integer nextType) {
        QuestionInfoQuery query = new QuestionInfoQuery();
        query.setStatus(PostStatusEnum.POST.getStatus());
        QuestionInfo questionInfo = questionInfoService.showDetailNext(query, nextType, currentId, true);
        questionInfo.setHaveCollect(false);
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null != userAppDto) {
//            AppUserCollect appUserCollect = appUserCollectService.getAppUserCollectByUserIdAndObjectIdAndCollectType(userAppDto.getUserId(),
//                    questionInfo.getQuestionId().toString(), CollectTypeEnum.QUESTION.getType());
            AppUserCollect appUserCollect = userFeignClient.getAppUserCollectByUserIdAndObjectIdAndCollectType(userAppDto.getUserId(),
                    questionInfo.getQuestionId().toString(), CollectTypeEnum.QUESTION.getType());
            if (appUserCollect != null) {
                questionInfo.setHaveCollect(true);
            }
        }
        questionInfo.setQuestion(resetContentImg(questionInfo.getQuestion()));
        questionInfo.setAnswerAnalysis(resetContentImg(questionInfo.getAnswerAnalysis()));
//        return getSuccessResponseVO(questionInfo);
        return ResultUtils.success(questionInfo);
    }

    /**
     * 加载面试题答卷列表
     * @param token
     * @return
     */
    @GetMapping("/loadAppExamAnswer")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<PaginationResultVO<AppExamAnswer>> getAppExamAnswer(@RequestHeader(value = "Authorization", required = false) String token,
                                                              @RequestParam Integer pageNo,
                                                              @RequestParam Integer pageSize) {
        QueryWrapper<AppExamAnswer> wrapper = new QueryWrapper();
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null == userAppDto) {
            throw new BusinessException(ErrorCode.USERINFO_ERROR);
        }
        wrapper.eq("userId",userAppDto.getUserId());
        int count = (int)appExamAnswerService.count(wrapper);
        pageSize = pageSize == null ? PageSize.SIZE15.getSize() :(int) pageSize;
        pageNo = pageNo == null ? 0 :(int) pageNo;
        SimplePage page = new SimplePage(pageNo, count, pageSize);
        Page<AppExamAnswer> questionPage = appExamAnswerService.page(new Page<>(pageNo, pageSize),
                wrapper);
        PaginationResultVO<AppExamAnswer> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), questionPage.getRecords());
        return ResultUtils.success(result);
    }

    /**
     * 加载已回答面试题列表
     * @param token
     * @return
     */
    @GetMapping("/loadAppExamAnswerQuestion")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<PaginationResultVO<AppExamAnswerQuestion>> loadAppExamAnswerQuestion(@RequestHeader(value = "Authorization", required = false) String token,
                                                                               @RequestParam Integer pageNo,
                                                                               @RequestParam Integer pageSize) {
        QueryWrapper<AppExamAnswerQuestion> wrapper = new QueryWrapper();
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null == userAppDto) {
            throw new BusinessException(ErrorCode.USERINFO_ERROR);
        }
        wrapper.eq("userId",userAppDto.getUserId());
        int count = (int)appExamAnswerQuestionService.count(wrapper);
        pageSize = pageSize == null ? PageSize.SIZE15.getSize() :(int) pageSize;
        pageNo = pageNo == null ? 0 :(int) pageNo;
        SimplePage page = new SimplePage(pageNo, count, pageSize);
        Page<AppExamAnswerQuestion> questionPage = appExamAnswerQuestionService.page(new Page<>(pageNo, pageSize),
                wrapper);
        PaginationResultVO<AppExamAnswerQuestion> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), questionPage.getRecords());
        return ResultUtils.success(result);
    }

    /**
     * 新增/修改面试题答卷
     * @param token
     * @return
     */
    @PostMapping("/saveAppExamAnswer")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<String> saveAppExamAnswer(@RequestHeader(value = "Authorization", required = false) String token,
                                                                       @RequestBody AppExamAnswerDto appExamAnswerDto) {
        if (appExamAnswerDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null == userAppDto){
            throw new BusinessException(ErrorCode.USERINFO_ERROR);
        }
        AppExamAnswer appExamAnswer = new AppExamAnswer();
        BeanUtils.copyProperties(appExamAnswerDto, appExamAnswer);
        appExamAnswer.setUserId(userAppDto.getUserId());
        boolean result = appExamAnswerService.saveOrUpdate(appExamAnswer);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        String appExamAnswerId = appExamAnswer.getId();
        return ResultUtils.success(appExamAnswerId);
    }

    /**
     * 查询答卷详情
     * @param token
     * @return
     */
    @GetMapping("/getAppExamAnswerDetail")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<AppExamAnswerResultVO> getAppExamAnswerDetail(@RequestHeader(value = "Authorization", required = false) String token,
                                                                      @RequestParam String id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null == userAppDto){
            throw new BusinessException(ErrorCode.USERINFO_ERROR);
        }
        QueryWrapper<AppExamAnswerQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("appExamAnswerId",id);
        List<AppExamAnswerQuestion> appExamAnswerQuestionList = appExamAnswerQuestionService.list(wrapper);
        for (AppExamAnswerQuestion answerQuestion:
            appExamAnswerQuestionList) {
            QuestionInfo questionInfoByQuestion = questionInfoService.getQuestionInfoByQuestionId(answerQuestion.getQuestionId());
            answerQuestion.setAnswer(questionInfoByQuestion.getAnswerAnalysis());
        }
        AppExamAnswerResultVO appExamAnswerVO = new AppExamAnswerResultVO();
        appExamAnswerVO.setAppExamAnswerQuestionInfoList(appExamAnswerQuestionList);
        return ResultUtils.success(appExamAnswerVO);
    }

    /**
     * 创建答卷
     * @param token
     * @return
     */
    @PostMapping("/createAppExamAnswer")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<AppExamAnswerVO> createAppExamAnswer(@RequestHeader(value = "Authorization", required = false) String token,
                                                                   @RequestBody AppExamAnswerDto appExamAnswerDto) {
        if (appExamAnswerDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null == userAppDto){
            throw new BusinessException(ErrorCode.USERINFO_ERROR);
        }
        AppExamAnswer appExamAnswer = new AppExamAnswer();
        BeanUtils.copyProperties(appExamAnswerDto, appExamAnswer);
        appExamAnswer.setUserId(userAppDto.getUserId());
        appExamAnswer.setId(IdUtil.randomUUID());
        appExamAnswer.setCreateTime(DateTime.now());
        appExamAnswer.setUpdateTime(DateTime.now());
        appExamAnswer.setCategoryId(appExamAnswerDto.getCategoryId());
        //从指定分类题库随机题
        QuestionInfoQuery query = new QuestionInfoQuery();
        query.setPageSize(appExamAnswerDto.getPageSize());
        query.setPageNo(appExamAnswerDto.getPageNo());
        query.setCategoryId(appExamAnswerDto.getCategoryId());
        List<QuestionInfo> list = questionInfoService.findListByPageRandom(query).getList();
        for (QuestionInfo questionInfo:
             list) {
            AppExamAnswerQuestion question = new AppExamAnswerQuestion();
            question.setQuestionId(questionInfo.getQuestionId());
            question.setUserId(userAppDto.getUserId());
            question.setAppExamAnswerId(appExamAnswer.getId());
            appExamAnswerQuestionService.save(question);
        }
        boolean result = appExamAnswerService.save(appExamAnswer);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        AppExamAnswerVO appExamAnswerVO = new AppExamAnswerVO();
        BeanUtils.copyProperties(appExamAnswer, appExamAnswerVO);
        appExamAnswerVO.setAppExamAnswerQuestionInfoList(list);
        return ResultUtils.success(appExamAnswerVO);
    }

    /**
     *  提交面试题答卷
     * @param token
     * @return
     */
    @GetMapping("/postAppExamAnswer")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<AppExamAnswer> postAppExamAnswer(@RequestHeader(value = "Authorization", required = false) String token,
                                                         @VerifyParam(required = true) @RequestParam String id) {
        if (StrUtil.isEmpty(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null == userAppDto){
            throw new BusinessException(ErrorCode.USERINFO_ERROR);
        }
        AppExamAnswer appExamAnswer = new AppExamAnswer();
        appExamAnswer.setUserId(userAppDto.getUserId());
        appExamAnswer.setId(id);
        appExamAnswer.setUpdateTime(DateTime.now());
        if (StrUtil.isNotEmpty(appExamAnswer.getId())) {
            QueryWrapper<AppExamAnswerQuestion> wrapper = new QueryWrapper<>();
            wrapper.eq("appExamAnswerId",appExamAnswer.getId());
            List<AppExamAnswerQuestion> list = appExamAnswerQuestionService.list(wrapper);
            //对所有已经提交的面试题统计
            double accuracyResult = 0;
            double scoreResult = 0;
            for (AppExamAnswerQuestion appExamAnswerQuestion:
                list) {
                accuracyResult += appExamAnswerQuestion.getAccuracy();
                scoreResult += appExamAnswerQuestion.getScore();
            }
            appExamAnswer.setAccuracy(Math.round((accuracyResult/list.size()) * 100F * 10.0) / 100.0);
            appExamAnswer.setScore(scoreResult/list.size());
            appExamAnswer.setStatus(1);
        }
        boolean result = appExamAnswerService.saveOrUpdate(appExamAnswer);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(appExamAnswer);
    }

    /**
     * 新增/修改面试题
     * @param token
     * @return
     */
    @PostMapping("/saveAppExamAnswerQuestion")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<AppExamAnswerQuestion> saveAppExamAnswerQuestion(@RequestHeader(value = "Authorization", required = false) String token,
                                                                               @RequestBody AppExamAnswerQuestionDto appExamAnswerQuestionDto) {
            if (appExamAnswerQuestionDto == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
            if (null == userAppDto){
                throw new BusinessException(ErrorCode.USERINFO_ERROR);
            }
            AppExamAnswerQuestion appExamAnswerQuestion = new AppExamAnswerQuestion();
            appExamAnswerQuestion.setUserId(userAppDto.getUserId());
            appExamAnswerQuestion.setCreateTime(DateTime.now());
            appExamAnswerQuestion.setUpdateTime(DateTime.now());
            BeanUtils.copyProperties(appExamAnswerQuestionDto, appExamAnswerQuestion);
            boolean result = appExamAnswerQuestionService.saveOrUpdate(appExamAnswerQuestion);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
            return ResultUtils.success(appExamAnswerQuestion);
    }

    /**
     * 提交面试题
     * @param token
     * @return
     */
    @PostMapping("/postAppExamAnswerQuestion")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<AppExamAnswerQuestion> postAppExamAnswerQuestion(@RequestHeader(value = "Authorization", required = false) String token,
                                                                         @RequestBody AppExamAnswerQuestionDto appExamAnswerQuestionDto) {
        if (appExamAnswerQuestionDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null == userAppDto){
            throw new BusinessException(ErrorCode.USERINFO_ERROR);
        }
        AppExamAnswerQuestion appExamAnswerQuestion = new AppExamAnswerQuestion();
        BeanUtils.copyProperties(appExamAnswerQuestionDto, appExamAnswerQuestion);
        if (ObjUtil.isNull(appExamAnswerQuestion.getQuestionId())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"问题ID不能为空");
        }
        appExamAnswerQuestion.setUserId(userAppDto.getUserId());
        appExamAnswerQuestion.setId(IdUtil.randomUUID());
        appExamAnswerQuestion.setAccuracy(0D);
        appExamAnswerQuestion.setScore(0D);
        appExamAnswerQuestion.setStatus(1);
        appExamAnswerQuestion.setUpdateTime(DateTime.now());
        QuestionInfo questionInfo = questionInfoService.getQuestionInfoByQuestionId(appExamAnswerQuestion.getQuestionId());
        appExamAnswerQuestion.setAnswer(questionInfo.getAnswerAnalysis());
        if (StrUtil.isNotEmpty(appExamAnswerQuestion.getUserAnswer())){
            // TODO:字符串里面包含HTML标签导致误判
            double similarityRatio = Compare2String.getSimilarityRatio(HtmlParser.getTextFromHtml(appExamAnswerQuestion.getUserAnswer()), HtmlParser.getTextFromHtml(questionInfo.getAnswerAnalysis()));
            if (StrUtil.isNotEmpty(appExamAnswerQuestionDto.getId())) {
                AppExamAnswerQuestion answerQuestion = appExamAnswerQuestionService.getById(appExamAnswerQuestionDto.getId());
                similarityRatio = answerQuestion.getAccuracy() > similarityRatio ? Math.round(answerQuestion.getAccuracy() * 100F * 100.0) / 100000.0 : similarityRatio;
            }
            appExamAnswerQuestion.setAccuracy( similarityRatio);
            appExamAnswerQuestion.setScore( similarityRatio);
            appExamAnswerQuestion.setStatus(1);
        }
        boolean result = appExamAnswerQuestionService.saveOrUpdate(appExamAnswerQuestion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(appExamAnswerQuestion);
    }

    /**
     * 删除提交面试题记录
     * @param token
     * @return
     */
    @GetMapping("/delAppExamAnswerQuestion")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Boolean> delAppExamAnswerQuestion(@RequestHeader(value = "Authorization", required = false) String token,
                                                                               String id) {
        if (StrUtil.isEmpty(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null == userAppDto){
            throw new BusinessException(ErrorCode.USERINFO_ERROR);
        }
        AppExamAnswerQuestion appExamAnswerQuestion = new AppExamAnswerQuestion();
        appExamAnswerQuestion.setId(id);
        boolean result = appExamAnswerQuestionService.removeById(appExamAnswerQuestion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    /**
     * 删除提交面试题记录
     * @param token
     * @return
     */
    @GetMapping("/delAppExamAnswer")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Boolean> delAppExamAnswer(@RequestHeader(value = "Authorization", required = false) String token,
                                                          String id) {
        if (StrUtil.isEmpty(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null == userAppDto){
            throw new BusinessException(ErrorCode.USERINFO_ERROR);
        }
        AppExamAnswer appExamAnswer = new AppExamAnswer();
        appExamAnswer.setId(id);
        boolean result = appExamAnswerService.removeById(appExamAnswer);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

}
