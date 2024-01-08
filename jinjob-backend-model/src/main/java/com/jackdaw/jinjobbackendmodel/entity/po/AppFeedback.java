package com.jackdaw.jinjobbackendmodel.entity.po;

import com.jackdaw.jinjobbackendcommon.utils.DateUtil;
import com.jackdaw.jinjobbackendmodel.enums.DateTimePatternEnum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * 问题反馈
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppFeedback implements Serializable {


	/**
	 * 自增ID
	 */
	private Integer feedbackId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 反馈内容
	 */
	private String content;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 父级ID
	 */
	private Integer pFeedbackId;

	/**
	 * 状态0:未回复 1:已回复
	 */
	private Integer status;

	/**
	 * 0:访客 1:管理员
	 */
	private Integer sendType;

	/**
	 * 访客最后发送时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date clientLastSendTime;

	@Override
	public String toString (){
		return "自增ID:"+(feedbackId == null ? "空" : feedbackId)+"，用户ID:"+(userId == null ? "空" : userId)+"，昵称:"+(nickName == null ? "空" : nickName)+"，反馈内容:"+(content == null ? "空" : content)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，父级ID:"+(pFeedbackId == null ? "空" : pFeedbackId)+"，状态0:未回复 1:已回复:"+(status == null ? "空" : status)+"，0:访客 1:管理员:"+(sendType == null ? "空" : sendType)+"，访客最后发送时间:"+(clientLastSendTime == null ? "空" : DateUtil.format(clientLastSendTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}
}
