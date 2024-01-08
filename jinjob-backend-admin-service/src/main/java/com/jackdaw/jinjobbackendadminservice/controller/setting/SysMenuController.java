package com.jackdaw.jinjobbackendadminservice.controller.setting;

import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.SysMenuService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.entity.po.SysMenu;
import com.jackdaw.jinjobbackendmodel.entity.query.SysMenuQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 菜单表 控制层
 */
@RestController("sysMenuController")
@RequestMapping("/settings")
public class SysMenuController extends ABaseController {

    @Resource
    private SysMenuService sysMenuService;

    @PostMapping("/menuList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_MENU)
    public ResponseVO menuList() {
        SysMenuQuery param = new SysMenuQuery();
        param.setOrderBy("sort asc");
        param.setFormate2Tree(true);
        List<SysMenu> listByParam = sysMenuService.findListByParam(param);
        return getSuccessResponseVO(listByParam);
    }

    @PostMapping("/saveMenu")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_MENU_EDIT)
    public ResponseVO saveMenu(@VerifyParam SysMenu sysMenu) {
        this.sysMenuService.saveMenu(sysMenu);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/delMenu")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_MENU_EDIT)
    public ResponseVO saveMenu(@VerifyParam(required = true) Integer menuId) {
        this.sysMenuService.deleteSysMenuByMenuId(menuId);
        return getSuccessResponseVO(null);
    }
}