package com.lowell.oj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.lowell.oj.model.dto.question.JudgeCase;
import com.lowell.oj.model.dto.question.JudgeConfig;
import com.lowell.oj.model.dto.questionsubmit.JudgeInfo;
import com.lowell.oj.model.entity.Question;
import com.lowell.oj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * @Date: 2025/3/16 13:59
 * @Author: Lowell
 * @Description: 默认判题策略
 **/
public class DefaultJudgeStrategy implements JudgeStrategy {

    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        JudgeInfo judgeInfo1Response = new JudgeInfo();
        judgeInfo1Response.setMemory(memory);
        judgeInfo1Response.setTime(time);

        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = judgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfo1Response.setMessage(judgeInfoMessageEnum.getValue());

            return judgeInfo1Response;

        }
        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfo1Response.setMessage(judgeInfoMessageEnum.getValue());

                return judgeInfo1Response;
            }

        }
        //判断题目限制

        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getMemoryLimit();
        if (memory > needMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfo1Response.setMessage(judgeInfoMessageEnum.getValue());

            return judgeInfo1Response;
        }

        if (time > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfo1Response.setMessage(judgeInfoMessageEnum.getValue());

            return judgeInfo1Response;
        }
        return judgeInfo1Response;

    }
}
