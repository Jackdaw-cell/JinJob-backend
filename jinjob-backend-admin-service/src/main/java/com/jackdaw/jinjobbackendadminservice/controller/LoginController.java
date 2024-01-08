package com.jackdaw.jinjobbackendadminservice.controller;

import com.jackdaw.jinjobbackendadminservice.service.SysAccountService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.constant.Constants;
import com.jackdaw.jinjobbackendmodel.entity.dto.CreateImageCode;
import com.jackdaw.jinjobbackendmodel.entity.dto.SessionUserAdminDto;
import com.jackdaw.jinjobbackendmodel.entity.po.SysAccount;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.VerifyRegexEnum;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@Api(value = "Login")
public class LoginController extends ABaseController {

    @Resource
    private SysAccountService sysAccountService;

    @GetMapping(value = "/checkCode")
    @GlobalInterceptor(checkLogin = false)
    public void checkCode(HttpServletResponse response, HttpSession session) throws
            IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        session.setAttribute(Constants.CHECK_CODE_KEY, code);
        vCode.write(response.getOutputStream());
    }

    @PostMapping("/login")
    @GlobalInterceptor(checkLogin = false)
    public ResponseVO login(HttpSession session,
                            @VerifyParam(required = true) String phone,
                            @VerifyParam(required = true) String password,
                            @VerifyParam(required = true) String checkCode) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
                throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"图片验证码不正确");
            }
            SessionUserAdminDto sessionAdminUserDto = sysAccountService.login(phone, password);
            session.setAttribute(Constants.SESSION_KEY, sessionAdminUserDto);
            return getSuccessResponseVO(sessionAdminUserDto);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    @PostMapping("/getMenuList")
    @GlobalInterceptor
    public ResponseVO getMenuList(HttpSession session) {
        SessionUserAdminDto sessionAdminUserDto = getUserAdminFromSession(session);
        return getSuccessResponseVO(sessionAdminUserDto.getMenuList());
    }

    @PostMapping("/updateMyPwd")
    @GlobalInterceptor
    public ResponseVO updateMyPwd(HttpSession session,
                                  @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD) String password) {
        SessionUserAdminDto userAdminDto = getUserAdminFromSession(session);
        SysAccount sysAccount = new SysAccount();
        sysAccount.setPassword(StringTools.encodeByMD5(password));
        sysAccountService.updateSysAccountByUserId(sysAccount, userAdminDto.getUserId());
        return getSuccessResponseVO(null);
    }

    @PostMapping("/logout")
    @GlobalInterceptor
    public ResponseVO logout(HttpSession session) {
        session.invalidate();
        return getSuccessResponseVO(null);
    }
}
