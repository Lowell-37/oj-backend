package com.lowell.oj.judge.codesandbox.impl;

import com.lowell.oj.judge.codesandbox.CodeSandbox;
import com.lowell.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.lowell.oj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @Date: 2025/3/15 12:47
 * @Author: Lowell
 * @Description:
 **/
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
