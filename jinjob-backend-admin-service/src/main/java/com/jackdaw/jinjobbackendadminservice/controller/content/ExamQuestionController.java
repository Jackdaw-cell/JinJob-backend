package com.jackdaw.jinjobbackendadminservice.controller.content;

import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.ExamQuestionItemService;
import com.jackdaw.jinjobbackendadminservice.service.ExamQuestionService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.JsonUtils;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.entity.dto.ImportErrorItem;
import com.jackdaw.jinjobbackendmodel.entity.dto.SessionUserAdminDto;
import com.jackdaw.jinjobbackendmodel.entity.po.ExamQuestion;
import com.jackdaw.jinjobbackendmodel.entity.po.ExamQuestionItem;
import com.jackdaw.jinjobbackendmodel.entity.query.ExamQuestionItemQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.ExamQuestionQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import com.jackdaw.jinjobbackendmodel.enums.PostStatusEnum;
import com.jackdaw.jinjobbackendmodel.enums.QuestionTypeEnum;
import com.jackdaw.jinjobbackendmodel.enums.ResponseCodeEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 问题 Controller
 */
@RestController("examQuestionController")
@RequestMapping("/examQuestion")
public class ExamQuestionController extends ABaseController {

    @Resource
    private ExamQuestionService examQuestionService;

    @Resource
    private ExamQuestionItemService examQuestionItemService;

    /**
     * 根据条件分页查询
     */
    @PostMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EXAM_QUESTION_LIST)
    public ResponseVO loadDataList(ExamQuestionQuery query) {
        query.setOrderBy("question_id desc");
        query.setQueryAnswer(true);
        return getSuccessResponseVO(examQuestionService.findListByPage(query));
    }


    /**
     * 保存题目
     * @param session
     * @param examQuestion
     * @param questionItemListJson
     * @return
     */
    @PostMapping("/saveExamQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EXAM_QUESTION_EDIT)
    public ResponseVO saveExamQuestion(HttpSession session,
                                       @VerifyParam(required = true) ExamQuestion examQuestion,
                                       String questionItemListJson) {
        SessionUserAdminDto userAdminDto = getUserAdminFromSession(session);
        examQuestion.setCreateUserId(String.valueOf(userAdminDto.getUserId()));
        examQuestion.setCreateUserName(userAdminDto.getUserName());
        List<ExamQuestionItem> examQuestionItemList = new ArrayList<>();
        if (!QuestionTypeEnum.TRUE_FALSE.getType().equals(examQuestion.getQuestionType())) {
            if (StringTools.isEmpty(questionItemListJson)) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            examQuestionItemList = JsonUtils.convertJsonArray2List(questionItemListJson, ExamQuestionItem.class);
        }
        examQuestionService.saveExamQuestion(examQuestion, examQuestionItemList, userAdminDto.getSuperAdmin());
        return getSuccessResponseVO(null);
    }

    @PostMapping("/loadQuestionItem")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EXAM_QUESTION_LIST)
    public ResponseVO loadQuestionItem(@VerifyParam(required = true) Integer questionId) {
        ExamQuestionItemQuery itemQuery = new ExamQuestionItemQuery();
        itemQuery.setQuestionId(questionId);
        itemQuery.setOrderBy("sort asc");
        return getSuccessResponseVO(examQuestionItemService.findListByParam(itemQuery));
    }

    /**
     * 删除条目
     * @param session
     * @param questionId
     * @return
     */
    @PostMapping("/delExamQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EXAM_QUESTION_DEL)
    public ResponseVO delExamQuestion(HttpSession session,
                                      @VerifyParam(required = true) Integer questionId) {
        SessionUserAdminDto userAdminDto = getUserAdminFromSession(session);
        examQuestionService.delExamQuestionBatch(String.valueOf(questionId), userAdminDto.getSuperAdmin() ? null : userAdminDto.getUserId());
        return getSuccessResponseVO(null);
    }

    @PostMapping("/delExamQuestionBatch")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EXAM_QUESTION_DEL_BATCH)
    public ResponseVO delExamQuestion(@VerifyParam(required = true) String questionIds) {
        examQuestionService.delExamQuestionBatch(questionIds, null);
        return getSuccessResponseVO(null);
    }

    /**
     * 题目发布
     * @param questionIds
     * @return
     */
    @PostMapping("/postExamQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EXAM_QUESTION_POST)
    public ResponseVO postExamQuestion(@VerifyParam(required = true) String questionIds) {
        updateStatus(questionIds, PostStatusEnum.POST.getStatus());
        return getSuccessResponseVO(null);
    }

    /**
     * 取消发布
     * @param questionIds
     * @return
     */
    @PostMapping("/cancelPostExamQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EXAM_QUESTION_POST)
    public ResponseVO cancelPostExamQuestion(@VerifyParam(required = true) String questionIds) {
        updateStatus(questionIds, PostStatusEnum.NO_POST.getStatus());
        return getSuccessResponseVO(null);
    }

    /**
     * 更新状态
     * @param questionIds
     * @param status
     */
    private void updateStatus(String questionIds, Integer status) {
        ExamQuestionQuery params = new ExamQuestionQuery();
        params.setQuestionIds(questionIds.split(","));
        ExamQuestion question = new ExamQuestion();
        question.setStatus(status);
        examQuestionService.updateByParam(question, params);
    }

    @PostMapping("/showExamQuestionDetailNext")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EXAM_QUESTION_LIST)
    public ResponseVO showExamQuestionDetailNext(ExamQuestionQuery query,
                                                 @VerifyParam(required = true) Integer currentId, Integer nextType) {
        ExamQuestion examQuestion = examQuestionService.showDetailNext(query, nextType, currentId);
        return getSuccessResponseVO(examQuestion);
    }

    @PostMapping("/importExamQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EXAM_QUESTION_IMPORT)
    public ResponseVO importExamQuestion(HttpSession session, MultipartFile file) {
        SessionUserAdminDto userAdminDto = getUserAdminFromSession(session);
        List<ImportErrorItem> errorList = examQuestionService.importExamQuestion(file, userAdminDto);
        return getSuccessResponseVO(errorList);
    }
}