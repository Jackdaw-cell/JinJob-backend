package com.jackdaw.jinjobbackendquestionservice.controller.content;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.jackdaw.jinjobbackendcommon.annotation.DailyCheckin;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendmodel.common.BaseResponse;
import com.jackdaw.jinjobbackendmodel.common.DeleteRequest;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendmodel.exception.ThrowUtils;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion.*;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit.OjQuestionSubmitAddRequest;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit.OjQuestionSubmitQueryRequest;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionSubmit;
import com.jackdaw.jinjobbackendmodel.entity.vo.OjQuestionVO;
import com.jackdaw.jinjobbackendquestionservice.controller.ABaseController;
import com.jackdaw.jinjobbackendquestionservice.service.OjQuestionService;
import com.jackdaw.jinjobbackendquestionservice.service.OjQuestionSubmitService;
import com.jackdaw.jinjobbackendserviceclient.service.user.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题目接口
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
@RestController
@RequestMapping("/")
@Slf4j
public class OJQuestionController extends ABaseController {

    @Resource
    private OjQuestionService ojQuestionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private OjQuestionSubmitService ojQuestionSubmitService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param ojQuestionAddRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody OjQuestionAddRequest ojQuestionAddRequest,
                                          @RequestHeader(value = "Authorization", required = false) String token) {
        if (ojQuestionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OjQuestionInfo ojQuestionInfo = new OjQuestionInfo();
        BeanUtils.copyProperties(ojQuestionAddRequest, ojQuestionInfo);
        List<String> tags = ojQuestionAddRequest.getTags();
        if (tags != null) {
            ojQuestionInfo.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = ojQuestionAddRequest.getJudgeCase();
        if (judgeCase != null) {
            ojQuestionInfo.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = ojQuestionAddRequest.getJudgeConfig();
        if (judgeConfig != null) {
            ojQuestionInfo.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        ojQuestionService.validQuestion(ojQuestionInfo, true);
//        User loginUser = userFeignClient.getLoginUser(request);
        AppUserLoginDto loginUser = getAppUserLoginInfoFromToken(token);
        ojQuestionInfo.setUserId(loginUser.getUserId());
        ojQuestionInfo.setMainMethod(ojQuestionAddRequest.getMainMethod());
        ojQuestionInfo.setSubmitMethod(ojQuestionInfo.getSubmitMethod());
        if (ojQuestionInfo.getDescript() != null) {
            ojQuestionInfo.setDescript(ojQuestionInfo.getDescript());
        }
        boolean result = ojQuestionService.save(ojQuestionInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = ojQuestionInfo.getId();
        return ResultUtils.success(newQuestionId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest,
                                                @RequestHeader(value = "Authorization", required = false) String token) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        User user = userFeignClient.getLoginUser(request);
        AppUserLoginDto loginUser = getAppUserLoginInfoFromToken(token);
        long id = deleteRequest.getId();
        // 判断是否存在
        OjQuestionInfo oldOjQuestionInfo = ojQuestionService.getById(id);
        ThrowUtils.throwIf(oldOjQuestionInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
//        if (!oldOjQuestion.getUserId().equals(loginUser.getUserId())) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        boolean b = ojQuestionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param ojQuestionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Boolean> updateQuestion(@RequestBody OjQuestionUpdateRequest ojQuestionUpdateRequest) {
        if (ojQuestionUpdateRequest == null || ojQuestionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OjQuestionInfo ojQuestionInfo = new OjQuestionInfo();
        BeanUtils.copyProperties(ojQuestionUpdateRequest, ojQuestionInfo);
        List<String> tags = ojQuestionUpdateRequest.getTags();
        if (tags != null) {
            ojQuestionInfo.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = ojQuestionUpdateRequest.getJudgeCase();
        if (judgeCase != null) {
            ojQuestionInfo.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = ojQuestionUpdateRequest.getJudgeConfig();
        if (judgeConfig != null) {
            ojQuestionInfo.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        if (ojQuestionUpdateRequest.getMainMethod() != null) {
            ojQuestionInfo.setMainMethod(ojQuestionUpdateRequest.getMainMethod());
        }
        if (ojQuestionUpdateRequest.getSubmitMethod() != null) {
            ojQuestionInfo.setSubmitMethod(ojQuestionUpdateRequest.getSubmitMethod());
        }
        if (ojQuestionUpdateRequest.getDescript() != null) {
            ojQuestionInfo.setDescript(ojQuestionUpdateRequest.getDescript());
        }
        // 参数校验
        ojQuestionService.validQuestion(ojQuestionInfo, false);
        long id = ojQuestionUpdateRequest.getId();
        // 判断是否存在
        OjQuestionInfo oldOjQuestionInfo = ojQuestionService.getById(id);
        ThrowUtils.throwIf(oldOjQuestionInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = ojQuestionService.updateById(ojQuestionInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<OjQuestionInfo> getQuestionById(long id,
                                                        @RequestHeader(value = "Authorization", required = false) String token) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OjQuestionInfo ojQuestionInfo = ojQuestionService.getById(id);
        if (ojQuestionInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
//        User loginUser = userFeignClient.getLoginUser(request);
        AppUserLoginDto loginUser = getAppUserLoginInfoFromToken(token);
        // 不是本人或管理员，不能直接获取所有信息
//        if (!ojQuestion.getUserId().equals(loginUser.getUserId())) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        return ResultUtils.success(ojQuestionInfo);
    }

    /**
     * 根据 id 获取（脱敏）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<OjQuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OjQuestionInfo ojQuestionInfo = ojQuestionService.getById(id);
        if (ojQuestionInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(ojQuestionService.getQuestionVO(ojQuestionInfo, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param ojQuestionQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Page<OjQuestionInfo>> listQuestionVOByPage(@RequestBody OjQuestionQueryRequest ojQuestionQueryRequest,
                                                                   @RequestHeader(value = "Authorization", required = false) String token) {
        long current = ojQuestionQueryRequest.getPageNo();
        long size = ojQuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<OjQuestionInfo> questionPage = ojQuestionService.page(new Page<>(current, size),
                ojQuestionService.getQueryWrapper(ojQuestionQueryRequest));
//        return ResultUtils.success(ojQuestionService.getQuestionVOPage(questionPage));
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param ojQuestionQueryRequest
     * @return
     */
    @PostMapping("/my/list/page/vo")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Page<OjQuestionInfo>> listMyQuestionVOByPage(@RequestBody OjQuestionQueryRequest ojQuestionQueryRequest,
                                                                     @RequestHeader(value = "Authorization", required = false) String token) {
        if (ojQuestionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        User loginUser = userFeignClient.getLoginUser(request);
        AppUserLoginDto loginUser = getAppUserLoginInfoFromToken(token);
        ojQuestionQueryRequest.setUserId(loginUser.getUserId());
        long current = ojQuestionQueryRequest.getPageNo();
        long size = ojQuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<OjQuestionInfo> questionPage = ojQuestionService.page(new Page<>(current, size),
                ojQuestionService.getQueryWrapper(ojQuestionQueryRequest));
//        return ResultUtils.success(ojQuestionService.getQuestionVOPage(questionPage));
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取题目列表（仅管理员）
     *
     * @param ojQuestionQueryRequest
     * @return
     */
//    @PostMapping("/list/page")
//    @GlobalInterceptor(checkLogin = true)
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Page<OjQuestionInfo>> listQuestionByPage(@RequestBody OjQuestionQueryRequest ojQuestionQueryRequest,
//                                                                 @RequestHeader(value = "Authorization", required = false) String token) {
//        long current = ojQuestionQueryRequest.getCurrent();
//        long size = ojQuestionQueryRequest.getPageSize();
//        Page<OjQuestionInfo> questionPage = ojQuestionService.page(new Page<>(current, size),
//                ojQuestionService.getQueryWrapper(ojQuestionQueryRequest));
//        return ResultUtils.success(questionPage);
//    }

    // endregion

    /**
     * 编辑（用户）
     *
     * @param ojQuestionEditRequest
     * @return
     */
    @PostMapping("/edit")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Boolean> editQuestion(@RequestBody OjQuestionEditRequest ojQuestionEditRequest,
                                              @RequestHeader(value = "Authorization", required = false) String token) {
        if (ojQuestionEditRequest == null || ojQuestionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OjQuestionInfo ojQuestionInfo = new OjQuestionInfo();
        BeanUtils.copyProperties(ojQuestionEditRequest, ojQuestionInfo);
        List<String> tags = ojQuestionEditRequest.getTags();
        if (tags != null) {
            ojQuestionInfo.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = ojQuestionEditRequest.getJudgeCase();
        if (judgeCase != null) {
            ojQuestionInfo.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = ojQuestionEditRequest.getJudgeConfig();
        if (judgeConfig != null) {
            ojQuestionInfo.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        if (ojQuestionEditRequest.getMainMethod() != null) {
            ojQuestionInfo.setMainMethod(ojQuestionEditRequest.getMainMethod());
        }
        if (ojQuestionEditRequest.getSubmitMethod() != null) {
            ojQuestionInfo.setSubmitMethod(ojQuestionEditRequest.getSubmitMethod());
        }
        if (ojQuestionEditRequest.getDescript() != null) {
            ojQuestionInfo.setDescript(ojQuestionEditRequest.getDescript());
        }
        // 参数校验
        ojQuestionService.validQuestion(ojQuestionInfo, false);
        AppUserLoginDto loginUser = getAppUserLoginInfoFromToken(token);
//        User loginUser = userFeignClient.getLoginUser(request);
        long id = ojQuestionEditRequest.getId();
        // 判断是否存在
        OjQuestionInfo oldOjQuestionInfo = ojQuestionService.getById(id);
        ThrowUtils.throwIf(oldOjQuestionInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
//        if (!oldOjQuestion.getUserId().equals(loginUser.getUserId())) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        boolean result = ojQuestionService.updateById(ojQuestionInfo);
        return ResultUtils.success(result);
    }

    /**
     * 提交题目
     *
     * @param ojQuestionSubmitAddRequest
     * @return 提交记录的 id
     */
    @PostMapping("/question_submit/do")
    @DailyCheckin()
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<OjQuestionSubmit> doQuestionSubmit(@RequestBody OjQuestionSubmitAddRequest ojQuestionSubmitAddRequest,
                                                           @RequestHeader(value = "Authorization", required = false) String token) {
        if (ojQuestionSubmitAddRequest == null || ojQuestionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        final User loginUser = userFeignClient.getLoginUser(request);
        AppUserLoginDto loginUser = getAppUserLoginInfoFromToken(token);
        OjQuestionSubmit ojQuestionSubmitId = ojQuestionSubmitService.doQuestionSubmit(ojQuestionSubmitAddRequest, loginUser.getUserId());
        return ResultUtils.success(ojQuestionSubmitId);
    }

    /**
     * 分页获取题目提交列表
     *
     * @param ojQuestionSubmitQueryRequest
     * @return
     */
    @PostMapping("/question_submit/list/page")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Page<OjQuestionSubmit>> listQuestionSubmitByPage(@RequestBody OjQuestionSubmitQueryRequest ojQuestionSubmitQueryRequest,
                                                                         @RequestHeader(value = "Authorization", required = false) String token) {
        long current = ojQuestionSubmitQueryRequest.getPageNo();
        long size = ojQuestionSubmitQueryRequest.getPageSize();
        // 从数据库中查询原始的题目提交分页信息
        Page<OjQuestionSubmit> questionSubmitPage = ojQuestionSubmitService.page(new Page<>(current, size),
                ojQuestionSubmitService.getQueryWrapper(ojQuestionSubmitQueryRequest));
        AppUserLoginDto loginUser = getAppUserLoginInfoFromToken(token);
        return ResultUtils.success(questionSubmitPage);
    }



}
