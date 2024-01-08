package com.jackdaw.jinjobbackendquestionservice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.constant.CommonConstant;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendmodel.exception.ThrowUtils;
import com.jackdaw.jinjobbackendcommon.utils.SqlUtils;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion.JudgeCase;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion.OjQuestionQueryRequest;
import com.jackdaw.jinjobbackendmodel.entity.vo.OjQuestionVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.AppUserInfoVO;
import com.jackdaw.jinjobbackendquestionservice.mapper.QuestionMapper;
import com.jackdaw.jinjobbackendquestionservice.service.OjQuestionService;
import com.jackdaw.jinjobbackendserviceclient.service.user.UserFeignClient;
import org.apache.commons.collections4.CollectionUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
* @author Jackdaw
* @description@description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2023-08-07 20:58:00
*/
@Service
public class OjQuestionServiceImpl extends ServiceImpl<QuestionMapper, OjQuestionInfo>
    implements OjQuestionService {


    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 校验题目是否合法
     * @param ojQuestionInfo
     * @param add
     */
    @Override
    public void validQuestion(OjQuestionInfo ojQuestionInfo, boolean add) {
        if (ojQuestionInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = ojQuestionInfo.getTitle();
        String content = ojQuestionInfo.getContent();
        String tags = ojQuestionInfo.getTags();
        String answer = ojQuestionInfo.getAnswer();
        String judgeCase = ojQuestionInfo.getJudgeCase();
        String judgeConfig = ojQuestionInfo.getJudgeConfig();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param ojQuestionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<OjQuestionInfo> getQueryWrapper(OjQuestionQueryRequest ojQuestionQueryRequest) {
        QueryWrapper<OjQuestionInfo> queryWrapper = new QueryWrapper<>();
        if (ojQuestionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = ojQuestionQueryRequest.getId();
        String title = ojQuestionQueryRequest.getTitle();
        String content = ojQuestionQueryRequest.getContent();
        List<String> tags = ojQuestionQueryRequest.getTags();
        String answer = ojQuestionQueryRequest.getAnswer();
        String userId = ojQuestionQueryRequest.getUserId();
        String sortField = ojQuestionQueryRequest.getSortField();
        String sortOrder = ojQuestionQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public OjQuestionVO getQuestionVO(OjQuestionInfo ojQuestionInfo, HttpServletRequest request) {
        //取出少数用例作为展示用例
        String judgeCase = ojQuestionInfo.getJudgeCase();
        //json->JudgeCase->json
        try {
            // Convert JSON string array to List<JudgeCase>
            ObjectMapper objectMapper = new ObjectMapper();
            List<JudgeCase> judgeCases = objectMapper.readValue(judgeCase, objectMapper.getTypeFactory().constructCollectionType(List.class, JudgeCase.class));
            List<JudgeCase> firstTwoCases = new ArrayList<>(judgeCases.subList(0, Math.min(2, judgeCases.size())));
            String resultJsonString = objectMapper.writeValueAsString(firstTwoCases);
            ojQuestionInfo.setJudgeCase(resultJsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        OjQuestionVO ojQuestionVO = OjQuestionVO.objToVo(ojQuestionInfo);
        // 1. 关联查询用户信息
        String  userId = ojQuestionInfo.getUserId();
        AppUserInfo user = null;
        if (userId != null) {
            user = userFeignClient.getById(userId);
        }
        AppUserInfoVO userVO = userFeignClient.getUserVO(user);
        ojQuestionVO.setUserVO(userVO);
        return ojQuestionVO;
    }

//    @Override
//    public Page<OjQuestionVO> getQuestionVOPage(Page<OjQuestion> questionPage) {
//        List<OjQuestion> ojQuestionList = questionPage.getRecords();
//        Page<OjQuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
//        if (CollectionUtils.isEmpty(ojQuestionList)) {
//            return questionVOPage;
//        }
//        // 1. 关联查询用户信息
//        Set<String> userIdSet = ojQuestionList.stream().map(OjQuestion::getUserId).collect(Collectors.toSet());
//        Map<String, List<AppUserInfo>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
//                .collect(Collectors.groupingBy(AppUserInfo::getUserId));
//        // 填充信息
//        List<OjQuestionVO> ojQuestionVOList = ojQuestionList.stream().map(question -> {
//            OjQuestionVO ojQuestionVO = OjQuestionVO.objToVo(question);
//            String userId = question.getUserId();
//            AppUserInfo user = null;
//            if (userIdUserListMap.containsKey(userId)) {
//                user = userIdUserListMap.get(userId).get(0);
//            }
//            ojQuestionVO.setUserVO(userFeignClient.getUserVO(user));
//            return ojQuestionVO;
//        }).collect(Collectors.toList());
//        questionVOPage.setRecords(ojQuestionVOList);
//        return questionVOPage;
//    }

}




