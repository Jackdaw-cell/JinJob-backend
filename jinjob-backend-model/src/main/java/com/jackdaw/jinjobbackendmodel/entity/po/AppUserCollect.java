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
 * 用户收藏
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserCollect implements Serializable {


	/**
	 * 收藏ID
	 */
	private Integer collectId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 主体ID 问题ID,考题ID,分享文章ID
	 */
	private String objectId;

	/**
	 * 0:分享收藏 1:问题收藏  2:考题收藏
	 */
	private Integer collectType;

	/**
	 * 收藏时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date collectTime;


	@Override
	public String toString (){
		return "收藏ID:"+(collectId == null ? "空" : collectId)+"，用户ID:"+(userId == null ? "空" : userId)+"，主体ID 问题ID,考题ID,分享文章ID:"+(objectId == null ? "空" : objectId)+"，0:分享收藏 1:问题收藏  2:考题收藏:"+(collectType == null ? "空" : collectType)+"，收藏时间:"+(collectTime == null ? "空" : DateUtil.format(collectTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}
}
