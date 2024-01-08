package com.jackdaw.jinjobbackendmodel.entity.query;


import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户在线考试参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppExamQuery extends BaseParam {


	/**
	 * 自增ID
	 */
	private Integer examId;

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 用户昵称
	 */
	private String nickName;

	private String nickNameFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 开始时间
	 */
	private String startTime;

	private String startTimeStart;

	private String startTimeEnd;

	/**
	 * 结束时间
	 */
	private String endTime;

	private String endTimeStart;

	private String endTimeEnd;

	/**
	 * 0:未完成 1:已完成
	 */
	private Integer status;

	/**
	 * 分数
	 */
	private Float score;

	/**
	 * 答案集合
	 */
	private String answerList;

	/**
	 * 备注
	 */
	private String remark;

	private String remarkFuzzy;


}
