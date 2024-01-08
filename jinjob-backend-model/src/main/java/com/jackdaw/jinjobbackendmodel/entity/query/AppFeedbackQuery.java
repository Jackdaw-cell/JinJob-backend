package com.jackdaw.jinjobbackendmodel.entity.query;


import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 问题反馈参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppFeedbackQuery extends BaseParam {


	/**
	 * 自增ID
	 */
	private Integer feedbackId;

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 昵称
	 */
	private String nickName;

	private String nickNameFuzzy;

	/**
	 * 反馈内容
	 */
	private String content;

	private String contentFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

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
	private String clientLastSendTime;

	private String clientLastSendTimeStart;

	private String clientLastSendTimeEnd;

}
