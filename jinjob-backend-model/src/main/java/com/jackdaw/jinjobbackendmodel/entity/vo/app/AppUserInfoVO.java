package com.jackdaw.jinjobbackendmodel.entity.vo.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserInfoVO implements Serializable {


    /**
     * 用户ID
     */
    private String userId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 0:女 1:男
     */
    private Integer sex;

    /**
     * OJ通过数量
     */
    private Integer ojAcSum;

    /**
     * 考试通过数量
     */
    private Integer examAcSum;

    /**
     * 考试通过均分
     */
    private Float examAcScore;


    /**
     * 标签
     */
    private String tags;

    /**
     * 简介
     */
    private String description;

    /**
     * 背景
     */
    private String background;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinTime;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**
     * 手机品牌
     */
    private String lastUseDeviceBrand;

}
