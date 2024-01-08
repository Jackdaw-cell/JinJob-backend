package com.jackdaw.jinjobbackendadminservice.service;

import com.jackdaw.jinjobbackendmodel.entity.po.AppDevice;
import com.jackdaw.jinjobbackendmodel.entity.query.AppDeviceQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 设备信息 业务接口
 */
public interface AppDeviceService {

    /**
     * 根据条件查询列表
     */
    List<AppDevice> findListByParam(AppDeviceQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(AppDeviceQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<AppDevice> findListByPage(AppDeviceQuery param);

    /**
     * 新增
     */
    Integer add(AppDevice bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<AppDevice> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<AppDevice> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(AppDevice bean, AppDeviceQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(AppDeviceQuery param);

    /**
     * 根据DeviceId查询对象
     */
    AppDevice getAppDeviceByDeviceId(String deviceId);


    /**
     * 根据DeviceId修改
     */
    Integer updateAppDeviceByDeviceId(AppDevice bean, String deviceId);


    /**
     * 根据DeviceId删除
     */
    Integer deleteAppDeviceByDeviceId(String deviceId);

    void reportData(AppDevice appDevice);
}