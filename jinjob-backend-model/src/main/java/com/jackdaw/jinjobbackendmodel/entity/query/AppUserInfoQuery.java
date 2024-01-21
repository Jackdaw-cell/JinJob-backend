package com.jackdaw.jinjobbackendmodel.entity.query;


import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserInfoQuery extends BaseParam {


	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 邮箱
	 */
	private String email;

	private String emailFuzzy;

	/**
	 * 昵称
	 */
	private String nickName;

	private String nickNameFuzzy;

	/**
	 * 头像
	 */
	private String avatar;

	private String avatarFuzzy;

	/**
	 * 密码
	 */
	private String password;

	private String passwordFuzzy;

	/**
	 * 性别 0:女 1:男
	 */
	private Integer sex;

	/**
	 * 创建时间
	 */
	private String joinTime;

	private String joinTimeStart;

	private String joinTimeEnd;

	/**
	 * 最后登录时间
	 */
	private String lastLoginTime;

	private String lastLoginTimeStart;

	private String lastLoginTimeEnd;

	/**
	 * 最后使用的设备ID
	 */
	private String lastUseDeviceId;

	private String lastUseDeviceIdFuzzy;
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
	 * 标签
	 */
	private String examAcSum;

	/**
	 * 标签
	 */
	private String ojAcSum;


	/**
	 * 标签
	 */
	private String examAcScore;

	/**
	 * 手机品牌
	 */
	private String lastUseDeviceBrand;

	private String lastUseDeviceBrandFuzzy;

	/**
	 * 
	 */
	private String lastLoginIp;

	private String lastLoginIpFuzzy;

	/**
	 * 0:禁用 1:正常
	 */
	private Integer status;




}
