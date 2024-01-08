package com.jackdaw.jinjobbackenduserservice.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendcommon.utils.JWTUtil;
import com.jackdaw.jinjobbackendmodel.common.BaseResponse;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendcommon.config.AppConfig;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserInfoDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.myExamInfo.ExamUserInfoDto;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.CopyTools;
import com.jackdaw.jinjobbackendcommon.utils.ScaleFilter;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.constant.Constants;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import com.jackdaw.jinjobbackendmodel.entity.po.*;
import com.jackdaw.jinjobbackendmodel.entity.query.*;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.AppUserInfoVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.ExamQuestionVO;
import com.jackdaw.jinjobbackendmodel.enums.AnswerResultEnum;
import com.jackdaw.jinjobbackendmodel.enums.CollectTypeEnum;
import com.jackdaw.jinjobbackendmodel.enums.RequestFrequencyTypeEnum;
import com.jackdaw.jinjobbackendserviceclient.service.question.*;
import com.jackdaw.jinjobbackenduserservice.service.AppUserCollectService;
import com.jackdaw.jinjobbackenduserservice.service.AppUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController("myController")
@RequestMapping("/my")
public class MyController extends ABaseController {
    @Resource
    private AppUserCollectService appUserCollectService;

    @Resource
    private AppConfig appConfig;

    @Resource
    private AppUserInfoService appUserInfoService;

    @Resource
    private JWTUtil<AppUserLoginDto> jwtUtil;

    @Resource
    private QuestionFeignClient questionFeignClient;


    @GetMapping("/loadCollect")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<PaginationResultVO> loadCollect(@RequestHeader(value = "Authorization", required = false) String token,
                                                        @VerifyParam(required = true) @RequestParam Integer collectType) {
        AppUserCollectQuery query = new AppUserCollectQuery();
        query.setCollectType(collectType);
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        query.setOrderBy("collect_id desc");
        query.setUserId(userAppDto.getUserId());
        PaginationResultVO resultVO = appUserCollectService.findListByPage(query);
        List<AppUserCollect> appUserCollectList = resultVO.getList();
        List<String> objectIdList = appUserCollectList.stream().map(item -> item.getObjectId()).collect(Collectors.toList());
        if (objectIdList.isEmpty()) {
//            return getSuccessResponseVO(resultVO);
            return ResultUtils.success(resultVO);
        }
        Map<Integer, AppUserCollect> objectIdMap = appUserCollectList.stream().collect(Collectors.toMap(item ->
                Integer.parseInt(item.getObjectId()), Function.identity(), (data1, data2) -> data2));

        CollectTypeEnum collectTypeEnum = CollectTypeEnum.getByType(query.getCollectType());
        switch (collectTypeEnum) {
            case SHARE:
                query.setCollectType(null);
                ShareInfoQuery shareInfoQuery = new ShareInfoQuery();
                shareInfoQuery.setShareIds(objectIdList.toArray(new String[objectIdList.size()]));
                shareInfoQuery.setOrderBy("field(share_id," + StringUtils.join(objectIdList, ",") + ")");
                List<ShareInfo> shareInfoList = questionFeignClient.findListByParam(shareInfoQuery);
                for (ShareInfo item : shareInfoList) {
                    AppUserCollect collect = objectIdMap.get(item.getShareId());
                    item.setCollectId(collect.getCollectId());
                }
                resultVO.setList(shareInfoList);
                break;
            case QUESTION:
                QuestionInfoQuery questionInfoQuery = new QuestionInfoQuery();
                questionInfoQuery.setQuestionIds(objectIdList.toArray(new String[objectIdList.size()]));
                questionInfoQuery.setOrderBy("field(question_id," + StringUtils.join(objectIdList, ",") + ")");
                List<QuestionInfo> questionInfoList = questionFeignClient.findListByParam(questionInfoQuery);
                for (QuestionInfo item : questionInfoList) {
                    AppUserCollect collect = objectIdMap.get(item.getQuestionId());
                    item.setCollectId(collect.getCollectId());
                }
                resultVO.setList(questionInfoList);
                break;
            case EXAM:
                AppExamQuestionQuery appExamQuestionQuery = new AppExamQuestionQuery();
                appExamQuestionQuery.setUserId(userAppDto.getUserId());
                appExamQuestionQuery.setShowUserAnswer(true);
                appExamQuestionQuery.setQuestionIds(objectIdList);
                List<ExamQuestionVO> examQuestionList = questionFeignClient.getAppExamQuestion(appExamQuestionQuery);
                resultVO.setList(examQuestionList);
        }
//        return getSuccessResponseVO(resultVO);
        return ResultUtils.success(resultVO);
    }

    @PostMapping("/getCollectNext")
    @GlobalInterceptor
    public BaseResponse<? extends Object> getCollectNext(
            @RequestHeader(value = "Authorization", required = false) String token,
            @VerifyParam(required = true) @RequestParam Integer currentId,
            @VerifyParam(required = true) @RequestParam Integer collectType,
            Integer nextType) {
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        AppUserCollectQuery query = new AppUserCollectQuery();
        query.setUserId(userAppDto.getUserId());
        query.setCollectType(collectType);
        AppUserCollect appUserCollect = appUserCollectService.showDetailNext(query, nextType, currentId);

        CollectTypeEnum collectTypeEnum = CollectTypeEnum.getByType(collectType);
        Integer objectId = Integer.parseInt(appUserCollect.getObjectId());
        switch (collectTypeEnum) {
            case SHARE:
                ShareInfo shareInfo = questionFeignClient.getShareInfoByShareId(objectId);
                shareInfo.setCollectId(appUserCollect.getCollectId());
//                return getSuccessResponseVO(shareInfo);
                return ResultUtils.success(shareInfo);
            case QUESTION:
                QuestionInfo questionInfo = questionFeignClient.getQuestionInfoByQuestionId(objectId);
                questionInfo.setCollectId(appUserCollect.getCollectId());
//                return getSuccessResponseVO(questionInfo);
                return ResultUtils.success(questionInfo);
        }

//        return getSuccessResponseVO(null);
        return ResultUtils.success(null);
    }

    @PostMapping("/loadMyExam")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<PaginationResultVO> loadMyExam(@RequestHeader(value = "Authorization", required = false) String token,
                                                       @RequestParam Integer pageNo) {
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        AppExamQuery appExamQuery = new AppExamQuery();
        appExamQuery.setPageNo(pageNo);
        appExamQuery.setUserId(userAppDto.getUserId());
        appExamQuery.setOrderBy("exam_id desc");
        PaginationResultVO resultVO = questionFeignClient.findListByPage(appExamQuery);
//        return getSuccessResponseVO(resultVO);
        return ResultUtils.success(resultVO);
    }

    @GetMapping("/loadWrongQuestion")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<PaginationResultVO> loadWrongQuestion(@RequestHeader(value = "Authorization", required = false) String token,
                                                              @RequestParam  Integer pageNo) {
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        AppExamQuestionQuery appExamQuestionQuery = new AppExamQuestionQuery();
        appExamQuestionQuery.setPageNo(pageNo);
        appExamQuestionQuery.setOrderBy("exam_id desc");
        appExamQuestionQuery.setUserId(userAppDto.getUserId());
        appExamQuestionQuery.setAnswerResult(AnswerResultEnum.WRONG.getResult());
        PaginationResultVO resultVO = questionFeignClient.findListByPage(appExamQuestionQuery);
        List<AppExamQuestion> appExamQuestionList = resultVO.getList();

        List<String> questionIds = appExamQuestionList.stream().map(item -> item.getQuestionId().toString()).collect(Collectors.toList());
        if (questionIds.isEmpty()) {
//            return getSuccessResponseVO(resultVO);
            return ResultUtils.success(resultVO);
        }
        appExamQuestionQuery = new AppExamQuestionQuery();
        appExamQuestionQuery.setShowUserAnswer(true);
        appExamQuestionQuery.setQuestionIds(questionIds);
        appExamQuestionQuery.setAnswerResult(AnswerResultEnum.WRONG.getResult());
        List<ExamQuestionVO> examQuestionList = questionFeignClient.getAppExamQuestion(appExamQuestionQuery);
        for (ExamQuestionVO item : examQuestionList) {
            item.setQuestion(resetContentImg(item.getQuestionAnswer()));
            item.setAnswerAnalysis(resetContentImg(item.getAnswerAnalysis()));
        }
        resultVO.setList(examQuestionList);
//        return getSuccessResponseVO(resultVO);
        return ResultUtils.success(resultVO);
    }

    @PostMapping("/uploadAvatar")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Object> uploadAvatar(@RequestHeader(value = "Authorization", required = false) String token,
                                             MultipartFile file) throws IOException {
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        String folderName = appConfig.getProjectFolder() + Constants.FOLDER_AVATAR;
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String avatarName = userAppDto.getUserId() + StringTools.getFileSuffix(file.getOriginalFilename());
        File avatarFileName = new File(folder.getPath() + "/" + avatarName);
        file.transferTo(avatarFileName);
        //生成缩略图
        ScaleFilter.createThumbnail(avatarFileName, Constants.WIDTH_70, Constants.WIDTH_70, avatarFileName);

        AppUserInfo appUserInfo = new AppUserInfo();
        appUserInfo.setAvatar(Constants.FOLDER_AVATAR + avatarName);
        appUserInfoService.updateAppUserInfoByUserId(appUserInfo, userAppDto.getUserId());
//        return getSuccessResponseVO(null);
        return ResultUtils.success(null);
    }


    @PostMapping("/updateUserInfo")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Object> updateUserInfo(@RequestHeader(value = "Authorization", required = false) String token,
                                               @RequestBody AppUserInfo submitAppUserInfo) {
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        AppUserInfo appUserInfo = new AppUserInfo();
        appUserInfo.setUserId(submitAppUserInfo.getUserId());
        appUserInfo.setSex(submitAppUserInfo.getSex());
        appUserInfo.setAvatar(submitAppUserInfo.getAvatar());
        appUserInfo.setDescription(submitAppUserInfo.getDescription());
        appUserInfo.setTags(submitAppUserInfo.getTags());
        appUserInfo.setEmail(submitAppUserInfo.getEmail());
        appUserInfo.setNickName(submitAppUserInfo.getNickName());
        appUserInfo.setBackground(submitAppUserInfo.getBackground());
        if (!StringTools.isEmpty(submitAppUserInfo.getPassword())) {
            appUserInfo.setPassword(StringTools.encodeByMD5(submitAppUserInfo.getPassword()));
        }
         appUserInfoService.updateAppUserInfoByUserId(appUserInfo, userAppDto.getUserId());
        AppUserLoginDto appUserInfoDto = userAppDto;
        BeanUtil.copyProperties(appUserInfo,appUserInfoDto);
        String newToken = jwtUtil.createToken(Constants.JWT_KEY_LOGIN_TOKEN, appUserInfoDto, Constants.JWT_TOKEN_EXPIRES_DAYS);
        return ResultUtils.success(newToken);
    }

    @GetMapping("/getUserInfo")
    @GlobalInterceptor
    public BaseResponse<? extends Object> getUserInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null == userAppDto) {
            throw new BusinessException(ErrorCode.TOKEN_ERROR);
        }
        AppUserInfo appUserInfo = appUserInfoService.getAppUserInfoByUserId(userAppDto.getUserId());
        if (appUserInfo == null) {
//            return getSuccessResponseVO(null);
            throw new BusinessException(ErrorCode.USERINFO_ERROR);
        }
//        return getSuccessResponseVO(CopyTools.copy(appUserInfo, AppUserInfoVO.class));
        return ResultUtils.success(CopyTools.copy(appUserInfo, AppUserInfoVO.class));
    }


    @GetMapping("/loadFeedback")
    @GlobalInterceptor
    public BaseResponse<PaginationResultVO> loadFeedback(@RequestHeader(value = "Authorization", required = false) String token,
                                                         @RequestParam Integer pageNo,
                                                         @VerifyParam(required = true) @RequestParam Integer pFeedbackId) {
        AppFeedbackQuery appFeedbackQuery = new AppFeedbackQuery();
        appFeedbackQuery.setOrderBy("feedback_id desc");
        appFeedbackQuery.setUserId(getAppUserLoginInfoFromToken(token).getUserId());
        appFeedbackQuery.setPageNo(pageNo);
        appFeedbackQuery.setPFeedbackId(0);
        appFeedbackQuery.setPFeedbackId(pFeedbackId);
        PaginationResultVO resultVO = questionFeignClient.findListByPage(appFeedbackQuery);
//        return getSuccessResponseVO(resultVO);
        return ResultUtils.success(resultVO);
    }

    @GetMapping("/loadFeedbackReply")
    @GlobalInterceptor
    public BaseResponse<List<AppFeedback>> loadFeedbackReply(@RequestHeader(value = "Authorization", required = false) String token,
                                                             @RequestParam Integer pFeedbackId) {
        AppFeedbackQuery query = new AppFeedbackQuery();
        query.setPFeedbackId(pFeedbackId);
        query.setUserId(getAppUserLoginInfoFromToken(token).getUserId());
        query.setOrderBy("feedback_id asc");
        List<AppFeedback> list = questionFeignClient.findListByParam(query);
//        return getSuccessResponseVO(list);
        return ResultUtils.success(list);
    }

    //修改评论
    @PostMapping("/sendFeedback")
    @GlobalInterceptor(frequencyType = RequestFrequencyTypeEnum.DAY, reqeustFrequencyThreshold = 10)
    public BaseResponse<AppFeedback> sendFeedback(@RequestHeader(value = "Authorization", required = false) String token,
                                                  @VerifyParam(required = true, max = 500) @RequestParam("content") String content,
                                                  @RequestParam("pFeedbackId") Integer pFeedbackId) {
        AppUserLoginDto appUserLoginDto = getAppUserLoginInfoFromToken(token);
        AppFeedback appFeedback = new AppFeedback();
        appFeedback.setUserId(appUserLoginDto.getUserId());
        appFeedback.setNickName(appUserLoginDto.getNickName());
        appFeedback.setContent(content);
        appFeedback.setPFeedbackId(pFeedbackId);
        if(questionFeignClient.saveFeedBack4Client(appFeedback)>0){
            return ResultUtils.success(appFeedback);
        }
        return ResultUtils.error(null);
    }

    //回复评论
    @PostMapping("/replyFeedback")
    @GlobalInterceptor(frequencyType = RequestFrequencyTypeEnum.DAY, reqeustFrequencyThreshold = 10)
    public BaseResponse<AppFeedback> replyFeedback(@RequestHeader(value = "Authorization", required = false) String token,
                                                  @VerifyParam(required = true, max = 500) @RequestParam String content,
                                                  @VerifyParam(required = true) @RequestParam Integer pFeedbackId) {
        AppUserLoginDto appUserLoginDto = getAppUserLoginInfoFromToken(token);
        AppFeedback appFeedback = new AppFeedback();
        appFeedback.setUserId(appUserLoginDto.getUserId());
        appFeedback.setNickName(appUserLoginDto.getNickName());
        appFeedback.setContent(content);
        appFeedback.setPFeedbackId(pFeedbackId);
        if(questionFeignClient.replyFeedback(appFeedback)>0){
            return ResultUtils.success(appFeedback);
        }
        return ResultUtils.error(null);
    }

    //删除评论
    @GetMapping("/deleteAppFeedbackByFeedbackId")
    @GlobalInterceptor(frequencyType = RequestFrequencyTypeEnum.DAY, reqeustFrequencyThreshold = 10)
    public BaseResponse<Integer> deleteAppFeedbackByFeedbackId(@RequestHeader(value = "Authorization", required = false) String token,
                                                          @RequestParam("feedbackId") Integer feedbackId) {
        if(questionFeignClient.deleteAppFeedbackByFeedbackId(feedbackId)>0){
            return ResultUtils.success(feedbackId);
        }
        return ResultUtils.error(null);
    }

    @GetMapping("/getCheckinInfo")
    @GlobalInterceptor(frequencyType = RequestFrequencyTypeEnum.DAY, reqeustFrequencyThreshold = 10)
    public BaseResponse<String> sendFeedback(@RequestHeader(value = "Authorization", required = false) String token) {
        AppUserLoginDto appUserLoginDto = getAppUserLoginInfoFromToken(token);
        return ResultUtils.success(appUserInfoService.getCheckinInfo(appUserLoginDto.getUserId()));
    }

    @GetMapping("/getUserList")
    @GlobalInterceptor
    public BaseResponse<PaginationResultVO<AppUserInfoDto>> getUserList(HttpServletRequest request,
                                                                        @RequestHeader(value = "Authorization", required = true) String token,
                                                                        @RequestBody AppUserInfoQuery appUserInfoQuery) {
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        PaginationResultVO<AppUserInfoDto> listByPage = appUserInfoService.findDtoListByPage(appUserInfoQuery);
        if (ObjUtil.isNotNull(userAppDto)){
            return ResultUtils.success(listByPage);
        }
        return ResultUtils.error(null);
    }

    @GetMapping("/getUserExamInfo")
    @GlobalInterceptor
    public BaseResponse<ExamUserInfoDto> getUserExamInfo(HttpServletRequest request,@RequestHeader(value = "Authorization", required = true) String token
    ) {
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        AppUserInfo userInfo = appUserInfoService.getAppUserInfoByUserId(userAppDto.getUserId());
        ExamUserInfoDto userInfoDto = new ExamUserInfoDto();
        userInfoDto.setExamAcScore(userInfo.getExamAcScore());
        userInfoDto.setExamAcSum(userInfo.getExamAcSum());
        userInfoDto.setOjAcSum(userInfo.getOjAcSum());
        if (ObjUtil.isNotNull(userInfo)){
            return ResultUtils.success(userInfoDto);
        }
        return ResultUtils.error(null);
    }
}
