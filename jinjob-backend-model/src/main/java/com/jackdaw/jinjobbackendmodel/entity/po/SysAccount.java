package com.jackdaw.jinjobbackendmodel.entity.po;

import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.enums.DateTimePatternEnum;
import com.jackdaw.jinjobbackendmodel.enums.VerifyRegexEnum;
import com.jackdaw.jinjobbackendcommon.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * 账号信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysAccount implements Serializable {


    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 手机号
     */
    @VerifyParam(required = true, regex = VerifyRegexEnum.PHONE)
    private String phone;

    /**
     * 用户名
     */
    @VerifyParam(required = true, max = 20)
    private String userName;

    /**
     * 密码
     */
    @VerifyParam(regex = VerifyRegexEnum.PASSWORD)
    private String password;

    /**
     * 职位
     */
    private String position;

    /**
     * 状态 0:禁用 1:启用
     */
    private Integer status;

    /**
     * 用户拥有的角色多个用逗号隔开
     */
    @VerifyParam(required = true)
    private String roles;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String roleNames;

    @Override
    public String toString() {
        return "用户ID:" + (userId == null ? "空" : userId) + "，手机号:" + (phone == null ? "空" : phone) + "，用户名:" + (userName == null ? "空" : userName) + "，密码:" + (password == null ? "空" : password) + "，职位:" + (position == null ? "空" : position) + "，状态 0:禁用 1:启用:" + (status == null ? "空" : status) + "，用户拥有的角色多个用逗号隔开:" + (roles == null ? "空" : roles) + "，创建时间:" + (createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
    }
}
