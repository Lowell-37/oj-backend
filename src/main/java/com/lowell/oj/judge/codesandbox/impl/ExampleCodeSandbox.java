package com.lowell.oj.judge.codesandbox.impl;

import com.lowell.oj.judge.codesandbox.CodeSandbox;
import com.lowell.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.lowell.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.lowell.oj.model.dto.questionsubmit.JudgeInfo;
import com.lowell.oj.model.enums.JudgeInfoMessageEnum;
import com.lowell.oj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * @Date: 2025/3/15 12:47
 * @Author: Lowell
 * @Description: 实力代码沙箱
 **/
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutpuList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
