package com.jackdaw.jinjobbackendadminservice.controller.setting;

import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.SysRoleService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.entity.po.SysRole;
import com.jackdaw.jinjobbackendmodel.entity.query.SysRoleQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import com.jackdaw.jinjobbackendmodel.enums.ResponseCodeEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController("sysRoleController")
@RequestMapping("/settings")
public class SysRoleController extends ABaseController {

    @Resource
    private SysRoleService sysRoleService;

    @PostMapping("loadRoles")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_LIST)
    public ResponseVO loadRoles(SysRoleQuery roleQuery) {
        roleQuery.setOrderBy("create_time desc");
        PaginationResultVO<SysRole> roles = sysRoleService.findListByPage(roleQuery);
        return getSuccessResponseVO(roles);
    }

    @PostMapping("loadAllRoles")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_LIST)
    public ResponseVO loadAllRoles() {
        SysRoleQuery roleQuery = new SysRoleQuery();
        roleQuery.setOrderBy("create_time desc");
        List<SysRole> roles = sysRoleService.findListByParam(roleQuery);
        return getSuccessResponseVO(roles);
    }

    @PostMapping("saveRole")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_EDIT)
    public ResponseVO saveRole(@VerifyParam SysRole role, @VerifyParam(required = true) String menuIds, String halfMenuIds) {
        if (role.getRoleId() == null && StringTools.isEmpty(menuIds)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        sysRoleService.saveRole(role, menuIds, halfMenuIds);
        return getSuccessResponseVO(null);
    }

    @PostMapping("getRoleByRoleId")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_LIST)
    public ResponseVO getRoleByRoleId(Integer roleId) {
        SysRole sysRole = sysRoleService.getSysRoleByRoleId(roleId);
        return getSuccessResponseVO(sysRole);
    }

    @PostMapping("saveRoleMenu")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_EDIT)
    public ResponseVO saveRole(@VerifyParam(required = true) Integer roleId, @VerifyParam(required = true) String menuIds, String halfMenuIds) {
        sysRoleService.saveRoleMenu(roleId, menuIds, halfMenuIds);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/delRole")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_DEL)
    public ResponseVO roleDel(@VerifyParam(required = true) Integer roleId) {
        sysRoleService.deleteSysRoleByRoleId(roleId);
        return getSuccessResponseVO(null);
    }
}
