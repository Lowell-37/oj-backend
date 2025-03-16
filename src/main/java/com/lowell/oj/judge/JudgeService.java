package com.lowell.oj.judge;

import com.lowell.oj.model.entity.QuestionSubmit;
import com.lowell.oj.model.vo.QuestionSubmitVO;

/**
 * @Date: 2025/3/16 11:02
 * @Author:Lowell
 * @Description: 判题服务
 **/
public interface JudgeService {
    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);

}
