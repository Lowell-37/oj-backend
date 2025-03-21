package com.lowell.oj.judge.strategy;

import com.lowell.oj.judge.codesandbox.model.JudgeInfo;

/**
 * @Date: 2025/3/16 12:30
 * @Author:Lowell
 * @Description:
 **/
public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
