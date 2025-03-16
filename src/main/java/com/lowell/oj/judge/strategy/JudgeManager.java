package com.lowell.oj.judge.strategy;

import com.lowell.oj.judge.JudgeServiceImpl;
import com.lowell.oj.model.dto.questionsubmit.JudgeInfo;
import com.lowell.oj.model.entity.Question;
import com.lowell.oj.model.entity.QuestionSubmit;
import org.apache.xmlbeans.impl.values.JavaStringHolder;
import org.elasticsearch.client.watcher.DeactivateWatchRequest;
import org.springframework.stereotype.Service;

/**
 * @Date: 2025/3/16 14:29
 * @Author: Lowell
 * @Description:
 **/
@Service
public class JudgeManager {
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("Java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
