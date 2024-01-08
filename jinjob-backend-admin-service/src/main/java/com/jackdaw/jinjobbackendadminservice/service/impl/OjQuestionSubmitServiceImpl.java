package com.jackdaw.jinjobbackendadminservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackdaw.jinjobbackendadminservice.mapper.QuestionSubmitMapper;
import com.jackdaw.jinjobbackendadminservice.service.OjQuestionService;
import com.jackdaw.jinjobbackendadminservice.service.OjQuestionSubmitService;
import com.jackdaw.jinjobbackendcommon.utils.SqlUtils;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.constant.CommonConstant;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit.OjQuestionSubmitAddRequest;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit.OjQuestionSubmitQueryRequest;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionSubmit;
import com.jackdaw.jinjobbackendmodel.entity.vo.OjQuestionSubmitVO;
import com.jackdaw.jinjobbackendmodel.enums.QuestionSubmitLanguageEnum;
import com.jackdaw.jinjobbackendmodel.enums.QuestionSubmitStatusEnum;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendserviceclient.service.judge.JudgeFeignClient;
import com.jackdaw.jinjobbackendserviceclient.service.user.UserFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
* @author Jackdaw
* @description@description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2023-08-07 20:58:53
*/
@Service
public class OjQuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, OjQuestionSubmit>
    implements OjQuestionSubmitService {
    
    @Resource
    private OjQuestionService ojQuestionService;

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param ojQuestionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<OjQuestionSubmit> getQueryWrapper(OjQuestionSubmitQueryRequest ojQuestionSubmitQueryRequest) {
        QueryWrapper<OjQuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (ojQuestionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = ojQuestionSubmitQueryRequest.getLanguage();
        Integer status = ojQuestionSubmitQueryRequest.getStatus();
        Long questionId = ojQuestionSubmitQueryRequest.getQuestionId();
        String  userId = ojQuestionSubmitQueryRequest.getUserId();
        String sortField = ojQuestionSubmitQueryRequest.getSortField();
        String sortOrder = ojQuestionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public OjQuestionSubmitVO getQuestionSubmitVO(OjQuestionSubmit ojQuestionSubmit, AppUserInfo loginUser) {
        OjQuestionSubmitVO ojQuestionSubmitVO = OjQuestionSubmitVO.objToVo(ojQuestionSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        String userId = loginUser.getUserId();
        // 处理脱敏
        if (userId != ojQuestionSubmit.getUserId()) {
            ojQuestionSubmitVO.setCode(null);
        }
        return ojQuestionSubmitVO;
    }

    @Override
    public Page<OjQuestionSubmitVO> getQuestionSubmitVOPage(Page<OjQuestionSubmit> questionSubmitPage, AppUserInfo loginUser) {
        List<OjQuestionSubmit> ojQuestionSubmitList = questionSubmitPage.getRecords();
        Page<OjQuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(ojQuestionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<OjQuestionSubmitVO> ojQuestionSubmitVOList = ojQuestionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(ojQuestionSubmitVOList);
        return questionSubmitVOPage;
    }

}




