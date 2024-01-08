package com.jackdaw.jinjobbackendadminservice.service.impl;

import com.jackdaw.jinjobbackendadminservice.mapper.AppUpdateMapper;
import com.jackdaw.jinjobbackendadminservice.service.AppUpdateService;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendcommon.config.AppConfig;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.constant.Constants;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUpdate;
import com.jackdaw.jinjobbackendmodel.entity.query.AppUpdateQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.SimplePage;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.enums.AppUpdateSatusEnum;
import com.jackdaw.jinjobbackendmodel.enums.AppUpdateTypeEnum;
import com.jackdaw.jinjobbackendmodel.enums.PageSize;
import com.jackdaw.jinjobbackendmodel.enums.ResponseCodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * app发布 业务接口实现
 */
@Service("appUpdateService")
public class AppUpdateServiceImpl implements AppUpdateService {


    @Resource
    private AppConfig appConfig;

    @Resource
    private AppUpdateMapper<AppUpdate, AppUpdateQuery> appUpdateMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<AppUpdate> findListByParam(AppUpdateQuery param) {
        return this.appUpdateMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AppUpdateQuery param) {
        return this.appUpdateMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<AppUpdate> findListByPage(AppUpdateQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<AppUpdate> list = this.findListByParam(param);
        PaginationResultVO<AppUpdate> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(AppUpdate bean) {
        return this.appUpdateMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<AppUpdate> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appUpdateMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<AppUpdate> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appUpdateMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(AppUpdate bean, AppUpdateQuery param) {
        StringTools.checkParam(param);
        return this.appUpdateMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(AppUpdateQuery param) {
        StringTools.checkParam(param);
        return this.appUpdateMapper.deleteByParam(param);
    }

    /**
     * 根据Id获取对象
     */
    @Override
    public AppUpdate getAppUpdateById(Integer id) {
        return this.appUpdateMapper.selectById(id);
    }

    /**
     * 根据Id修改
     */
    @Override
    public Integer updateAppUpdateById(AppUpdate bean, Integer id) {
        return this.appUpdateMapper.updateById(bean, id);
    }

    /**
     * 根据Id删除
     */
    @Override
    public Integer deleteAppUpdateById(Integer id) {
        return this.appUpdateMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUpdate(AppUpdate appUpdate, MultipartFile file) {
        AppUpdateQuery updateQuery = new AppUpdateQuery();
        updateQuery.setOrderBy("id desc");
        updateQuery.setSimplePage(new SimplePage(0, 1));
        List<AppUpdate> appUpdateList = appUpdateMapper.selectList(updateQuery);
        if (!appUpdateList.isEmpty()) {
            AppUpdate latest = appUpdateList.get(0);
            Long dbVersion = Long.parseLong(latest.getVersion().replace(".", ""));
            Long currentVersion = Long.parseLong(appUpdate.getVersion().replace(".", ""));
            if (appUpdate.getId() == null && currentVersion <= dbVersion) {
                throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"当前版本必须大于历史版本");
            }
            //更新，修改当前版本
            if (appUpdate.getId() != null && currentVersion <= dbVersion && !appUpdate.getId().equals(latest.getId())) {
                throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"当前版本必须大于历史版本");
            }
        }
        if (appUpdate.getId() == null) {
            appUpdate.setCreateTime(new Date());
            appUpdate.setStatus(AppUpdateSatusEnum.INIT.getStatus());
            appUpdateMapper.insert(appUpdate);
        } else {
            appUpdate.setStatus(null);
            appUpdate.setGrayscaleDevice(null);
            appUpdateMapper.updateById(appUpdate, appUpdate.getId());
        }

        if (file != null) {
            try {
                File folder = new File(appConfig.getProjectFolder() + Constants.APP_UPDATE_FOLDER);
                if (!folder.exists()) {
                    folder.mkdir();
                }
                AppUpdateTypeEnum typeEnum = AppUpdateTypeEnum.getByType(appUpdate.getUpdateType());
                file.transferTo(new File(folder.getAbsolutePath() + "/" + appUpdate.getId() + typeEnum.getSuffix()));
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"更新app失败");
            }
        }
    }

    @Override
    public void postUpdate(Integer id, Integer status, String deviceIds) {
        AppUpdateSatusEnum statusEnum = AppUpdateSatusEnum.getByStatus(status);
        if (statusEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (AppUpdateSatusEnum.GRAYSCALE == statusEnum && StringTools.isEmpty(deviceIds)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (AppUpdateSatusEnum.GRAYSCALE != statusEnum) {
            deviceIds = "";
        }
        AppUpdate appUpdate = new AppUpdate();
        appUpdate.setStatus(statusEnum.getStatus());
        appUpdate.setGrayscaleDevice(deviceIds);
        appUpdateMapper.updateById(appUpdate, id);
    }

    @Override
    public AppUpdate getLatestUpdate(String version, String deviceId) {
        return appUpdateMapper.selectLatestUpdate(version, deviceId);
    }
}