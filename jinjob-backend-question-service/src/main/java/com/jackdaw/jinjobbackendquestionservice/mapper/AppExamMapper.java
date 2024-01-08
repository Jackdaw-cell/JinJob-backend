package com.jackdaw.jinjobbackendquestionservice.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 用户在线考试 数据库操作接口
 */
public interface AppExamMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据ExamId更新
	 */
	 Integer updateByExamId(@Param("bean") T t,@Param("examId") Integer examId);


	/**
	 * 根据ExamId删除
	 */
	 Integer deleteByExamId(@Param("examId") Integer examId);


	/**
	 * 根据ExamId获取对象
	 */
	 T selectByExamId(@Param("examId") Integer examId);


}
