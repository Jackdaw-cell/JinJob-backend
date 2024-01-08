package com.jackdaw.jinjobbackendadminservice.controller.app;

import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.AppDeviceService;
import com.jackdaw.jinjobbackendadminservice.service.AppUserInfoService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserInfo;
import com.jackdaw.jinjobbackendmodel.entity.query.AppDeviceQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.AppUserInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import com.jackdaw.jinjobbackendmodel.enums.ResponseCodeEnum;
import com.jackdaw.jinjobbackendmodel.enums.UserStatusEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("appUserInfoController")
@RequestMapping("/appUser")
public class AppUserInfoController extends ABaseController {

    @Resource
    private AppUserInfoService appUserInfoService;

    @Resource
    private AppDeviceService appDeviceService;


    @PostMapping("/loadDeviceList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_USER_DEVICE)
    public ResponseVO loadDeviceList(AppDeviceQuery appDeviceQuery) {
        appDeviceQuery.setOrderBy("create_time desc");
        return getSuccessResponseVO(appDeviceService.findListByPage(appDeviceQuery));
    }

    @PostMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_USER_LIST)
    public ResponseVO loadDataList(AppUserInfoQuery query) {
        query.setOrderBy("join_time desc");
        return getSuccessResponseVO(appUserInfoService.findListByPage(query));
    }

    @PostMapping("updateStatus")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_USER_EDIT)
    public ResponseVO updateStatus(@VerifyParam(required = true) String userId, @VerifyParam(required = true) Integer status) {
        UserStatusEnum statusEnum = UserStatusEnum.getByStatus(status);
        if (statusEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        AppUserInfo appUserInfo = new AppUserInfo();
        appUserInfo.setStatus(status);
        this.appUserInfoService.updateAppUserInfoByUserId(appUserInfo, userId);
        return getSuccessResponseVO(null);
    }

}
