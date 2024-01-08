package com.jackdaw.jinjobbackendjudgeservice.judge.codesandbox.impl;

import com.jackdaw.jinjobbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.jackdaw.jinjobbackendmodel.entity.dto.codesandbox.ExecuteCodeRequest;
import com.jackdaw.jinjobbackendmodel.entity.dto.codesandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用网上现成的代码沙箱）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
