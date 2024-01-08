package com.jackdaw.jinjobbackendadminservice.controller.setting;

import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.SysAccountService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendcommon.config.AppConfig;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.entity.po.SysAccount;
import com.jackdaw.jinjobbackendmodel.entity.query.SysAccountQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.SysAccountVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import com.jackdaw.jinjobbackendmodel.enums.ResponseCodeEnum;
import com.jackdaw.jinjobbackendmodel.enums.UserStatusEnum;
import com.jackdaw.jinjobbackendmodel.enums.VerifyRegexEnum;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("sysAccountController")
@RequestMapping("/settings")
public class SysAccountController extends ABaseController {
    @Resource
    private SysAccountService sysAccountService;

    @Resource
    private AppConfig appConfig;

    @PostMapping("loadAccountList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ACCOUNT_LIST)
    public ResponseVO loadAccountList(SysAccountQuery accountQuery) {
        accountQuery.setOrderBy("create_time desc");
        accountQuery.setQueryRoles(true);
        PaginationResultVO<SysAccount> accountList = sysAccountService.findListByPage(accountQuery);
        return getSuccessResponseVO(convert2PaginationVO(accountList, SysAccountVO.class));
    }

    @PostMapping("saveAccount")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ACCOUNT_EDIT)
    public ResponseVO saveAccount(@VerifyParam SysAccount sysAccount) {
        this.sysAccountService.saveSysAccount(sysAccount);
        return getSuccessResponseVO(null);
    }


    @PostMapping("updatePassword")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ACCOUNT_UPDATE_PASSWORD)
    public ResponseVO updatePassword(@VerifyParam(required = true) Integer userId,
                                     @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD) String password) {
        SysAccount updateInfo = new SysAccount();
        updateInfo.setPassword(StringTools.encodeByMD5(password));
        sysAccountService.updateSysAccountByUserId(updateInfo, userId);
        return getSuccessResponseVO(null);
    }


    @PostMapping("updateStatus")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ACCOUNT_OP_STATUS)
    public ResponseVO updateStatus(@VerifyParam(required = true) Integer userId, @VerifyParam(required = true) Integer status) {
        UserStatusEnum statusEnum = UserStatusEnum.getByStatus(status);
        if (statusEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        SysAccount sysAccount = new SysAccount();
        sysAccount.setStatus(status);
        this.sysAccountService.updateSysAccountByUserId(sysAccount, userId);
        return getSuccessResponseVO(null);
    }
    
    @PostMapping("delAccount")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ACCOUNT_DEL)
    public ResponseVO delAccount(@VerifyParam(required = true) Integer userId) {
        SysAccount sysAccount = this.sysAccountService.getSysAccountByUserId(userId);
        if (!StringTools.isEmpty(appConfig.getSuperAdminPhones()) && ArrayUtils.contains(appConfig.getSuperAdminPhones().split(","), sysAccount.getPhone())) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"系统超级管理员不允许删除");
        }
        this.sysAccountService.deleteSysAccountByUserId(userId);
        return getSuccessResponseVO(null);
    }


}
