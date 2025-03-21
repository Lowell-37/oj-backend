package com.lowell.oj.judge.codesandbox.model;

import lombok.Data;

/**
 * @Date: 2025/3/12 10:35
 * @Author: Lowell
 * @Description:判题信息
 **/
@Data
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 内存消耗（kb）
     */
    private Long memory;

    /**
     * 消耗时间（ms）
     */
    private Long time;

}
