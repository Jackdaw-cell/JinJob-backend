package com.jackdaw.jinjobbackendmodel.entity.dto.userAccount;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
@Data
public class UserAccountAddRequest implements Serializable {

    private String email;

    private String password;

    private String nickName;

    private Integer sex;

    private String checkCode;

    private static final long serialVersionUID = 1L;
}