package com.jackdaw.jinjobbackendjudgeservice.judge.codesandbox;

import com.jackdaw.jinjobbackendmodel.entity.dto.codesandbox.ExecuteCodeRequest;
import com.jackdaw.jinjobbackendmodel.entity.dto.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
