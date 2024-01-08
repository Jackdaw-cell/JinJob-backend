package com.jackdaw.jinjobbackendadminservice.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据库操作接口
 */
public interface ExamQuestionItemMapper<T, P> extends BaseMapper<T, P> {

    /**
     * 根据ItemId更新
     */
    Integer updateByItemId(@Param("bean") T t, @Param("itemId") Integer itemId);


    /**
     * 根据ItemId删除
     */
    Integer deleteByItemId(@Param("itemId") Integer itemId);


    /**
     * 根据ItemId获取对象
     */
    T selectByItemId(@Param("itemId") Integer itemId);

    void deleteBatch(@Param("itemIdList") List<Integer> itemIdList);

    void deleteBatchByQuestionId(@Param("questionIdArray") String[] questionIdArray, @Param("status") Integer status, @Param("userId") Integer userId);

}
