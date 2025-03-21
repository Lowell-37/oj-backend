package com.lowell.oj.judge;

import com.lowell.oj.judge.codesandbox.model.JudgeInfo;
import com.lowell.oj.judge.strategy.DefaultJudgeStrategy;
import com.lowell.oj.judge.strategy.JavaLanguageJudgeStrategy;
import com.lowell.oj.judge.strategy.JudgeContext;
import com.lowell.oj.judge.strategy.JudgeStrategy;
import com.lowell.oj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * @Date: 2025/3/16 14:29
 * @Author: Lowell
 * @Description:
 **/
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}

