package com.jackdaw.jinjobbackendadminservice.service;

import com.jackdaw.jinjobbackendmodel.entity.po.AppUserCollect;
import com.jackdaw.jinjobbackendmodel.entity.query.AppUserCollectQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 用户收藏 业务接口
 */
public interface AppUserCollectService {

    /**
     * 根据条件查询列表
     */
    List<AppUserCollect> findListByParam(AppUserCollectQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(AppUserCollectQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<AppUserCollect> findListByPage(AppUserCollectQuery param);

    /**
     * 新增
     */
    Integer add(AppUserCollect bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<AppUserCollect> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<AppUserCollect> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(AppUserCollect bean, AppUserCollectQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(AppUserCollectQuery param);

    /**
     * 根据CollectId查询对象
     */
    AppUserCollect getAppUserCollectByCollectId(Integer collectId);


    /**
     * 根据CollectId修改
     */
    Integer updateAppUserCollectByCollectId(AppUserCollect bean, Integer collectId);


    /**
     * 根据CollectId删除
     */
    Integer deleteAppUserCollectByCollectId(Integer collectId);


    /**
     * 根据UserIdAndObjectIdAndCollectType查询对象
     */
    AppUserCollect getAppUserCollectByUserIdAndObjectIdAndCollectType(String userId, String objectId, Integer collectType);


    /**
     * 根据UserIdAndObjectIdAndCollectType修改
     */
    Integer updateAppUserCollectByUserIdAndObjectIdAndCollectType(AppUserCollect bean, String userId, String objectId, Integer collectType);


    /**
     * 根据UserIdAndObjectIdAndCollectType删除
     */
    Integer deleteAppUserCollectByUserIdAndObjectIdAndCollectType(String userId, String objectId, Integer collectType);

    void saveCollect(String userId, String objectId, Integer collectType);

    AppUserCollect showDetailNext(AppUserCollectQuery query, Integer type, Integer currentId);
}