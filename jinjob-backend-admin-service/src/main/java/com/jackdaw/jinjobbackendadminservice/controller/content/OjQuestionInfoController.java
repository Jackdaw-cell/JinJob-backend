package com.jackdaw.jinjobbackendadminservice.controller.content;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.OjQuestionService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion.*;
import com.jackdaw.jinjobbackendmodel.entity.po.QuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.query.SimplePage;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.enums.PageSize;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendmodel.exception.ThrowUtils;
import com.jackdaw.jinjobbackendmodel.common.BaseResponse;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendmodel.entity.dto.SessionUserAdminDto;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 编程题 Controller
 * 与其他模块不同，用mybatis-plus实现持久化
 */
@RestController("ojQuestionInfoController")
@RequestMapping("/ojQuestionInfo")
public class OjQuestionInfoController extends ABaseController {

    @Resource
    private OjQuestionService ojQuestionService;


    private final static Gson GSON = new Gson();

    /**
     * 根据条件分页查询
     */
    @PostMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_LIST)
    public ResponseVO loadDataList(OjQuestionQueryRequest ojQuestionQueryRequest) {
        QueryWrapper<OjQuestionInfo> wrapper = new QueryWrapper();
        if (ojQuestionQueryRequest.getId() != null) {
            wrapper.eq("id",ojQuestionQueryRequest.getId());
        }
        if (ojQuestionQueryRequest.getTitle() != null) {
            wrapper.eq("title",ojQuestionQueryRequest.getTitle());
        }
        if (ojQuestionQueryRequest.getContent() != null) {
            wrapper.like("content",ojQuestionQueryRequest.getContent());
        }
        if (ojQuestionQueryRequest.getAnswer() != null) {
            wrapper.like("answer",ojQuestionQueryRequest.getAnswer());
        }

        int count = (int)ojQuestionService.count(wrapper);
        int pageSize = ojQuestionQueryRequest.getPageSize() == null ? PageSize.SIZE15.getSize() :(int) ojQuestionQueryRequest.getPageSize();
        int pageNo = ojQuestionQueryRequest.getPageNo()== null ? 0 :(int)ojQuestionQueryRequest.getPageNo();
        SimplePage page = new SimplePage(pageNo, count, pageSize);
        Page<OjQuestionInfo> questionPage = ojQuestionService.page(new Page<>(pageNo, pageSize),
                ojQuestionService.getQueryWrapper(ojQuestionQueryRequest));
        PaginationResultVO<QuestionInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), questionPage.getRecords());
        return getSuccessResponseVO(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @PostMapping("/loadOjQuestionItem")
    public ResponseVO getQuestionById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OjQuestionInfo ojQuestionInfo = ojQuestionService.getById(id);
        if (ojQuestionInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return getSuccessResponseVO(ojQuestionInfo);
    }

    /**
     * 新增/修改
     */
    @PostMapping("/saveOjQuestionInfo")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_EDIT)
    public ResponseVO saveQuestionInfo(HttpSession session,
                                       @RequestBody OjQuestionRequest ojQuestionRequest) {
        if (ojQuestionRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OjQuestionInfo ojQuestionInfo = new OjQuestionInfo();
        BeanUtils.copyProperties(ojQuestionRequest, ojQuestionInfo);
        List<String> tags = ojQuestionRequest.getTags();
        if (tags != null) {
            ojQuestionInfo.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = ojQuestionRequest.getJudgeCase();
        if (judgeCase != null) {
            ojQuestionInfo.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = ojQuestionRequest.getJudgeConfig();
        if (judgeConfig != null) {
            ojQuestionInfo.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        ojQuestionService.validQuestion(ojQuestionInfo, true);
        SessionUserAdminDto userAdminDto = getUserAdminFromSession(session);
        ojQuestionInfo.setUserId(userAdminDto.getUserId().toString());
        ojQuestionInfo.setMainMethod(ojQuestionRequest.getMainMethod());
        ojQuestionInfo.setSubmitMethod(ojQuestionInfo.getSubmitMethod());
        if (ojQuestionInfo.getDescript() != null) {
            ojQuestionInfo.setDescript(ojQuestionInfo.getDescript());
        }
        boolean result = ojQuestionService.saveOrUpdate(ojQuestionInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = ojQuestionInfo.getId();
        return getSuccessResponseVO(newQuestionId);
    }

    @PostMapping("/delOjQuestionInfo")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_DEL)
    public ResponseVO delQuestion(HttpSession session,  Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        OjQuestionInfo oldOjQuestionInfo = ojQuestionService.getById(id);
        ThrowUtils.throwIf(oldOjQuestionInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean b = ojQuestionService.removeById(id);
        return getSuccessResponseVO(b);
    }

    @PostMapping("/delOjQuestionInfoBatch")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_DEL_BATCH)
    public ResponseVO delQuestionBatch(HttpSession session, String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))  // 先将字符串按指定分隔符切分
                .map(Long::valueOf)      // 再将每个子字符串转换为Long类型
                .collect(Collectors.toList());  // 最后将转换后的Long值收集到List中
        if (idList == null || idList.size() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        List<Long> ids = deleteRequest.getIds();
        // 判断是否存在
        for (long id:
                idList) {
            OjQuestionInfo oldOjQuestionInfo = ojQuestionService.getById(id);
            ThrowUtils.throwIf(oldOjQuestionInfo == null, ErrorCode.NOT_FOUND_ERROR);
        }
        boolean b = ojQuestionService.removeBatchByIds(idList);
        return getSuccessResponseVO(b);
    }

//    发布题目
//    @PostMapping("/postQuestion")
//    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_POST)
//    public ResponseVO postQuestion(@VerifyParam(required = true) String questionIds) {
//        updateStatus(questionIds, PostStatusEnum.POST.getStatus());
//        return getSuccessResponseVO(null);
//    }
//
//    取消发布
//    @PostMapping("/cancelPostQuestion")
//    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_POST)
//    public ResponseVO cancelPostQuestion(@VerifyParam(required = true) String questionIds) {
//        updateStatus(questionIds, PostStatusEnum.NO_POST.getStatus());
//        return getSuccessResponseVO(null);
//    }

//    private void updateStatus(String questionIds, Integer status) {
//        QuestionInfoQuery params = new QuestionInfoQuery();
//        params.setQuestionIds(questionIds.split(","));
//        QuestionInfo question = new QuestionInfo();
//        question.setStatus(status);
//        questionInfoService.updateByParam(question, params);
//    }

//    @PostMapping("/importQuestion")
//    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_IMPORT)
//    public ResponseVO importQuestion(HttpSession session, MultipartFile file) {
//        SessionUserAdminDto userAdminDto = getUserAdminFromSession(session);
//        List<ImportErrorItem> errorList = questionInfoService.importQuestion(file, userAdminDto);
//        return getSuccessResponseVO(errorList);
//    }

//    @PostMapping("/showQuestionDetailNext")
//    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_LIST)
//    public ResponseVO showQuestionDetailNext(QuestionInfoQuery query,
//                                             @VerifyParam(required = true) Integer currentId, Integer nextType) {
//        QuestionInfo questionInfo = questionInfoService.showDetailNext(query, nextType, currentId, false);
//        return getSuccessResponseVO(questionInfo);
//    }
}