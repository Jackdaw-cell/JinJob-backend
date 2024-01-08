package com.jackdaw.jinjobbackendadminservice.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 用户收藏 数据库操作接口
 */
public interface AppUserCollectMapper<T, P> extends BaseMapper<T, P> {

    /**
     * 根据CollectId更新
     */
    Integer updateByCollectId(@Param("bean") T t, @Param("collectId") Integer collectId);


    /**
     * 根据CollectId删除
     */
    Integer deleteByCollectId(@Param("collectId") Integer collectId);


    /**
     * 根据CollectId获取对象
     */
    T selectByCollectId(@Param("collectId") Integer collectId);


    /**
     * 根据UserIdAndObjectIdAndCollectType更新
     */
    Integer updateByUserIdAndObjectIdAndCollectType(@Param("bean") T t, @Param("userId") String userId, @Param("objectId") String objectId, @Param("collectType") Integer collectType);


    /**
     * 根据UserIdAndObjectIdAndCollectType删除
     */
    Integer deleteByUserIdAndObjectIdAndCollectType(@Param("userId") String userId, @Param("objectId") String objectId, @Param("collectType") Integer collectType);


    /**
     * 根据UserIdAndObjectIdAndCollectType获取对象
     */
    T selectByUserIdAndObjectIdAndCollectType(@Param("userId") String userId, @Param("objectId") String objectId, @Param("collectType") Integer collectType);

    T showDetailNext(@Param("query") P p);
}
