package com.lowell.oj.model.dto.question;

import lombok.Data;

/**
 * @Date: 2025/3/12 10:35
 * @Author: Lowell
 * @Description:
 **/
@Data
public class JudgeConfig {
    /**
     * 时间限制，单位为ms
     */
    private Long timeLimit;

    /**
     * 内存限制，单位为kb
     */
    private Long memoryLimit;

    /**
     * 堆栈限制，单位为kb
     */
    private Long stackLimit;
}
