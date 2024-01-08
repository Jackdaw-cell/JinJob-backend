package com.jackdaw.jinjobbackendadminservice.service.impl;


import com.jackdaw.jinjobbackendadminservice.mapper.AppDeviceMapper;
import com.jackdaw.jinjobbackendadminservice.service.AppDeviceService;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.entity.po.AppDevice;
import com.jackdaw.jinjobbackendmodel.entity.query.AppDeviceQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.SimplePage;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.enums.PageSize;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 设备信息 业务接口实现
 */
@Service("appDeviceService")
public class AppDeviceServiceImpl implements AppDeviceService {

    @Resource
    private AppDeviceMapper<AppDevice, AppDeviceQuery> appDeviceMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<AppDevice> findListByParam(AppDeviceQuery param) {
        return this.appDeviceMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AppDeviceQuery param) {
        return this.appDeviceMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<AppDevice> findListByPage(AppDeviceQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<AppDevice> list = this.findListByParam(param);
        PaginationResultVO<AppDevice> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(AppDevice bean) {
        return this.appDeviceMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<AppDevice> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appDeviceMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<AppDevice> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appDeviceMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(AppDevice bean, AppDeviceQuery param) {
        StringTools.checkParam(param);
        return this.appDeviceMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(AppDeviceQuery param) {
        StringTools.checkParam(param);
        return this.appDeviceMapper.deleteByParam(param);
    }

    /**
     * 根据DeviceId获取对象
     */
    @Override
    public AppDevice getAppDeviceByDeviceId(String deviceId) {
        return this.appDeviceMapper.selectByDeviceId(deviceId);
    }

    /**
     * 根据DeviceId修改
     */
    @Override
    public Integer updateAppDeviceByDeviceId(AppDevice bean, String deviceId) {
        return this.appDeviceMapper.updateByDeviceId(bean, deviceId);
    }

    /**
     * 根据DeviceId删除
     */
    @Override
    public Integer deleteAppDeviceByDeviceId(String deviceId) {
        return this.appDeviceMapper.deleteByDeviceId(deviceId);
    }

    @Override
    public void reportData(AppDevice appDevice) {
        Date curDate = new Date();
        appDevice.setCreateTime(curDate);
        appDevice.setLastUseTime(curDate);
        this.appDeviceMapper.insertOrUpdate(appDevice);
    }
}