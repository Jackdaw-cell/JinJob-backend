package com.jackdaw.jinjobbackendquestionservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.constant.CommonConstant;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.SqlUtils;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit.OjQuestionSubmitAddRequest;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit.OjQuestionSubmitQueryRequest;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionSubmit;
import com.jackdaw.jinjobbackendmodel.enums.QuestionSubmitLanguageEnum;
import com.jackdaw.jinjobbackendmodel.enums.QuestionSubmitStatusEnum;
import com.jackdaw.jinjobbackendmodel.entity.vo.OjQuestionSubmitVO;
import com.jackdaw.jinjobbackendquestionservice.mapper.QuestionSubmitMapper;
import com.jackdaw.jinjobbackendquestionservice.rabbitmq.MyMessageProducer;
import com.jackdaw.jinjobbackendquestionservice.service.OjQuestionService;
import com.jackdaw.jinjobbackendquestionservice.service.OjQuestionSubmitService;
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

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private MyMessageProducer myMessageProducer;

    /**
     * 提交题目
     *
     * @param ojQuestionSubmitAddRequest
     * @return
     */
    @Override
    public OjQuestionSubmit doQuestionSubmit(OjQuestionSubmitAddRequest ojQuestionSubmitAddRequest, String userId) {
        // 校验编程语言是否合法
        String language = ojQuestionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        long questionId = ojQuestionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        OjQuestionInfo ojQuestionInfo = ojQuestionService.getById(questionId);
        if (ojQuestionInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 每个用户串行提交题目
        OjQuestionSubmit ojQuestionSubmit = new OjQuestionSubmit();
        ojQuestionSubmit.setUserId(userId);
        ojQuestionSubmit.setQuestionId(questionId);
        String mainMethod = ojQuestionInfo.getMainMethod();
        //TODO:静态代码每个题目不同，要对应修改
        String static_code =
//                "public class Main {\n" +
                removeCharacter(mainMethod) +
                "\n{}" +
                "\n}";
        String str = StrUtil.format(static_code, ojQuestionSubmitAddRequest.getCode());
        ojQuestionSubmit.setCode(str);
        ojQuestionSubmit.setLanguage(language);
        // 设置初始状态
        ojQuestionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        ojQuestionSubmit.setJudgeInfo("{}");
        boolean save = this.save(ojQuestionSubmit);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        Long questionSubmitId = ojQuestionSubmit.getId();
        // 方法一：向消息队列推送消息
        // Judge服务判题信息从消息队列取出
//        myMessageProducer.sendMessage("code_exchange", "my_routingKey", String.valueOf(questionSubmitId));
        // 方法二：Feign调用判题服务
        //judge服务判题信息从FeignClient的HTTP请求取出
        try {
            OjQuestionSubmit ojQuestionSubmitResult = null;
            CompletableFuture<OjQuestionSubmit> future = CompletableFuture.supplyAsync(() -> {
                return judgeFeignClient.doJudge(questionSubmitId);
            });
            future.join();
            OjQuestionSubmit submit = future.get();
            if (submit.getStatus().equals(2)){
                userFeignClient.setCountOjQuestion(userId);
            }
            return submit;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


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

    public  String removeCharacter(String originalString) {

        // Check if the string is not empty
        if (!originalString.isEmpty()) {
            // Find the last index of '}'
            int lastIndex = originalString.lastIndexOf('}');

            if (lastIndex != -1) {
                // Remove the last occurrence of '}'
                String stringWithoutLastCharacter = originalString.substring(0, lastIndex) +
                        originalString.substring(lastIndex + 1);
                return stringWithoutLastCharacter;
            }
        }
        return originalString;
    }

}




