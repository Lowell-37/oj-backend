package com.lowell.oj.judge.codesandbox;

import com.lowell.oj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.lowell.oj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.lowell.oj.judge.codesandbox.impl.ThirdPartyCodeSandbox;


/**
 * @Date: 2025/3/16 9:57
 * @Author: Lowell
 * @Description: 代码沙箱工厂（根据字符参数创建指定的代码沙箱实例）
 **/
public class CodeSandboxFactory {
    public static CodeSandbox newInstence(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }


}
