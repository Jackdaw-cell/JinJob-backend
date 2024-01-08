package com.jackdaw.jinjobbackendquestionservice.service.impl;

import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.entity.po.AppFeedback;
import com.jackdaw.jinjobbackendmodel.entity.query.AppFeedbackQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.SimplePage;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.enums.FeedbackSendTypeEnum;
import com.jackdaw.jinjobbackendmodel.enums.FeedbackStatusEnum;
import com.jackdaw.jinjobbackendmodel.enums.PageSize;
import com.jackdaw.jinjobbackendquestionservice.mapper.AppFeedbackMapper;
import com.jackdaw.jinjobbackendquestionservice.service.AppFeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 问题反馈 业务接口实现
 */
@Service("appFeedbackService")
public class AppFeedbackServiceImpl implements AppFeedbackService {

    @Resource
    private AppFeedbackMapper<AppFeedback, AppFeedbackQuery> appFeedbackMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<AppFeedback> findListByParam(AppFeedbackQuery param) {
        List<AppFeedback> list = this.appFeedbackMapper.selectList(param);
        //将父级回复回复在上面
        if ((param.getPageNo() == null || param.getPageNo() == 1) && param.getPFeedbackId() != 0) {
            AppFeedback feedback = appFeedbackMapper.selectByFeedbackId(param.getPFeedbackId());
            if (!StringTools.isEmpty(param.getUserId()) && !feedback.getUserId().equals(param.getUserId())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            list.add(0, feedback);
        }

        return list;
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AppFeedbackQuery param) {
        return this.appFeedbackMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<AppFeedback> findListByPage(AppFeedbackQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<AppFeedback> list = this.findListByParam(param);
        PaginationResultVO<AppFeedback> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(AppFeedback bean) {
        return this.appFeedbackMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<AppFeedback> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appFeedbackMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<AppFeedback> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appFeedbackMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(AppFeedback bean, AppFeedbackQuery param) {
        StringTools.checkParam(param);
        return this.appFeedbackMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(AppFeedbackQuery param) {
        StringTools.checkParam(param);
        return this.appFeedbackMapper.deleteByParam(param);
    }

    /**
     * 根据FeedbackId获取对象
     */
    @Override
    public AppFeedback getAppFeedbackByFeedbackId(Integer feedbackId) {
        return this.appFeedbackMapper.selectByFeedbackId(feedbackId);
    }

    /**
     * 根据FeedbackId修改
     */
    @Override
    public Integer updateAppFeedbackByFeedbackId(AppFeedback bean, Integer feedbackId) {
        return this.appFeedbackMapper.updateByFeedbackId(bean, feedbackId);
    }

    /**
     * 根据FeedbackId删除
     */
    @Override
    public Integer deleteAppFeedbackByFeedbackId(Integer feedbackId) {
        return this.appFeedbackMapper.deleteByFeedbackId(feedbackId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveFeedBack4Client(AppFeedback appFeedback) {
        Date curDate = new Date();
        if (appFeedback.getPFeedbackId() != null && appFeedback.getPFeedbackId() != 0) {
            AppFeedback parentFeedback = this.appFeedbackMapper.selectByFeedbackIdAndUserId(appFeedback.getPFeedbackId(), appFeedback.getUserId());
            if (null == parentFeedback) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            //更新父级反馈
            AppFeedback parentUpdate = new AppFeedback();
            parentUpdate.setClientLastSendTime(curDate);
            parentUpdate.setStatus(FeedbackStatusEnum.NO_REPLY.getStatus());
            appFeedbackMapper.updateByFeedbackId(parentUpdate, parentFeedback.getFeedbackId());
        }
        appFeedback.setCreateTime(curDate);
        appFeedback.setStatus(FeedbackStatusEnum.NO_REPLY.getStatus());
        appFeedback.setSendType(FeedbackSendTypeEnum.CLIENT.getStatus());
        appFeedback.setClientLastSendTime(curDate);
        return appFeedbackMapper.insert(appFeedback);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer replyFeedback(AppFeedback appFeedback) {
        AppFeedback pFeedback = appFeedbackMapper.selectByFeedbackId(appFeedback.getPFeedbackId());
        if (null == pFeedback) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //插入反馈
        Date curDate = new Date();
        appFeedback.setCreateTime(curDate);
        appFeedback.setStatus(FeedbackStatusEnum.NO_REPLY.getStatus());
        appFeedback.setSendType(FeedbackSendTypeEnum.ADMIN.getStatus());
        appFeedback.setUserId(pFeedback.getUserId());
        appFeedback.setNickName(pFeedback.getNickName());
        Integer insert = appFeedbackMapper.insert(appFeedback);
        if (insert>0){
            //更新父级反馈状态
            AppFeedback parentUpdate = new AppFeedback();
            parentUpdate.setStatus(FeedbackStatusEnum.REPLY.getStatus());
            return appFeedbackMapper.updateByFeedbackId(parentUpdate, appFeedback.getPFeedbackId());
        }
        return -1;
    }
}