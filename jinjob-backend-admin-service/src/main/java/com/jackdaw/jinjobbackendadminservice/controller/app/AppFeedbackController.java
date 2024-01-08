package com.jackdaw.jinjobbackendadminservice.controller.app;

import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.AppFeedbackService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.entity.po.AppFeedback;
import com.jackdaw.jinjobbackendmodel.entity.query.AppFeedbackQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * App问题反馈
 */
@RestController("appFeedbackController")
@RequestMapping("/appFeedback")
public class AppFeedbackController extends ABaseController {
    @Resource
    private AppFeedbackService appFeedbackService;

    @PostMapping("/loadFeedback")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_FEEDBACK_LIST)
    public ResponseVO loadFeedback(AppFeedbackQuery query) {
        query.setOrderBy("feedback_id desc");
        query.setPFeedbackId(0);
        PaginationResultVO resultVO = appFeedbackService.findListByPage(query);
        return getSuccessResponseVO(resultVO);
    }

    @PostMapping("/loadFeedbackReply")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_FEEDBACK_REPLY)
    public ResponseVO loadFeedbackReply(@VerifyParam(required = true) Integer pFeedbackId) {
        AppFeedbackQuery query = new AppFeedbackQuery();
        query.setPFeedbackId(pFeedbackId);
        query.setOrderBy("feedback_id asc");
        List<AppFeedback> list = appFeedbackService.findListByParam(query);
        return getSuccessResponseVO(list);
    }

    @PostMapping("/replyFeedback")
    @GlobalInterceptor
    public ResponseVO replyFeedback(@VerifyParam(required = true, max = 500) String content,
                                    @VerifyParam(required = true) Integer pFeedbackId) {
        AppFeedback appFeedback = new AppFeedback();
        appFeedback.setContent(content);
        appFeedback.setPFeedbackId(pFeedbackId);
        appFeedbackService.replyFeedback(appFeedback);
        return getSuccessResponseVO(appFeedback);
    }
}
