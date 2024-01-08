package com.jackdaw.jinjobbackendadminservice.controller.content;

import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.QuestionInfoService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.entity.dto.ImportErrorItem;
import com.jackdaw.jinjobbackendmodel.entity.dto.SessionUserAdminDto;
import com.jackdaw.jinjobbackendmodel.entity.po.QuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.query.QuestionInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import com.jackdaw.jinjobbackendmodel.enums.PostStatusEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 问题 Controller
 */
@RestController("questionInfoController")
@RequestMapping("/questionInfo")
public class QuestionInfoController extends ABaseController {

    @Resource
    private QuestionInfoService questionInfoService;

    /**
     * 根据条件分页查询
     */
    @PostMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_LIST)
    public ResponseVO loadDataList(QuestionInfoQuery query) {
        query.setOrderBy("question_id desc");
        query.setQueryTextContent(true);
        return getSuccessResponseVO(questionInfoService.findListByPage(query));
    }

    /**
     * 新增
     */
    @PostMapping("/saveQuestionInfo")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_EDIT)
    public ResponseVO saveQuestionInfo(HttpSession session, QuestionInfo bean) {
        SessionUserAdminDto userAdminDto = getUserAdminFromSession(session);
        bean.setCreateUserId(String.valueOf(userAdminDto.getUserId()));
        bean.setCreateUserName(userAdminDto.getUserName());
        questionInfoService.saveQuestion(bean, userAdminDto.getSuperAdmin());
        return getSuccessResponseVO(null);
    }

    @PostMapping("/delQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_DEL)
    public ResponseVO delQuestion(HttpSession session, @VerifyParam(required = true) Integer questionId) {
        SessionUserAdminDto userAdminDto = getUserAdminFromSession(session);
        questionInfoService.delQuestionBatch(String.valueOf(questionId), userAdminDto.getSuperAdmin() ? null : userAdminDto.getUserId());
        return getSuccessResponseVO(null);
    }

    @PostMapping("/delQuestionBatch")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_DEL_BATCH)
    public ResponseVO delQuestionBatch(String questionIds) {
        questionInfoService.delQuestionBatch(questionIds, null);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/postQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_POST)
    public ResponseVO postQuestion(@VerifyParam(required = true) String questionIds) {
        updateStatus(questionIds, PostStatusEnum.POST.getStatus());
        return getSuccessResponseVO(null);
    }

    @PostMapping("/cancelPostQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_POST)
    public ResponseVO cancelPostQuestion(@VerifyParam(required = true) String questionIds) {
        updateStatus(questionIds, PostStatusEnum.NO_POST.getStatus());
        return getSuccessResponseVO(null);
    }

    private void updateStatus(String questionIds, Integer status) {
        QuestionInfoQuery params = new QuestionInfoQuery();
        params.setQuestionIds(questionIds.split(","));
        QuestionInfo question = new QuestionInfo();
        question.setStatus(status);
        questionInfoService.updateByParam(question, params);
    }

    @PostMapping("/importQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_IMPORT)
    public ResponseVO importQuestion(HttpSession session, MultipartFile file) {
        SessionUserAdminDto userAdminDto = getUserAdminFromSession(session);
        List<ImportErrorItem> errorList = questionInfoService.importQuestion(file, userAdminDto);
        return getSuccessResponseVO(errorList);
    }

    @PostMapping("/showQuestionDetailNext")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_LIST)
    public ResponseVO showQuestionDetailNext(QuestionInfoQuery query,
                                             @VerifyParam(required = true) Integer currentId, Integer nextType) {
        QuestionInfo questionInfo = questionInfoService.showDetailNext(query, nextType, currentId, false);
        return getSuccessResponseVO(questionInfo);
    }
}