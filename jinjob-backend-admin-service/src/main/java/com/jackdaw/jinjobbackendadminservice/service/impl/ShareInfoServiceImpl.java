package com.jackdaw.jinjobbackendadminservice.service.impl;

import com.jackdaw.jinjobbackendadminservice.mapper.ACommonMapper;
import com.jackdaw.jinjobbackendadminservice.mapper.ShareInfoMapper;
import com.jackdaw.jinjobbackendadminservice.service.ShareInfoService;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.constant.Constants;
import com.jackdaw.jinjobbackendmodel.entity.po.ShareInfo;
import com.jackdaw.jinjobbackendmodel.entity.query.ShareInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.SimplePage;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.enums.PageSize;
import com.jackdaw.jinjobbackendmodel.enums.ResponseCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 文章 业务接口实现
 */
@Service("shareInfoService")
public class ShareInfoServiceImpl implements ShareInfoService {

    @Resource
    private ShareInfoMapper<ShareInfo, ShareInfoQuery> shareInfoMapper;

    @Resource
    private ACommonMapper aCommonMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<ShareInfo> findListByParam(ShareInfoQuery param) {
        return this.shareInfoMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(ShareInfoQuery param) {
        return this.shareInfoMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<ShareInfo> findListByPage(ShareInfoQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<ShareInfo> list = this.findListByParam(param);
        PaginationResultVO<ShareInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(ShareInfo bean) {
        return this.shareInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<ShareInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.shareInfoMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<ShareInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.shareInfoMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(ShareInfo bean, ShareInfoQuery param) {
        StringTools.checkParam(param);
        return this.shareInfoMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(ShareInfoQuery param) {
        StringTools.checkParam(param);
        return this.shareInfoMapper.deleteByParam(param);
    }

    /**
     * 根据ShareId获取对象
     */
    @Override
    public ShareInfo getShareInfoByShareId(Integer ShareId) {
        return this.shareInfoMapper.selectByShareId(ShareId);
    }

    /**
     * 根据ShareId修改
     */
    @Override
    public Integer updateShareInfoByShareId(ShareInfo bean, Integer ShareId) {
        return this.shareInfoMapper.updateByShareId(bean, ShareId);
    }

    /**
     * 根据ShareId删除
     */
    @Override
    public Integer deleteShareInfoByShareId(Integer ShareId) {
        return this.shareInfoMapper.deleteByShareId(ShareId);
    }

    @Override
    public void saveShare(ShareInfo shareInfo, Boolean superAdmin) {
        if (null == shareInfo.getShareId()) {
            shareInfo.setCreateTime(new Date());
            this.shareInfoMapper.insert(shareInfo);
        } else {
            ShareInfo dbInfo = this.shareInfoMapper.selectByShareId(shareInfo.getShareId());
            //只能自己发布的才能修改
            if (!dbInfo.getCreateUserId().equals(shareInfo.getCreateUserId()) && !superAdmin) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            shareInfo.setCreateUserId(null);
            shareInfo.setCreateUserName(null);
            shareInfo.setCreateTime(null);
            this.shareInfoMapper.updateByShareId(shareInfo, shareInfo.getShareId());
        }
    }

    @Override
    public void delShareBatch(String shareIds, Integer userId) {
        String[] shareIdArray = shareIds.split(",");
        if (userId != null) {
            ShareInfoQuery infoQuery = new ShareInfoQuery();
            infoQuery.setShareIds(shareIdArray);
            List<ShareInfo> shareInfoList = this.shareInfoMapper.selectList(infoQuery);
            List<ShareInfo> currentUserDataList = shareInfoList.stream()
                    .filter(a -> !a.getCreateUserId().equals(String.valueOf(userId))).collect(Collectors.toList());
            if (!currentUserDataList.isEmpty()) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
        }
        ShareInfoQuery query = new ShareInfoQuery();
        query.setShareIds(shareIdArray);
        shareInfoMapper.deleteByParam(query);
    }

    @Override
    public ShareInfo showShareDetailNext(ShareInfoQuery query, Integer type, Integer currentId, Boolean updateReadCount) {
        if (type == null) {
            query.setShareId(currentId);
        } else {
            query.setNextType(type);
            query.setCurrentId(currentId);
        }
        ShareInfo shareInfo = shareInfoMapper.showDetailNext(query);
        if (shareInfo == null && type == -1) {
            throw new BusinessException("已经是第一条了");
        } else if (shareInfo == null && type == 1) {
            throw new BusinessException("已经是最后一条了");
        }
        if (updateReadCount && shareInfo != null) {
            //更新阅读数
            aCommonMapper.updateCount(Constants.TABLE_NAME_SHARE_INFO, 1, null, currentId);
            shareInfo.setReadCount(shareInfo.getReadCount() + 1);
        }
        return shareInfo;
    }
}