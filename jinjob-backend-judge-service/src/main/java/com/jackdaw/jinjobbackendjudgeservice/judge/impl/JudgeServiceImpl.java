package com.jackdaw.jinjobbackendjudgeservice.judge.impl;

import cn.hutool.json.JSONUtil;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendjudgeservice.judge.JudgeService;
import com.jackdaw.jinjobbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.jackdaw.jinjobbackendjudgeservice.judge.codesandbox.factory.CodeSandboxFactory;
import com.jackdaw.jinjobbackendjudgeservice.judge.codesandbox.proxy.CodeSandboxProxy;
import com.jackdaw.jinjobbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.jackdaw.jinjobbackendjudgeservice.judge.strategy.impl.DefaultJudgeStrategy;
import com.jackdaw.jinjobbackendjudgeservice.judge.strategy.impl.JavaLanguageJudgeStrategy;
import com.jackdaw.jinjobbackendjudgeservice.model.JudgeContext;
import com.jackdaw.jinjobbackendmodel.entity.dto.codesandbox.ExecuteCodeRequest;
import com.jackdaw.jinjobbackendmodel.entity.dto.codesandbox.ExecuteCodeResponse;
import com.jackdaw.jinjobbackendmodel.entity.dto.codesandbox.JudgeInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion.JudgeCase;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionSubmit;
import com.jackdaw.jinjobbackendmodel.enums.QuestionSubmitStatusEnum;
import com.jackdaw.jinjobbackendserviceclient.service.question.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeignClient questionFeignClient;

//    @Resource
//    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;


    @Override
    public OjQuestionSubmit doJudge(long questionSubmitId) {
        // 1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        OjQuestionSubmit ojQuestionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (ojQuestionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = ojQuestionSubmit.getQuestionId();
        OjQuestionInfo ojQuestionInfo = questionFeignClient.getQuestionById(questionId);
        if (ojQuestionInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!ojQuestionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 3）更改判题（题目提交）的状态为 “判题中”，防止重复执行
        OjQuestionSubmit ojQuestionSubmitUpdate = new OjQuestionSubmit();
        ojQuestionSubmitUpdate.setId(questionSubmitId);
        ojQuestionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(ojQuestionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 4）调用沙箱，获取到执行结果
        //工厂模式 - 创建实例
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
       //代理模式 - 增强功能
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String language = ojQuestionSubmit.getLanguage();
        String code = ojQuestionSubmit.getCode();
        // 获取输入用例
        String judgeCaseStr = ojQuestionInfo.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        //执行代码沙箱 - 跑题
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        if (executeCodeResponse.getStatus() == 3) {
            ojQuestionSubmitUpdate = new OjQuestionSubmit();
            ojQuestionSubmitUpdate.setId(questionSubmitId);
            ojQuestionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
            ojQuestionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(executeCodeResponse.getJudgeInfo()));
            return ojQuestionSubmitUpdate;
        }
        if (executeCodeResponse.getOutputList().size() == 0) {
            ojQuestionSubmitUpdate = new OjQuestionSubmit();
            ojQuestionSubmitUpdate.setId(questionSubmitId);
            ojQuestionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
            ojQuestionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(executeCodeResponse.getJudgeInfo()));
            return ojQuestionSubmitUpdate;
        }
        List<String> outputList = executeCodeResponse.getOutputList();
        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setOjQuestionInfo(ojQuestionInfo);
        judgeContext.setOjQuestionSubmit(ojQuestionSubmit);
        //judge服务 - JAVA判题姬，启动！
        JudgeInfo judgeInfo = doJudge(judgeContext);
        // 6）修改数据库中的判题结果
        ojQuestionSubmitUpdate = new OjQuestionSubmit();
        ojQuestionSubmitUpdate.setId(questionSubmitId);
        ojQuestionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        ojQuestionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionFeignClient.updateQuestionSubmitById(ojQuestionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        OjQuestionSubmit ojQuestionSubmitResult = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        return ojQuestionSubmitResult;
    }

    /**
     * 选择判题策略
     *
     * @param judgeContext
     * @return
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        OjQuestionSubmit ojQuestionSubmit = judgeContext.getOjQuestionSubmit();
        String language = ojQuestionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
