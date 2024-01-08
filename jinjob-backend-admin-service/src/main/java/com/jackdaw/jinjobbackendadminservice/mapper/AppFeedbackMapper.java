package com.jackdaw.jinjobbackendadminservice.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 问题反馈 数据库操作接口
 */
public interface AppFeedbackMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据FeedbackId更新
	 */
	 Integer updateByFeedbackId(@Param("bean") T t,@Param("feedbackId") Integer feedbackId);


	/**
	 * 根据FeedbackId删除
	 */
	 Integer deleteByFeedbackId(@Param("feedbackId") Integer feedbackId);


	/**
	 * 根据FeedbackId获取对象
	 */
	 T selectByFeedbackId(@Param("feedbackId") Integer feedbackId);


	/**
	 * 根据FeedbackIdAndUserId更新
	 */
	 Integer updateByFeedbackIdAndUserId(@Param("bean") T t,@Param("feedbackId") Integer feedbackId,@Param("userId") String userId);


	/**
	 * 根据FeedbackIdAndUserId删除
	 */
	 Integer deleteByFeedbackIdAndUserId(@Param("feedbackId") Integer feedbackId,@Param("userId") String userId);


	/**
	 * 根据FeedbackIdAndUserId获取对象
	 */
	 T selectByFeedbackIdAndUserId(@Param("feedbackId") Integer feedbackId,@Param("userId") String userId);


}
