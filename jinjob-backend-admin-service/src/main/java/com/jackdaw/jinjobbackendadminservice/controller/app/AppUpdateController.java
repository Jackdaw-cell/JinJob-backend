package com.jackdaw.jinjobbackendadminservice.controller.app;

import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.AppUpdateService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUpdate;
import com.jackdaw.jinjobbackendmodel.entity.query.AppUpdateQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController("appUpdateController")
@RequestMapping("/app")
public class AppUpdateController extends ABaseController {

    @Resource
    private AppUpdateService appUpdateService;

    @PostMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_UPDATE_LIST)
    public ResponseVO loadDataList(AppUpdateQuery query) {
        query.setOrderBy("id desc");
        return getSuccessResponseVO(appUpdateService.findListByPage(query));
    }


    @PostMapping("/saveUpdate")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_UPDATE_EDIT)
    public ResponseVO saveUpdate(Integer id, @VerifyParam(required = true) String version,
                                 @VerifyParam(required = true) String updateDesc,
                                 @VerifyParam(required = true) Integer updateType,
                                 MultipartFile file) {
        AppUpdate appUpdate = new AppUpdate();
        appUpdate.setId(id);
        appUpdate.setVersion(version);
        appUpdate.setUpdateDesc(updateDesc);
        appUpdate.setUpdateType(updateType);
        appUpdateService.saveUpdate(appUpdate, file);
        return getSuccessResponseVO(null);
    }


    @PostMapping("/delUpdate")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_UPDATE_EDIT)
    public ResponseVO delUpdate(@VerifyParam(required = true) Integer id) {
        appUpdateService.deleteAppUpdateById(id);
        return getSuccessResponseVO(null);
    }
    
    @PostMapping("/postUpdate")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_UPDATE_POST)
    public ResponseVO postUpdate(@VerifyParam(required = true) Integer id,
                                 @VerifyParam(required = true) Integer status,
                                 String grayscaleDevice) {
        appUpdateService.postUpdate(id, status, grayscaleDevice);
        return getSuccessResponseVO(null);
    }

}
