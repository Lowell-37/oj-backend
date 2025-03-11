package com.lowell.oj.model.dto.question;

import lombok.Data;

/**
 * @Date: 2025/3/12 10:35
 * @Author: Lowell
 * @Description:
 **/
@Data
public class JudgeCase {
    /**
    输入用例
     */
    private String input;

    /**
    输出用例
     */
    private String output;
}
