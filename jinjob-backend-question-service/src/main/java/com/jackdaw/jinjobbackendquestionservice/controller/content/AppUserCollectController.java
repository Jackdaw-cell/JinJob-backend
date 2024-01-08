package com.jackdaw.jinjobbackendquestionservice.controller.content;

import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendmodel.common.BaseResponse;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendmodel.entity.dto.userCollect.CollectAddRequest;
import com.jackdaw.jinjobbackendmodel.entity.dto.userCollect.CollectCancelRequest;
import com.jackdaw.jinjobbackendquestionservice.controller.ABaseController;
import com.jackdaw.jinjobbackendserviceclient.service.user.UserFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户收藏 Controller
 */
@RestController("appUserCollectController")
@RequestMapping("/appUserCollect")
public class AppUserCollectController extends ABaseController {

//    @Resource
//    private AppUserCollectService appUserCollectService;

    @Resource
    private UserFeignClient userFeignClient;

    @PostMapping("/addCollect")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Object> addCollect(@RequestHeader(value = "Authorization", required = false) String token,
                                           @RequestBody CollectAddRequest collectAddRequest) {
//        appUserCollectService.saveCollect(getAppUserLoginInfoFromToken(token).getUserId(), collectAddRequest.getObjectId(), collectAddRequest.getCollectType());
        userFeignClient.saveCollect(getAppUserLoginInfoFromToken(token).getUserId(), collectAddRequest.getObjectId(), collectAddRequest.getCollectType());
//        return getSuccessResponseVO(null);
        return ResultUtils.success(null);
    }

    @PostMapping("/cancelCollect")
    @GlobalInterceptor(checkLogin = true)
    public BaseResponse<Object> cancelCollect(@RequestHeader(value = "Authorization", required = false) String token,
                                              @RequestBody CollectCancelRequest collectCancelRequest) {
//        appUserCollectService.deleteAppUserCollectByUserIdAndObjectIdAndCollectType(getAppUserLoginInfoFromToken(token).getUserId(), collectCancelRequest.getObjectId(), collectCancelRequest.getCollectType());
        userFeignClient.deleteAppUserCollectByUserIdAndObjectIdAndCollectType(getAppUserLoginInfoFromToken(token).getUserId(), collectCancelRequest.getObjectId(), collectCancelRequest.getCollectType());
//        return getSuccessResponseVO(null);
        return ResultUtils.success(null);
    }

}