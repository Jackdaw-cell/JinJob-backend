package com.jackdaw.jinjobbackendadminservice.controller.app;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.*;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit.OjQuestionSubmitQueryRequest;
import com.jackdaw.jinjobbackendmodel.entity.po.AppExam;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionSubmit;
import com.jackdaw.jinjobbackendmodel.entity.query.AppDeviceQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.AppExamQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.AppUserInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import com.jackdaw.jinjobbackendmodel.enums.ResponseCodeEnum;
import com.jackdaw.jinjobbackendmodel.enums.UserStatusEnum;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController("appExamInfoController")
@RequestMapping("/appExam")
public class AppExamInfoController extends ABaseController {

    @Resource
    private OjQuestionSubmitService ojQuestionSubmitService;
    @Resource
    private AppExamService appExamService;

    @PostMapping("/loadExamDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_USER_LIST)
    public ResponseVO loadDataList(AppExamQuery appExamQuery) {
        PaginationResultVO<AppExam> appExamList = appExamService.findListByPage(appExamQuery);
        return getSuccessResponseVO(appExamList);
    }

    @PostMapping("/loadOjSubmitDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_USER_LIST)
    public ResponseVO loadOjSubmitDataList(OjQuestionSubmitQueryRequest ojQuestionSubmitQueryRequest) {
        Integer current = ojQuestionSubmitQueryRequest.getPageNo();
        Integer size = ojQuestionSubmitQueryRequest.getPageSize();
        // 从数据库中查询原始的题目提交分页信息
        Page<OjQuestionSubmit> questionSubmitPage = ojQuestionSubmitService.page(new Page<>(current, size),
                ojQuestionSubmitService.getQueryWrapper(ojQuestionSubmitQueryRequest));
        PaginationResultVO<OjQuestionSubmit> ojSubmitList =  new PaginationResultVO((int)(questionSubmitPage.getTotal()), size, current,(int)(questionSubmitPage.getTotal()), questionSubmitPage.getRecords());
        return getSuccessResponseVO(ojSubmitList);
    }
}
