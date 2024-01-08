package com.jackdaw.jinjobbackenduserservice.controller;

import cn.hutool.core.util.ObjUtil;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AGlobalExceptionHandlerController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(AGlobalExceptionHandlerController.class);

    @ExceptionHandler(value = Exception.class)
    Object handleException(Exception e, HttpServletRequest request) {
        logger.error("请求错误，请求地址{},错误信息:", request.getRequestURL(), e);
        ResponseVO ajaxResponse = new ResponseVO();
        //404
        if (e instanceof NoHandlerFoundException) {
            ajaxResponse.setCode(ErrorCode.CODE_404.getCode());
            ajaxResponse.setMessage(ErrorCode.CODE_404.getMessage());
            ajaxResponse.setStatus(STATUC_ERROR);
        } else if (e instanceof BusinessException) {
            //业务错误
            BusinessException biz = (BusinessException) e;
            ajaxResponse.setCode(ObjUtil.isNull(biz.getCode()) ? ErrorCode.PARAMS_ERROR.getCode() : biz.getCode());
            ajaxResponse.setMessage(biz.getMessage());
            ajaxResponse.setStatus(STATUC_ERROR);
        } else if (e instanceof BindException || e instanceof MethodArgumentTypeMismatchException) {
            //参数类型错误
            ajaxResponse.setCode(ErrorCode.PARAMS_ERROR.getCode());
            ajaxResponse.setMessage(ErrorCode.PARAMS_ERROR.getMessage());
            ajaxResponse.setStatus(STATUC_ERROR);
        } else if (e instanceof DuplicateKeyException) {
            //主键冲突
            ajaxResponse.setCode(ErrorCode.CODE_601.getCode());
            ajaxResponse.setMessage(ErrorCode.CODE_601.getMessage());
            ajaxResponse.setStatus(STATUC_ERROR);
        } else {
            ajaxResponse.setCode(ErrorCode.API_REQUEST_ERROR.getCode());
            ajaxResponse.setMessage(ErrorCode.API_REQUEST_ERROR.getMessage());
            ajaxResponse.setStatus(STATUC_ERROR);
        }
        return ajaxResponse;
    }
}
