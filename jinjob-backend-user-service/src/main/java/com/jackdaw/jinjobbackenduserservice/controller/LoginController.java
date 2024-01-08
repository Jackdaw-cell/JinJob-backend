package com.jackdaw.jinjobbackenduserservice.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendmodel.common.BaseResponse;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendcommon.config.RedisUtils;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserInfoDto;
import com.jackdaw.jinjobbackendmodel.entity.query.AppUserInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendmodel.constant.Constants;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.CreateImageCode;
import com.jackdaw.jinjobbackendmodel.entity.dto.userAccount.UserAccountAddRequest;
import com.jackdaw.jinjobbackendmodel.entity.dto.userAccount.UserAccountLoginRequest;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserInfo;
import com.jackdaw.jinjobbackenduserservice.service.AppUserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/account")
public class LoginController extends ABaseController {

    @Resource
    private AppUserInfoService appUserInfoService;

    @Resource
    private RedisUtils<String> redisUtils;

    private static final Integer CHECK_CODE_TYPE_REGISTER = 0;

    private static final Integer CHECK_CODE_TYPE_LOGIN = 1;

    @PostMapping("/register")
    @GlobalInterceptor
    public BaseResponse<Object> register(
            HttpServletRequest request,
            @RequestBody UserAccountAddRequest userAccountAddRequest) {

        String redisKey = Constants.REDIS_KEY_CHECKCODE + CHECK_CODE_TYPE_REGISTER;
        try {
            String checkCodeRedis = redisUtils.get(redisKey);

            if (!userAccountAddRequest.getCheckCode().equalsIgnoreCase(checkCodeRedis)) {
//                throw new BusinessException("图片验证码不正确");
                throw new BusinessException(ErrorCode.CHECKCODE_ERROR);
            }
            AppUserInfo appUserInfo = new AppUserInfo();
            appUserInfo.setEmail(userAccountAddRequest.getEmail());
            appUserInfo.setSex(userAccountAddRequest.getSex());
            appUserInfo.setPassword(userAccountAddRequest.getPassword());
            appUserInfo.setNickName(userAccountAddRequest.getNickName());
            appUserInfo.setLastLoginIp(getIpAddr(request));
            appUserInfoService.register(appUserInfo);
            return ResultUtils.success(null);
        } finally {
//            redisUtils.delete(redisKey);
        }
    }

    @GetMapping("/checkCode")
    @GlobalInterceptor
    public void checkCode(HttpServletRequest request,
                          HttpServletResponse response,
                         @RequestParam(value = "type",required = true) String type) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 35, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        String redisKey = Constants.REDIS_KEY_CHECKCODE + type;
        redisUtils.setex(redisKey, code, 10 * 60);
        vCode.write(response.getOutputStream());
    }

    @PostMapping("/login")
    @GlobalInterceptor(checkLogin = false)
    public BaseResponse<String> login(HttpServletRequest request,
                                      @RequestBody UserAccountLoginRequest userAccountLoginRequest) {
        String redisKey = Constants.REDIS_KEY_CHECKCODE  + CHECK_CODE_TYPE_LOGIN;
        try {
            String checkCodeRedis = redisUtils.get(redisKey);
            if (!userAccountLoginRequest.getCheckCode().equalsIgnoreCase(checkCodeRedis)) {
                throw new BusinessException(ErrorCode.CHECKCODE_ERROR);
            }
            //TODO:网关全局token校验
            String token = appUserInfoService.login(userAccountLoginRequest.getEmail(), userAccountLoginRequest.getPassword(), getIpAddr(request));
//            return getSuccessResponseVO(token);
            return ResultUtils.success(token);
        } finally {
//            redisUtils.delete(redisKey);
        }
    }

    @GetMapping("/get/login")
    @GlobalInterceptor
    public BaseResponse<AppUserLoginDto> getLoginUser(HttpServletRequest request,
                                                      @RequestHeader(value = "Authorization", required = true) String token) {
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        return ResultUtils.success(userAppDto);
    }

    @GetMapping("/autoLogin")
    @GlobalInterceptor
    public BaseResponse<String> autoLogin(HttpServletRequest request,
                                          @RequestHeader(value = "Authorization") String token) {
        String newToken = appUserInfoService.autoLogin(token, getIpAddr(request));
//        return getSuccessResponseVO(newToken);
        return ResultUtils.success(newToken);
    }
}
