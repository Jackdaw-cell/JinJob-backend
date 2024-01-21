package com.jackdaw.jinjobbackendjudgeservice.judge.strategy.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jackdaw.jinjobbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.jackdaw.jinjobbackendjudgeservice.model.JudgeContext;
import com.jackdaw.jinjobbackendmodel.entity.dto.codesandbox.JudgeInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion.JudgeCase;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion.JudgeConfig;
import com.jackdaw.jinjobbackendmodel.enums.JudgeInfoMessageEnum;
import com.jackdaw.jinjobbackendmodel.enums.QuestionSubmitStatusEnum;

import java.util.List;
import java.util.Optional;

/**
 * Java 程序的判题策略
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        OjQuestionInfo ojQuestionInfo = judgeContext.getOjQuestionInfo();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
//        if (outputList.size() != inputList.size()) {
//            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
//            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
//            return judgeInfoResponse;
//        }
        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            if (outputList.get(i) == null){
                outputList.set(i,"");
            }
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoResponse.setMessage(StrUtil.format("回答错误,测试通过例：{}/{}\n错误测试例输出：{}\n",i,judgeCaseList.size(),inputList.get(i),outputList.get(i)));
                judgeInfoResponse.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
                return judgeInfoResponse;
            }
        }
        // 判断题目限制
        String judgeConfigStr = ojQuestionInfo.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();
        if (memory > needMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            judgeInfoResponse.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
            return judgeInfoResponse;
        }
        // Java 程序本身需要额外执行 10 秒钟
        long JAVA_PROGRAM_TIME_COST = 10000L; 
        if ((time - JAVA_PROGRAM_TIME_COST) > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            judgeInfoResponse.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        judgeInfoResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        return judgeInfoResponse;
    }
}
