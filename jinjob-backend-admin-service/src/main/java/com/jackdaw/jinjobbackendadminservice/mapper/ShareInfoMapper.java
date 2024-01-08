package com.jackdaw.jinjobbackendadminservice.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 文章 数据库操作接口
 */
public interface ShareInfoMapper<T, P> extends BaseMapper<T, P> {

    /**
     * 根据ShareId更新
     */
    Integer updateByShareId(@Param("bean") T t, @Param("shareId") Integer shareId);


    /**
     * 根据ShareId删除
     */
    Integer deleteByShareId(@Param("shareId") Integer shareId);


    /**
     * 根据ShareId获取对象
     */
    T selectByShareId(@Param("shareId") Integer shareId);

    T showDetailNext(@Param("query") P p);
}
