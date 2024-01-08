package com.jackdaw.jinjobbackenduserservice.controller.inner;

import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendcommon.config.RedisUtils;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.JWTUtil;
import com.jackdaw.jinjobbackendmodel.constant.Constants;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserCollect;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserInfo;
import com.jackdaw.jinjobbackendserviceclient.service.user.UserFeignClient;
import com.jackdaw.jinjobbackenduserservice.service.AppUserCollectService;
import com.jackdaw.jinjobbackenduserservice.service.AppUserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private AppUserInfoService userService;

    @Resource
    private JWTUtil<AppUserLoginDto> jwtUtil;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private AppUserCollectService appUserCollectService;

    @Override
    @GetMapping("/saveCollect")
    public void saveCollect(String userId, String objectId, Integer collectType) {
        appUserCollectService.saveCollect(userId,objectId,collectType);
    }

    @Override
    @GetMapping("/deleteAppUserCollectByUserIdAndObjectIdAndCollectType")
    public Integer deleteAppUserCollectByUserIdAndObjectIdAndCollectType(String userId, String objectId, Integer collectType) {
        return appUserCollectService.deleteAppUserCollectByUserIdAndObjectIdAndCollectType(userId,objectId,collectType);
    }

    @Override
    @GetMapping("/getAppUserCollectByUserIdAndObjectIdAndCollectType")
    public AppUserCollect getAppUserCollectByUserIdAndObjectIdAndCollectType(String userId, String objectId, Integer collectType) {
        return appUserCollectService.getAppUserCollectByUserIdAndObjectIdAndCollectType(userId,objectId,collectType);
    }

    /**
     * 根据 id 获取用户
     *
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public AppUserInfo getById(@RequestParam("userId") String userId) {
        return userService.getAppUserInfoByUserId(userId);
    }


    /**
     * 根据 token 获取用户
     *
     * @param token
     * @return
     */
    @Override
    @GetMapping("/get/token")
    public AppUserInfo getByToken(@RequestParam("token") String token) {
        AppUserLoginDto userAppDto = jwtUtil.getTokenData(Constants.JWT_KEY_LOGIN_TOKEN, token, AppUserLoginDto.class);
        if (userAppDto == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return userService.getAppUserInfoByUserId(userAppDto.getUserId());
    }

    /**
     * 根据 token 登录
     *
     * @param token
     * @return
     */
    @Override
    @GetMapping("/get/userCheckin")
    public String getUserCheckin(@RequestParam("token") String token){
        AppUserInfo appUserLoginDto = getByToken(token);
        return userService.getCheckinInfo(appUserLoginDto.getUserId());
    }

    /**
     *
     * @return
     */
    @Override
    @GetMapping("/get/setCountOjQuestion")
    public boolean setCountOjQuestion(@RequestParam("token") String userId){
        AppUserInfo appUser = getById(userId);
        appUser.setOjAcSum(appUser.getOjAcSum()+1);
        return userService.updateAppUserInfoByUserId(appUser,userId)>0;
    }

    /**
     *
     * @return
     */
    @Override
    @GetMapping("/get/setCountExamQuestion")
    public boolean setCountExamQuestion(@RequestParam("token") String userId,
                                      @RequestParam("score") Float score){
        AppUserInfo appUserLogin = getById(userId);
        appUserLogin.setExamAcSum(appUserLogin.getExamAcSum()+1);
        float result = (appUserLogin.getExamAcScore() * appUserLogin.getExamAcSum() + score) / (appUserLogin.getExamAcSum() + 1);
        appUserLogin.setExamAcScore((float) (Math.floor(result * 10) / 10));
        return userService.updateAppUserInfoByUserId(appUserLogin,userId)>0;
    }
}
