package com.jackdaw.jinjobbackenduserservice.controller;

import cn.hutool.core.util.ObjUtil;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendcommon.config.AppConfig;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.CopyTools;
import com.jackdaw.jinjobbackendcommon.utils.JWTUtil;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.constant.Constants;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


public class ABaseController {

    @Resource
    private JWTUtil<AppUserLoginDto> jwtUtil;

    @Resource
    private AppConfig appConfig;

    protected static final String STATUC_SUCCESS = "success";

    protected static final String STATUC_ERROR = "error";

    protected <T> ResponseVO getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUC_SUCCESS);
        responseVO.setCode(ErrorCode.SUCCESS.getCode());
        responseVO.setMessage(ErrorCode.SUCCESS.getMessage());
        responseVO.setData(t);
        return responseVO;
    }

    protected <T> ResponseVO getBusinessErrorResponseVO(BusinessException e, T t) {
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUC_ERROR);
        if (ObjUtil.isNull(e.getCode())) {
            vo.setCode(ErrorCode.PARAMS_ERROR.getCode());
        } else {
            vo.setCode(e.getCode());
        }
        vo.setMessage(e.getMessage());
        vo.setData(t);
        return vo;
    }

    protected <T> ResponseVO getServerErrorResponseVO(T t) {
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUC_ERROR);
        vo.setCode(ErrorCode.API_REQUEST_ERROR.getCode());
        vo.setMessage(ErrorCode.API_REQUEST_ERROR.getMessage());
        vo.setData(t);
        return vo;
    }

    protected <S, T> PaginationResultVO<T> convert2PaginationVO(PaginationResultVO<S> result, Class<T> classz) {
        PaginationResultVO<T> resultVO = new PaginationResultVO<>();
        resultVO.setList(CopyTools.copyList(result.getList(), classz));
        resultVO.setPageNo(result.getPageNo());
        resultVO.setPageSize(result.getPageSize());
        resultVO.setPageTotal(result.getPageTotal());
        resultVO.setTotalCount(result.getTotalCount());
        return resultVO;
    }

    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    protected AppUserLoginDto getAppUserLoginInfoFromToken(String token) {
        AppUserLoginDto loginDto = jwtUtil.getTokenData(Constants.JWT_KEY_LOGIN_TOKEN, token, AppUserLoginDto.class);
        return loginDto;
    }

    protected String resetContentImg(String content) {
        if (StringTools.isEmpty(content)) {
            return content;
        }
        content = content.replace(Constants.READ_IMAGE_PATH, appConfig.getAppDomain() + Constants.READ_IMAGE_PATH);
        return content;
    }
}
