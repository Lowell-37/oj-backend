package com.lowell.oj.judge.strategy;

import com.lowell.oj.model.dto.question.JudgeCase;
import com.lowell.oj.judge.codesandbox.model.JudgeInfo;
import com.lowell.oj.model.entity.Question;
import com.lowell.oj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @Date: 2025/3/16 13:58
 * @Author: Lowell
 * @Description:
 **/
@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
