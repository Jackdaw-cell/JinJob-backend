package com.jackdaw.jinjobbackendadminservice.service;

import com.jackdaw.jinjobbackendmodel.entity.po.ShareInfo;
import com.jackdaw.jinjobbackendmodel.entity.query.ShareInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 文章 业务接口
 */
public interface ShareInfoService {

    /**
     * 根据条件查询列表
     */
    List<ShareInfo> findListByParam(ShareInfoQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(ShareInfoQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<ShareInfo> findListByPage(ShareInfoQuery param);

    /**
     * 新增
     */
    Integer add(ShareInfo bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<ShareInfo> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<ShareInfo> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(ShareInfo bean, ShareInfoQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(ShareInfoQuery param);

    /**
     * 根据ShareId查询对象
     */
    ShareInfo getShareInfoByShareId(Integer ShareId);


    /**
     * 根据ShareId修改
     */
    Integer updateShareInfoByShareId(ShareInfo bean, Integer ShareId);


    /**
     * 根据ShareId删除
     */
    Integer deleteShareInfoByShareId(Integer ShareId);

    void saveShare(ShareInfo shareInfo, Boolean superAdmin);

    void delShareBatch(String ShareIds, Integer userId);

    ShareInfo showShareDetailNext(ShareInfoQuery query, Integer type, Integer currentId, Boolean updateReadCount);
}