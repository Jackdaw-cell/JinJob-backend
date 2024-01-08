package com.jackdaw.jinjobbackenduserservice.service.impl;


import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserCollect;
import com.jackdaw.jinjobbackendmodel.entity.query.AppUserCollectQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.SimplePage;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.enums.CollectTypeEnum;
import com.jackdaw.jinjobbackendmodel.enums.PageSize;
import com.jackdaw.jinjobbackenduserservice.mapper.AppUserCollectMapper;
import com.jackdaw.jinjobbackenduserservice.service.AppUserCollectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 用户收藏 业务接口实现
 */
@Service("appUserCollectService")
public class AppUserCollectServiceImpl implements AppUserCollectService {

    @Resource
    private AppUserCollectMapper<AppUserCollect, AppUserCollectQuery> appUserCollectMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<AppUserCollect> findListByParam(AppUserCollectQuery param) {
        return this.appUserCollectMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AppUserCollectQuery param) {
        return this.appUserCollectMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<AppUserCollect> findListByPage(AppUserCollectQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<AppUserCollect> list = this.findListByParam(param);
        PaginationResultVO<AppUserCollect> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(AppUserCollect bean) {
        return this.appUserCollectMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<AppUserCollect> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appUserCollectMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<AppUserCollect> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appUserCollectMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(AppUserCollect bean, AppUserCollectQuery param) {
        StringTools.checkParam(param);
        return this.appUserCollectMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(AppUserCollectQuery param) {
        StringTools.checkParam(param);
        return this.appUserCollectMapper.deleteByParam(param);
    }

    /**
     * 根据CollectId获取对象
     */
    @Override
    public AppUserCollect getAppUserCollectByCollectId(Integer collectId) {
        return this.appUserCollectMapper.selectByCollectId(collectId);
    }

    /**
     * 根据CollectId修改
     */
    @Override
    public Integer updateAppUserCollectByCollectId(AppUserCollect bean, Integer collectId) {
        return this.appUserCollectMapper.updateByCollectId(bean, collectId);
    }

    /**
     * 根据CollectId删除
     */
    @Override
    public Integer deleteAppUserCollectByCollectId(Integer collectId) {
        return this.appUserCollectMapper.deleteByCollectId(collectId);
    }

    /**
     * 根据UserIdAndObjectIdAndCollectType获取对象
     */
    @Override
    public AppUserCollect getAppUserCollectByUserIdAndObjectIdAndCollectType(String userId, String objectId, Integer collectType) {
        return this.appUserCollectMapper.selectByUserIdAndObjectIdAndCollectType(userId, objectId, collectType);
    }

    /**
     * 根据UserIdAndObjectIdAndCollectType修改
     */
    @Override
    public Integer updateAppUserCollectByUserIdAndObjectIdAndCollectType(AppUserCollect bean, String userId, String objectId, Integer collectType) {
        return this.appUserCollectMapper.updateByUserIdAndObjectIdAndCollectType(bean, userId, objectId, collectType);
    }

    /**
     * 根据UserIdAndObjectIdAndCollectType删除
     */
    @Override
    public Integer deleteAppUserCollectByUserIdAndObjectIdAndCollectType(String userId, String objectId, Integer collectType) {
        return this.appUserCollectMapper.deleteByUserIdAndObjectIdAndCollectType(userId, objectId, collectType);
    }

    @Override
    public void saveCollect(String userId, String objectId, Integer collectType) {
        CollectTypeEnum collectTypeEnum = CollectTypeEnum.getByType(collectType);
        if (null == collectTypeEnum) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AppUserCollect dbCollect = this.appUserCollectMapper.selectByUserIdAndObjectIdAndCollectType(userId, objectId, collectType);
        if (dbCollect != null) {
            return;
        }
        AppUserCollect appUserCollect = new AppUserCollect();
        appUserCollect.setUserId(userId);
        appUserCollect.setCollectTime(new Date());
        appUserCollect.setObjectId(objectId);
        appUserCollect.setCollectType(collectType);
        this.appUserCollectMapper.insert(appUserCollect);
    }

    @Override
    public AppUserCollect showDetailNext(AppUserCollectQuery query, Integer type, Integer currentId) {
        if (type == null) {
            query.setCollectId(currentId);
        } else {
            query.setNextType(type);
            query.setCurrentId(currentId);
        }
        AppUserCollect collect = appUserCollectMapper.showDetailNext(query);
        if (collect == null && type == null) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"内容不存在");
        } else if (collect == null && type == -1) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"已经是第一条了");
        } else if (collect == null && type == 1) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"已经是最后一条了");
        }
        return collect;
    }
}