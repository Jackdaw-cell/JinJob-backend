package com.jackdaw.jinjobbackendmodel.common;

/**
 * 自定义错误码
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    CODE_404(404, "请求地址不存在"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    CHECKCODE_ERROR(50002, "验证码错误"),
    CHECKEXAM_ERROR(50003, "考试检验失败"),
    TOKEN_ERROR(50003, "用户信息检验失败"),
    USERINFO_ERROR(50004, "用户信息获取失败"),
    CODE_601(50005, "信息已经存在"),
    API_REQUEST_ERROR(50010, "接口调用失败");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
