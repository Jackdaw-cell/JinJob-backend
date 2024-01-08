package com.jackdaw.jinjobbackendquestionservice.controller.content;

import cn.hutool.core.util.ObjUtil;
import com.jackdaw.jinjobbackendcommon.annotation.DailyCheckin;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.common.BaseResponse;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.CopyTools;
import com.jackdaw.jinjobbackendmodel.entity.dto.AppExamPostDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.myExamInfo.ExamAddRequest;
import com.jackdaw.jinjobbackendmodel.entity.po.AppExam;
import com.jackdaw.jinjobbackendmodel.entity.query.AppExamQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.AppExamQuestionQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.AppExamVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.ExamQuestionVO;
import com.jackdaw.jinjobbackendmodel.enums.AppExamStatusEnum;
import com.jackdaw.jinjobbackendquestionservice.controller.ABaseController;
import com.jackdaw.jinjobbackendquestionservice.service.AppExamService;
import com.jackdaw.jinjobbackendserviceclient.service.user.UserFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 问题 Controller
 */
@RestController("appExamController")
@RequestMapping("/appExam")
public class AppExamController extends ABaseController {
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private AppExamService appExamService;

    @GetMapping("/loadExam")
    @GlobalInterceptor
    public BaseResponse<PaginationResultVO<AppExam>> loadExam(@RequestHeader(value = "Authorization", required = false) String token,
                                        @RequestParam Integer status) {
        AppUserLoginDto appDto = getAppUserLoginInfoFromToken(token);
        if (appDto == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        AppExamQuery appExamQuery = new AppExamQuery();
        if (status != 2){
            appExamQuery.setStatus(status);
        }
        appExamQuery.setUserId(appDto.getUserId());
        appExamQuery.setOrderBy("exam_id desc");
        appExamQuery.setPageNo(1);
        appExamQuery.setPageSize(10);
        PaginationResultVO<AppExam> appExamList = appExamService.findListByPage(appExamQuery);
//        return getSuccessResponseVO(appExamList);
        return ResultUtils.success(appExamList);
    }

    @GetMapping("/loadExamByPage")
    @GlobalInterceptor
    public BaseResponse<PaginationResultVO<AppExam>> loadExamByPage(@RequestHeader(value = "Authorization", required = false) String token,
                                                @RequestParam Integer status,
                                                @RequestParam Integer pageNo,
                                                @RequestParam Integer pageSize) {
        AppUserLoginDto appDto = getAppUserLoginInfoFromToken(token);
        if (appDto == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        AppExamQuery appExamQuery = new AppExamQuery();
        appExamQuery.setStatus(status);
        appExamQuery.setUserId(appDto.getUserId());
        appExamQuery.setOrderBy("exam_id desc");
        appExamQuery.setPageNo(pageNo);
        appExamQuery.setPageSize(pageSize);

        PaginationResultVO<AppExam> appExamList = appExamService.findListByPage(appExamQuery);
//        return getSuccessResponseVO(appExamList);
        return ResultUtils.success(appExamList);
    }
    /**
     * 根据条件分页查询
     */
    @PostMapping("/createExam")
    @GlobalInterceptor(checkLogin = true)
    public  BaseResponse<AppExam> createExam(@RequestHeader(value = "Authorization", required = false) String token,
                                             @VerifyParam(required = true) @RequestBody ExamAddRequest examAddRequest) {
        AppUserLoginDto appDto = getAppUserLoginInfoFromToken(token);
        AppExam appExam = appExamService.createExam(examAddRequest.getCategoryIds(), appDto);
//        return getSuccessResponseVO(appExam);
        return ResultUtils.success(appExam);
    }

    @GetMapping("/getExamQuestion")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<AppExamVO> getExamQuestion(@RequestHeader(value = "Authorization", required = false) String token,
                                      @VerifyParam(required = true) @RequestParam Integer examId) {
        AppUserLoginDto appDto = getAppUserLoginInfoFromToken(token);
        if(ObjUtil.isNull(checkAppExam(appDto, examId))){
            throw new BusinessException(ErrorCode.CHECKEXAM_ERROR);
        };
        AppExam appExam = checkAppExam(appDto, examId);
        Boolean showAnswer = false;
        if (AppExamStatusEnum.FINISHED.getStatus().equals(appExam.getStatus())) {
            showAnswer = true;
        }
        AppExamVO appExamVO = CopyTools.copy(appExam, AppExamVO.class);
        AppExamQuestionQuery appExamQuestionQuery = new AppExamQuestionQuery();
        appExamQuestionQuery.setExamId(examId);
        appExamQuestionQuery.setUserId(appDto.getUserId());
        appExamQuestionQuery.setShowUserAnswer(showAnswer);
        List<ExamQuestionVO> examQuestionList = appExamService.getAppExamQuestion(appExamQuestionQuery);
        for (ExamQuestionVO item : examQuestionList) {
            item.setQuestion(resetContentImg(item.getQuestion()));
            item.setAnswerAnalysis(resetContentImg(item.getAnswerAnalysis()));
        }
        appExamVO.setExamQuestionList(examQuestionList);
//        return getSuccessResponseVO(appExamVO);
        return ResultUtils.success(appExamVO);
    }

    @GetMapping("/startExam")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Date> startExam(@RequestHeader(value = "Authorization", required = false) String token,
                                @VerifyParam(required = true) @RequestParam Integer examId) {
        AppUserLoginDto appDto = getAppUserLoginInfoFromToken(token);
        if(ObjUtil.isNull(checkAppExam(appDto, examId))){
            throw new BusinessException(ErrorCode.CHECKEXAM_ERROR);
        };
        Date curDate = new Date();
        AppExam appExam = new AppExam();
        appExam.setStartTime(curDate);
        AppExamQuery appExamQuery = new AppExamQuery();
        appExamQuery.setExamId(examId);
        appExamQuery.setUserId(appDto.getUserId());
        appExamService.updateByParam(appExam, appExamQuery);
//        return getSuccessResponseVO(curDate);
        return ResultUtils.success(curDate);
    }

    @GetMapping("/delExam")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Object> delExam(@RequestHeader(value = "Authorization", required = false) String token,
                                        @VerifyParam(required = true) @RequestParam Integer examId) {
        AppUserLoginDto appDto = getAppUserLoginInfoFromToken(token);
        if(ObjUtil.isNull(checkAppExam(appDto, examId))){
            throw new BusinessException(ErrorCode.CHECKEXAM_ERROR);
        };
        appExamService.delExam4Api(appDto.getUserId(), examId);
//        return getSuccessResponseVO(null);
        return ResultUtils.success(null);
    }

    @PostMapping("/postExam")
    @DailyCheckin()
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<AppExam> postExam(@RequestHeader(value = "Authorization", required = false) String token,
                                         @RequestBody AppExamPostDto appExamPostDto) {
        AppUserLoginDto appDto = getAppUserLoginInfoFromToken(token);
        AppExam appExam = appExamService.postExam(appDto, appExamPostDto);
//        return getSuccessResponseVO(appExam);
        return ResultUtils.success(appExam);
    }

    private AppExam checkAppExam(AppUserLoginDto appDto, Integer examId) {
        AppExam appExam = appExamService.getAppExamByExamId(examId);
        if (null == appExam || !appDto.getUserId().equals(appExam.getUserId())) {
//            throw new BusinessException(ErrorCode.PARAM);
            return null;
        }
        return appExam;
    }

}