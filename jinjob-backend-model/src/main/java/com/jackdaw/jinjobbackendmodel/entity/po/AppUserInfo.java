package com.jackdaw.jinjobbackendmodel.entity.po;

import com.jackdaw.jinjobbackendmodel.enums.DateTimePatternEnum;
import com.jackdaw.jinjobbackendcommon.utils.DateUtil;
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
public class AppUserInfo implements Serializable {


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
	 * 密码
	 */
	private String password;

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
	 * 最后使用的设备ID
	 */
	private String lastUseDeviceId;

	/**
	 * 手机品牌
	 */
	private String lastUseDeviceBrand;

	/**
	 * 
	 */
	private String lastLoginIp;

	/**
	 * 0:禁用 1:正常
	 */
	private Integer status;


	@Override
	public String toString (){
		return "用户ID:"+(userId == null ? "空" : userId)+"，邮箱:"+(email == null ? "空" : email)+"，昵称:"+(nickName == null ? "空" : nickName)+"，头像:"+(avatar == null ? "空" : avatar)+"，密码:"+(password == null ? "空" : password)+"，性别 0:女 1:男:"+(sex == null ? "空" : sex)+"，创建时间:"+(joinTime == null ? "空" : DateUtil.format(joinTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，最后登录时间:"+(lastLoginTime == null ? "空" : DateUtil.format(lastLoginTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，最后使用的设备ID:"+(lastUseDeviceId == null ? "空" : lastUseDeviceId)+"，手机品牌:"+(lastUseDeviceBrand == null ? "空" : lastUseDeviceBrand)+"，lastLoginIp:"+(lastLoginIp == null ? "空" : lastLoginIp)+"，0:禁用 1:正常:"+(status == null ? "空" : status);
	}
}
