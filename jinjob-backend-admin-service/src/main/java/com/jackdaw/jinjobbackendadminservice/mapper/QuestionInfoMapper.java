package com.jackdaw.jinjobbackendadminservice.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 问题 数据库操作接口
 */
public interface QuestionInfoMapper<T, P> extends BaseMapper<T, P> {

    /**
     * 根据QuestionId更新
     */
    Integer updateByQuestionId(@Param("bean") T t, @Param("questionId") Integer questionId);


    /**
     * 根据QuestionId删除
     */
    Integer deleteByQuestionId(@Param("questionId") Integer questionId);


    /**
     * 根据QuestionId获取对象
     */
    T selectByQuestionId(@Param("questionId") Integer questionId);

    void deleteBatchByQuestionId(@Param("questionIdArray") String[] questionIdArray, @Param("status") Integer status, @Param("userId") Integer userId);

    T showDetailNext(@Param("query") P p);
}
