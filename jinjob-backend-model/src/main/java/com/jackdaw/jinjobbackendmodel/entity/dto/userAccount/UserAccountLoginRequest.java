package com.jackdaw.jinjobbackendmodel.entity.dto.userAccount;

import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
@Data
public class UserAccountLoginRequest implements Serializable {

    private String email;

    private String password;

    private String checkCode;

    private static final long serialVersionUID = 1L;
}