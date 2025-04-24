# 在线判题系统

## 项目介绍

OJ（Online Judge），在线判题评测系统。用户可以选择题目，在线做题，编写代码并提交，系统会对用户提交的代码，根据出题人设置的答案，来判断用户提交的结果是否正确。如下图，北大OJ和杭电OJ：

<img src="https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202503101608144.png" alt="image-20250310160846645" style="zoom:30%;" />

<img src="https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202503101608921.png" style="zoom:30%;" />

**难点**

判题系统：用于在线评测编程题目代码的系统，能够根据用户提交的代码、出题人预设的答题目输入和输出用例，进行编译代码、运行代码、判断代码运行结果是否正确。

## OJ系统的常用概念

+ AC（Accepted）：用户提交的代码成功通过了系统的所有测试用例
+ 题目限制：时间限制、内存限制
+ 题目介绍：对题目背景、问题描述和相关要求的详细说明
+ 题目输入：用户代码需要处理的数据
+ 题目输出：用户代码根据输入数据处理后应给出的结果
+ 题目输入用例：用于测试用户代码的具体输入数据集合
+ 题目输出用例：与输入用例相对应的正确输出结果
+ 普通测评：管理员预先设置好题目的输入用例和对应的输出用例。当用户提交代码后，判题机会将这些输入用例依次输入到用户的代码中，模拟实际的运行环境。然后，判题机会收集用户代码的输出结果，并与标准答案的输出用例进行逐一比对。比如我输入1，你要输出2才是正确的；交给判题机去执行用户的代码，给用户的代码喂输入用例，比如1，看用户程序的执行结果是否和标准答案的输出一致。



## 项目流程

1. 项目介绍、项目调研、需求分析
2. 项目业务流程
3. 功能模块
4. 技术选型
5. 项目初始化
6. 项目开发
7. 测试
8. 优化
9. 代码提交、代码审核
10. 产品验收
11. 上线



## 实现核心

1）权限校验：谁能提交代码，谁不能提交代码

2）代码沙箱（安全沙箱）

+ 预防用户代码藏毒，如写一个木马文件、修改系统权限
+ 沙箱是一个隔离的、安全的环境，用户的代码不会影响到沙箱之外的系统运行
+ 资源分配：限制用户程序的占用资源，如系统就2G内存，用户使用多个for循环，疯狂抢占资源，占满内存，其他用户就无法使用了

3）判题规则：题目用例的对比，结果验证

4）任务调度：服务器资源有限，用户需要排队，按照顺序执行判题，而不是直接拒绝、



## 核心业务流程

![image-20250423163849622](https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504231638777.png)

![image-20250423163931351](https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504231639453.png)



## 系统功能

### 用户模块

1. 注册
2. 登录

### 题目模块

1. 创建题目（管理员）
1. 删除题目（管理员）
1. 搜索题目
1. 在线做题
1. 提交题目代码

### 判题模块

1. 提交判题（结果是否正确）
2. 错误处理（内存溢出、安全性、超时）
3. 实现代码沙箱
4. 开放接口（提供一个独立的新服务）



## 技术选型

### 前端

Vue3、Arco Design组件库、在线代码编辑器

### 后端

Java、SpringBoot、Spring Cloud Alibaba、RabbitMQ、Redis

## 结构设计

![image-20250423165532309](https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504231655405.png)

## 前端初始化

使用[ Vue CLI](https://cli.vuejs.org/zh/guide/installation.html)进行项目初始化

```bash
npm install -g @vue/cli
```

```bash
vue create oj-frontend
```

使用组件库[Arco Design Vue](https://arco.design/vue/docs/start)

```bash
npm install --save-dev @arco-design/web-vue
```

引入组件库，修改`main.ts`

```css
import { *createApp* } from "vue";
import App from "./App.vue";
import *router* from "./router";
import *store* from "./store";
import ArcoVue from "@arco-design/web-vue";
import "@arco-design/web-vue/dist/arco.css";

*createApp*(App).use(ArcoVue).use(*store*).use(*router*).mount("#app");
```

### 通用路由菜单

根据路由配置，自动生成菜单内容，实现更通用、更自动的菜单配置。

步骤：

1. 提取通用路由文件
2. 菜单组件读取路由，动态渲染菜单项
3. 绑定跳转事件
4. 同步路由的更新到菜单项高亮

### 全局状态管理

所有页面全局共享的变量，而不是局限在某一个页面中。

适合作为全局状态的数据：已登录用户信息（每个页面几乎都要用）

Vuex 的本质：给你提供了一套增删改查全局变量的 API，只不过可能多了一些功能（比如时间旅行）



### 全局权限管理

目标：能够直接以一套通用的机制，去定义哪个页面需要那些权限。而不用每个页面独立去判断权限，提高效率。

思路：

1. 在路由配置文件， 定义某个路由的访问权限
2. 在全局页面组件 app.vue 中，绑定一个全局路由监听。每次访问页面时，根据用户要访问页面的路由信息，先判断用户是否有对应的访问权限。
3. 如果有，跳转到原页面；如果没有，拦截或跳转到 401 鉴权或登录页

### 根据权限隐藏菜单

需求：只有具有权限的菜单，才对用户可见

原理：类似上面的控制路由显示隐藏，只要判断用户没有这个权限，就直接过滤掉

1）新建 access 目录，专门用一个文件来定义权限

```typescript
▼typescript复制代码/**
 * 权限定义
 */
const ACCESS_ENUM = {
  NOT_LOGIN: "notLogin",
  USER: "user",
  ADMIN: "admin",
};

export default ACCESS_ENUM;
```

2）定义一个公用的权限校验方法

为什么？因为菜单组件中要判断权限、权限拦截也要用到权限判断功能，所以抽离成公共方法

创建 checkAccess.ts 文件，专门定义检测权限的函数：

```typescript
▼typescript复制代码import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 检查权限（判断当前登录用户是否具有某个权限）
 * @param loginUser 当前登录用户
 * @param needAccess 需要有的权限
 * @return boolean 有无权限
 */
const checkAccess = (loginUser: any, needAccess = ACCESS_ENUM.NOT_LOGIN) => {
  // 获取当前登录用户具有的权限（如果没有 loginUser，则表示未登录）
  const loginUserAccess = loginUser?.userRole ?? ACCESS_ENUM.NOT_LOGIN;
  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    return true;
  }
  // 如果用户登录才能访问
  if (needAccess === ACCESS_ENUM.USER) {
    // 如果用户没登录，那么表示无权限
    if (loginUserAccess === ACCESS_ENUM.NOT_LOGIN) {
      return false;
    }
  }
  // 如果需要管理员权限
  if (needAccess === ACCESS_ENUM.ADMIN) {
    // 如果不为管理员，表示无权限
    if (loginUserAccess !== ACCESS_ENUM.ADMIN) {
      return false;
    }
  }
  return true;
};

export default checkAccess;
```

3）修改 GlobalHeader 动态菜单组件，根据权限来过滤菜单

注意，这里使用计算属性，是为了当登录用户信息发生变更时，触发菜单栏的重新渲染，展示新增权限的菜单项

```typescript
▼typescript复制代码const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    // 根据权限过滤菜单
    if (
      !checkAccess(store.state.user.loginUser, item?.meta?.access as string)
    ) {
      return false;
    }
    return true;
  });
});
```



## 前后端联调

> npm install openapi-typescript-codegen --save-dev

> openapi --input http://localhost:8121/api/v2/api-docs --output ./generated --client axios





## 数据库表设计

### 用户表

```sql
-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;
```

### 题目表

题目标题

题目内容：存放题目的介绍、输入输出提示、描述、具体的详情

题目标签（json 数组字符串）：栈、队列、链表、简单、中等、困难

题目答案：管理员 / 用户设置的标准答案

提交数、通过题目的人数等：便于分析统计（可以考虑根据通过率自动给题目打难易度标签）

判题相关字段：

> 如果说题目不是很复杂，用例文件大小不大的话，可以直接存在数据库表里 但是如果用例文件比较大，> 512 KB 建议单独存放在一个文件中，数据库中只保存文件 url（类似存储用户头像）

- 输入用例：1、2
- 输出用例：3、4
- 时间限制
- 内存限制

judgeConfig 判题配置（json 对象）：

- 时间限制 timeLimit
- 内存限制 memoryLimit

judgeCase 判题用例（json 数组）

- 每一个元素是：一个输入用例对应一个输出用例

```json
[
  {
    "input": "1 2",
    "output": "3 4"
  },
  {
    "input": "1 3",
    "output": "2 4"
  }
]
```

------

存 json 的好处：便于扩展，只需要改变对象内部的字段，而不用修改数据库表（可能会影响数据库）

```json
{
  "timeLimit": 1000,
  "memoryLimit": 1000,
  "stackLimit": 1000
}
```

存 json 的前提：

1. 你不需要根据某个字段去倒查这条数据
2. 你的字段含义相关，属于同一类的值
3. 你的字段存储空间占用不能太大

------

其他扩展字段：

- 通过率
- 判题类型

```sql
create table if not exists question
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    answer      text                               not null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNUm int      default 0                 not null comment '题目通过数',
    judgeCase   text                               null comment '判题用例（json数组）',
    judgeConfig text                               null comment '判题配置（json对象）',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;
```

### 题目提交表

该表用户存放用户提交对应题目的相关信息，如用户代码，判题结果等

判题信息（judgeInfo）

```json
{
    "message": "程序执行信息",
    "time": 1000, //单位为ms
    "memory":1000, //单位为kb
}
```

判题信息枚举值

+ Accepted 成功
+ Wrong Answer 答案错误
+ Compile Error 编译错误
+ Memory Limit Exceeded 内存溢出
+ Time Limit Exceeded 超时
+ Presentation Error 展示错误
+ Output Limit Exceeded 输出溢出
+ Waiting 等待
+ Dangerous Operation 危险操作
+ Runtime Error 运行错误
+ System Error 系统错误

```sql
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    judgeInfo  text                               null comment '判题 信息（Json对象）',
    status     int      default 0                 not null comment '判题状态（0-待判题  1- 判题中 2- 成功  3-失败）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_postId (questionId),
    index idx_userId (userId)
) comment '题目提交表';
```



## 后端接口开发

![image-20250423173258331](https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504231732542.png)

## 前端页面

+ 用户注册页面

+ 创建题目页面（管理员）

+ 题目管理页面

    + 查看（搜索）
    + 删除
    + 修改
    + 快捷创建

+ 题目列表页（用户）

+ 题目详情页（在线做题页）

    + 判题状态的查看

+ 题目提交列表页



接入组件

[markdown编辑器](https://github.com/pd4d10/bytemd)

在`main.ts`中引入

```ts
import 'bytemd/dist/index.css'
```

```vue
<template>
  <Editor :value="value" :plugins="plugins" @change="handleChange" />
</template>

<script>
import gfm from '@bytemd/plugin-gfm'
import { Editor, Viewer } from '@bytemd/vue'

const plugins = [
  gfm(),
  // Add more plugins here
]

export default {
  components: { Editor },
  data() {
    return { value: '', plugins }
  },
  methods: {
    handleChange(v) {
      this.value = v
    },
  },
}
</script>
```

[代码编辑器](https://github.com/microsoft/monaco-editor/blob/main/docs/integrate-esm.md)

```shell
npm i monaco-editor-webpack-plugin
```



## 判题机模块

判题模块：调用代码沙箱，把代码和输入交给代码沙箱执行

代码沙箱：只负责接受代码和输入，返回编译运行的结果，不负责判题（可作为独立的服务，提供给其他项目使用）

两个模块完全解耦

为什么代码沙箱要接受和输出一组运行用例？

如果每个用例单独调用一次代码沙箱，会调用多次接口、需要多次网络传输、程序要多次编译、记录程序的执行状态（重复的代码不重复编译）

### 代码沙箱架构开发

### 1）定义代码沙箱的接口，提高通用性

项目代码只调用接口，不调用具体的实现类

### 2）定义多种不同的代码沙箱实现

示例代码沙箱：跑通业务流程测试用

远程代码沙箱：实际调用接口的沙箱

第三方代码沙箱：现成的代码沙箱，https://github.com/criyle/go-judge

### 3）编写单元测试，验证单个代码沙箱的执行

```java
@SpringBootTest
class CodeSandboxTest {
    @Test
    void executeCode() {
        CodeSandbox codeSandbox = new RemoteCodeSandbox();
        String code = "int main() { }";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }
}
```

存在问题：我们把new代码沙箱对象写死了，如果后续需要改用其他代码沙箱，涉及修改大量代码，缺乏可拓展性，因此考虑引入工厂模式

### 4）使用工厂模式

根据用户传入的字符串参数（沙箱类别），来生成对应的代码沙箱实现类

**静态工厂模式**

```java
/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例）
 */
public class CodeSandboxFactory {
    /**
     * 创建代码沙箱示例
     *
     * @param type 沙箱类型
     * @return
     */
    public static CodeSandbox newInstance(String type) {
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
```

根据字符串动态生成实例：

```java
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNext()) {
        String type = scanner.next();
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        String code = "int main() { }";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
        .code(code)
        .language(language)
        .inputList(inputList)
        .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
    }
}
```

### 5）参数配置化

将项目中一些可以交给用户自定义的选项或者字符串，写到配置文件中

在application.yml配置文件中指定变量：

```yaml
# 代码沙箱配置
codesandbox:
  type: remote
```

在Spring的Bean中通过`@Value`注解获取：

```java
@Value("${codesandbox.type:example}")
private String type;
```

### 6）代码沙箱能力增强，使用代理模式

比如，我们需要在调用代码沙箱前，输出请求参数日志； 在代码沙箱调用后，输出响应结果日志，便于管理员去分析

传统的代码沙箱，每个代码沙箱类都写一遍`log.info？`难道每次调用代码沙箱前后都执行 log？

**使用代理模式**，提供一个 Proxy，**来增强代码沙箱的能力**（代理模式的作用就是增强能力）
原本：需要用户自己去调用多次

<img src="https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504232001715.png" alt="image-20250423200153660"  />

使用代理后：不仅不用改变原本的代码沙箱实现类，而且对调用者来说，调用方式几乎没有改变，也不需要在每个调用沙箱的地方去写统计代码。

![image-20250423200343038](https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504232003092.png)

代理模式的实现原理：
实现被代理的接口
通过构造函数接受一个被代理的接口实现类
调用被代理的接口实现类，在调用前后增加对应的操作
`CodeSandboxProxy`示例代码：

```java
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {
    
    private final CodeSandbox codeSandbox;
    // 也可以使用全参构造函数注解@AllArgsConstructor 
    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
```

使用方法;

```java
@Test
void executeCodeByProxy() {
    CodeSandBox codeSandbox = CodeSandboxFactory.newInstance(type);
    codeSandbox = new CodeSandBoxProxy(codeSandbox);
    String code = "int main() { }";
    String language = QuestionSubmitLanguageEnum.JAVA.getValue();
    List<String> inputList = Arrays.asList("1 2", "3 4");
    ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
        .code(code)
        .language(language)
        .inputList(inputList)
        .build();
    ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
    Assertions.assertNotNull(executeCodeResponse);
}

```

实现示例代码沙箱：

```java
/**
 * 示例代码沙箱（仅为了跑通业务流程）
 */
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
```



## 判题服务开发

定义单独的` judgeService` 类，而不是把所有判题相关的代码写到 `questionSubmitService` 里，有利于后续的模块抽离

```java
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
```

### 判题服务业务流程

1. 传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
2. 如果题目提交状态不为等待中，就不用重复执行了
3. 更改判题（题目提交）的状态为 “判题中”，防止重复执行，也能让用户即时看到状态
4. 调用沙箱，获取到执行结果
5. 根据沙箱的执行结果，设置题目的判题状态和信息

### 判断逻辑

1. 先判断沙箱执行的结果输出数量是否和预期输出数量相等
2. 依次判断每一项输出和预期输出是否相等
3. 判题题目的限制是否符合要求
4. 可能还有其他的异常情况

### 策略模式优化

判题策略可能会有很多种，比如：我们的代码沙箱本身执行程序需要消耗时间，这个时间可能不同的编程语言是不同的，比如沙箱执行 Java 要额外花 10 秒，可以采用策略模式，针对不同的情况，定义独立的策略，便于分别修改策略和维护。

实现步骤如下：

1. 定义判题策略接口，让代码更加通用化

```java
/**
 * 判题策略
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
```

2. 定义判题上下文对象，用于定义在策略中传递的参数（可以理解为一种 DTO）：

   ```java
   /**
    * 上下文（用于定义在策略中传递的参数）
    */
   @Data
   public class JudgeContext {
   
       private JudgeInfo judgeInfo;
   
       private List<String> inputList;
   
       private List<String> outputList;
   
       private List<JudgeCase> judgeCaseList;
   
       private Question question;
   
       private QuestionSubmit questionSubmit;
   }
   ```



3. 实现默认判题策略，先把 `judgeService`中的代码搬运过来

4. 再新增一种判题策略，通过 `if ... else ...`的方式选择使用哪种策略

5. 定义 `JudgeManager`，目的是尽量简化对判题功能的调用，让调用方写最少的代码、调用最简单。对于判题策略的选取，也是在 `JudgeManager` 里处理的

   示例代码：

   ```java
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
   ```

   判题程序：

   ```java
   @Service
   public class JudgeServiceImpl implements JudgeService {
   
       @Resource
       private QuestionService questionService;
   
       @Resource
       private QuestionSubmitService questionSubmitService;
   
       @Resource
       private JudgeManager judgeManager;
   
       @Value("${codesandbox.type:example}")
       private String type;
   
   
       @Override
       public QuestionSubmit doJudge(long questionSubmitId) {
           // 1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
           QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
           if (questionSubmit == null) {
               throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
           }
           Long questionId = questionSubmit.getQuestionId();
           Question question = questionService.getById(questionId);
           if (question == null) {
               throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
           }
           // 2）如果题目提交状态不为等待中，就不用重复执行了
           if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
               throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
           }
           // 3）更改判题（题目提交）的状态为 “判题中”，防止重复执行
           QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
           questionSubmitUpdate.setId(questionSubmitId);
           questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
           boolean update = questionSubmitService.updateById(questionSubmitUpdate);
           if (!update) {
               throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
           }
           // 4）调用沙箱，获取到执行结果
           CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
           codeSandbox = new CodeSandboxProxy(codeSandbox);
           String language = questionSubmit.getLanguage();
           String code = questionSubmit.getCode();
           // 获取输入用例
           String judgeCaseStr = question.getJudgeCase();
           List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
           List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
           ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                   .code(code)
                   .language(language)
                   .inputList(inputList)
                   .build();
           ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
           List<String> outputList = executeCodeResponse.getOutpuList();
           // 5）根据沙箱的执行结果，设置题目的判题状态和信息
           JudgeContext judgeContext = new JudgeContext();
           judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
           judgeContext.setInputList(inputList);
           judgeContext.setOutputList(outputList);
           judgeContext.setJudgeCaseList(judgeCaseList);
           judgeContext.setQuestion(question);
           judgeContext.setQuestionSubmit(questionSubmit);
           JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
           // 6）修改数据库中的判题结果
           questionSubmitUpdate = new QuestionSubmit();
           questionSubmitUpdate.setId(questionSubmitId);
           questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
           questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
           update = questionSubmitService.updateById(questionSubmitUpdate);
           if (!update) {
               throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
           }
           QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionId);
           return questionSubmitResult;
       }
   }
   ```

   Java判题策略：

   ```java
   /**
    * @Date: 2025/3/16 13:59
    * @Author: Lowell
    * @Description: Java程序判题策略
    **/
   public class JavaLanguageJudgeStrategy implements JudgeStrategy {
   
       /**
        * 执行判题
        * @param judgeContext
        * @return
        */
       @Override
       public JudgeInfo doJudge(JudgeContext judgeContext) {
           JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
           Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
           Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
           List<String> inputList = judgeContext.getInputList();
           List<String> outputList = judgeContext.getOutputList();
           Question question = judgeContext.getQuestion();
           List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
           JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
           JudgeInfo judgeInfoResponse = new JudgeInfo();
           judgeInfoResponse.setMemory(memory);
           judgeInfoResponse.setTime(time);
           // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
           if (outputList.size() != inputList.size()) {
               judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
               judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
               return judgeInfoResponse;
           }
           // 依次判断每一项输出和预期输出是否相等
           for (int i = 0; i < judgeCaseList.size(); i++) {
               JudgeCase judgeCase = judgeCaseList.get(i);
               if (!judgeCase.getOutput().equals(outputList.get(i))) {
                   judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                   judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                   return judgeInfoResponse;
               }
           }
           // 判断题目限制
           String judgeConfigStr = question.getJudgeConfig();
           JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
           Long needMemoryLimit = judgeConfig.getMemoryLimit();
           Long needTimeLimit = judgeConfig.getTimeLimit();
           if (memory > needMemoryLimit) {
               judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
               judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
               return judgeInfoResponse;
           }
           // Java 程序本身需要额外执行 10 秒钟
           long JAVA_PROGRAM_TIME_COST = 10000L;
           if ((time - JAVA_PROGRAM_TIME_COST) > needTimeLimit) {
               judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
               judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
               return judgeInfoResponse;
           }
           judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
           return judgeInfoResponse;
       }
   }
   ```

## 代码沙箱原生实现

代码沙箱的作用：**只负责接收代码和输入，返回编译运行的结果，不负责判题**（**不会管你提交的题目答案与标准答案是否正确**，相当于把结果返回之后在根据结果跟题目结果进行判断来看该此做题是否成功）**可以作为独立的项目或者服务，提供给其他需要执行代码的项目中使用。**



由于代码沙箱是能够通过API调用的独立服务，所以新建一个Spring Boot Web项目，最终这个项目要提供一个能够执行代码、操作代码沙箱的接口。



使用 IDEA 的Spring Boot项目初始化工具，选择Java8、Spring Boot2.7版本

### 修改沙箱项目启动配置

修改SpringBoot项目版本，在 `pom.xml` 中修改为与 backend一致的版本

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.2</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

端口配置：

```yaml
server:
  port: 8081
```

编写测试接口，验证是否能访问通：

```java
@RestController("/")
public class MainController {
    @Resource
    private JavaNativeCodeSandbox javaNativeCodeSandbox;

    @GetMapping("/health")
    public String healthCheck() {
        return "ok";
    }
}
```

### Java 原生实现代码沙箱

通过命令行执行

> Java程序执行流程：接收代码 => 编译代码（javac） => 执行代码（java）

程序示例代码，注意要去掉包名，放到沙箱项目的 resource 目录下：

```java
public class SimpleCompute {
    public static void main(String[] args) {
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        System.out.println("结果：" + (a + b));
    }
}
```

java 编译代码：

```cmd
javac {Java类代码路径}
```

编译后生成class文件![image-20250423212059970](https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504232121155.png) 使用javac 编译命令，用 -encoding utf-8 参数解决，中文乱码问题

`java`命令执行编译后的代码

```cmd
java -cp .  Main 1 2 
```

![image-20250423212640979](https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504232126058.png)

### 核心流程实现

核心思路：用程序代理人工，用程序来操作命令行完成编译执行代码

核心依赖：需要依赖 java 的进程类 Process

1. 把用户的代码保存为文件
2. 编译代码，得到 class 文件
3. 执行代码，得到输出结果
4. 收集整理输出结果
5. 文件清理，释放空间
6. 错误处理，提升程序健壮性

### 保存代码文件

引入Hutool工具类，提高操作文件效率

```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.8.8</version>
</dependency>
```

新建目录，将每个用户的代码都存放在独立目录下，通过 UUID 随机生成目录名，便于隔离和维护：

```java
// 获取用户工作文件路径
String userDir = System.getProperty("user.dir");
//  File.separator 区分不太系统的分隔符：\\ or /
String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
// 判断全局目录路径是否存在
if (!FileUtil.exist(globalCodePathName)) {
    // 不存在，则创建文件目录
    FileUtil.mkdir(globalCodePathName);
}
// 存在，则保存用户提交代码
// 把用户的代码隔离存放，
String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
// 实际存放文件的目录
String userCodePath = userCodeParentPath + File.separator + GLOBAL_CODE_CLASS_NAME;
File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
```

### 编译代码

使用 `Process` 类在终端执行命令

```java
String compileCmd = String.format("javac -encoding utf-8%s", userCodeFile.getAbsolutePath());
Process process = Runtime.getRuntime().exec(compileCmd)
```

执行 `process.waitFor` 等待程序执行完成，并通过返回的 `exitValue`判断程序是否正常返回，然后从 Process 的输入流 `inputStream`和错误流 `errorStream`获取控制台输出。

示例代码：

```java
@Override
public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
    List<String> inputList = executeCodeRequest.getInputList();
    String code = executeCodeRequest.getCode();
    String language = executeCodeRequest.getLanguage();

    // 1、将用户提交的代码保存为文件。
    // 获取用户工作文件路径
    String userDir = System.getProperty("user.dir");
    //  File.separator 区分不太系统的分隔符：\\ or /
    String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
    // 判断全局目录路径是否存在
    if (!FileUtil.exist(globalCodePathName)) {
        // 不存在，则创建文件目录
        FileUtil.mkdir(globalCodePathName);
    }
    // 存在，则保存用户提交代码
    // 把用户的代码隔离存放，
    String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
    // 实际存放文件的目录
    String userCodePath = userCodeParentPath + File.separator + GLOBAL_CODE_CLASS_NAME;
    File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
    // 2、编译代码，得到class文件
    String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
    try {
        Process compileProcess = Runtime.getRuntime().exec(compileCmd);
        // 等待 Process 程序执行完，获取错误码
        int exitValue = compileProcess.waitFor();
        // 正常退出
        if (exitValue == 0) {
            System.out.println("编译成功");
            // 通过进程获取正常输出到控制台的信息
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            // 逐行读取
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            System.out.println(compileOutputStringBuilder);
        } else {
            // 异常退出
            System.out.println("编译失败，错误码：" + exitValue);
            // 通过进程获取正常输出到控制台的信息
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            // 逐行读取
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            // 分批获取进程的错误输出
            BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            StringBuilder errorCompileOutputStringBuilder = new StringBuilder();
            // 逐行读取
            String errorCompileOutputLine;
            while ((errorCompileOutputLine = bufferedReader.readLine()) != null) {
                errorCompileOutputStringBuilder.append(errorCompileOutputLine);
            }
            System.out.println(compileOutputStringBuilder);
            System.out.println(errorCompileOutputStringBuilder);
        }
    } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
    }
    return null;
}
```

可以把上述代码提取为工具类`ProcessUtils`，执行进程并获取输出，并且使用`StringBuilder`拼接控制台输出信息：

```java
public class ProcessUtils {

    /**
     * 执行进程并获取进程信息
     *
     * @param runProcess
     * @param processState 执行进程状态
     * @return
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String processState) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            // 等待 Process 程序执行完，获取错误码
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);

            // 正常退出
            if (exitValue == 0) {
                System.out.println(processState + "成功");
                // 通过进程获取正常输出到控制台的信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileOutputStringBuilder.append(compileOutputLine);
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());
            } else {
                // 异常退出
                System.out.println(processState + "失败，错误码：" + exitValue);
                // 通过进程获取正常输出到控制台的信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileOutputStringBuilder.append(compileOutputLine);
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());
                // 分批获取进程的错误输出
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                StringBuilder errorCompileOutputStringBuilder = new StringBuilder();
                // 逐行读取
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = bufferedReader.readLine()) != null) {
                    errorCompileOutputStringBuilder.append(errorCompileOutputLine);
                }
                executeMessage.setErrorMessage(errorCompileOutputStringBuilder.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }
}
```

### 执行程序

同样是使用`Process` 类运行java命令，为了解决编译或运行时的编码中文乱码问题。在指令中添加

`-=Dfile.encoding=UTF-8`

```java
String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
```

上述命令适用于执行从输入参数（args）中获取值的代码

```java
// 3、执行程序
for (String inputArgs : inputList) {
    String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
    try {
        Process runProcess = Runtime.getRuntime().exec(runCmd);
        ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
        System.out.println(executeMessage);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```

部分 acm 赛制需要进行`Scanner`控制台输入。对此类程序，需要使用`OutPutStream`向程序终端发送参数，并及时获取结果，最后需要记得关闭字节流释放资源

示例代码：

```java
/**
 * 执行交互式进程并获取信息
 *
 * @param runProcess
 * @param args
 * @return
 */
public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String args) {
    ExecuteMessage executeMessage = new ExecuteMessage();

    try {
        // 向控制台输入程序
        OutputStream outputStream = runProcess.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        String[] s = args.split(" ");
        String join = StrUtil.join("\n", s) + "\n";
        outputStreamWriter.write(join);
        // 相当于按了回车，执行输入的发送
        outputStreamWriter.flush();

        // 分批获取进程的正常输出
        InputStream inputStream = runProcess.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder compileOutputStringBuilder = new StringBuilder();
        // 逐行读取
        String compileOutputLine;
        while ((compileOutputLine = bufferedReader.readLine()) != null) {
            compileOutputStringBuilder.append(compileOutputLine);
        }
        executeMessage.setMessage(compileOutputStringBuilder.toString());
        // 记得资源的释放，否则会卡死
        outputStreamWriter.close();
        outputStream.close();
        inputStream.close();
        runProcess.destroy();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return executeMessage;
}
```

###  整理输出

1. 通过 for 循环遍历执行结果，从中获取输出列表
2. 获取程序执行时间

可以使用 Spring 的 `StopWatch` 获取一段程序的执行时间：

```java
StopWatch stopWatch = new StopWatch();
stopWatch.start();
... 程序执行
stopWatch.stop();
stopWatch.getLastTaskTimeMillis(); // 获取时间
```

可以使用最大值来统计时间，便于后续判题服务计算程序是否超时：

```java
        // 4、整理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        // 执行错误
        List<String> outputList = new ArrayList<>();
        // 取用时最大值，便于判断是否超时
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                // 执行中存在错误
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long executeTime = executeMessage.getTime();
            if (executeTime != null) {
                maxTime = Math.max(maxTime, executeTime);
            }
        }
        // 正常执行完成
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemory();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
```

获取内存信息 // to-do

### 文件清理

防止服务器空间不足，删除代码目录：

```java
if (userCodeFile.getParentFile() != null) {
    boolean del = FileUtil.del(userCodeParentPath);
    System.out.println("删除" + (del ? "成功" : "失败"));
}
```

### 错误处理

封装一个错误处理方法，当程序抛出异常时，直接返回错误响应。

```java
private ExecuteCodeResponse getErrorResponse(Throwable e) {
    ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
    executeCodeResponse.setOutputList(new ArrayList<>());
    executeCodeResponse.setMessage(e.getMessage());
    // 表示代码沙箱错误
    executeCodeResponse.setStatus(2);
    executeCodeResponse.setJudgeInfo(new JudgeInfo());
    return executeCodeResponse;
}
```

## Java 程序异常情况

### 执行超时

占用时间资源，导致程序卡死，不释放资源：

```java
/**
 * 无限睡眠（阻塞程序执行）
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        long ONE_HOUR = 60 * 60 * 1000L;
        Thread.sleep(ONE_HOUR);
        System.out.println("睡完了");
    }
}
```

### 占用内存

占用内存资源，导致空间浪费：

```java
public class Main {

    public static void main(String[] args) throws InterruptedException {
        List<byte[]> bytes = new ArrayList<>();
        while (true) {
            bytes.add(new byte[10000]);
        }
    }
}
```

实际运行上述程序时，我们会发现，内存占用到达一定空间后，程序就自动报错：`java.lang.OutOfMemoryError: Java heap space`，而不是无限增加内存占用，直到系统死机，这是 JVM 的一个保护机制。

### 读文件，信息泄露

比如直接通过相对路径获取项目配置文件，获取到密码：

```java
/**
 * 读取服务器文件（文件信息泄露）
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "src/main/resources/application.yml";
        List<String> allLines = Files.readAllLines(Paths.get(filePath));
        System.out.println(String.join("\n", allLines));
    }
}
```

### 写文件，植入木马

可以直接向服务器上写入文件，比如一个木马程序：`java -version 2>&1`（示例命令）

1. `java -version` 用于显示 Java 版本信息。这会将版本信息输出到标准错误流（stderr）而不是标准输出流（stdout）。
2. `2>&1` 将标准错误流重定向到标准输出流。这样，Java 版本信息就会被发送到标准输出流。

```java
/**
 * 向服务器写文件（植入危险程序）
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "src/main/resources/木马程序.bat";
        String errorProgram = "java -version 2>&1";
        Files.write(Paths.get(filePath), Arrays.asList(errorProgram));
        System.out.println("异常程序执行成功！");
    }
}
```

### 运行其他程序

直接通过 Process 执行危险程序，或者电脑上的其他程序：

```java
/**
 * 运行其他程序（比如危险木马）
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "src/main/resources/木马程序.bat";
        Process process = Runtime.getRuntime().exec(filePath);
        process.waitFor();
        // 分批获取进程的正常输出
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        // 逐行读取
        String compileOutputLine;
        while ((compileOutputLine = bufferedReader.readLine()) != null) {
            System.out.println(compileOutputLine);
        }
        System.out.println("执行异常程序成功");
    }
}
```

### 执行高危操作

甚至都不用写木马文件，直接执行系统自带的危险命令！

- 比如删除服务器的所有文件 `rm -rf /`

- 比如执行 dir（windows）、ls（linux） 获取你系统上的所有文件信息

## Java 程序安全控制

针对上面的异常情况，分别有如下方案，提高程序安全性。

1. 超时控制
2. 限制给用户程序分配的资源
3. 限制代码 - 黑白名单
4. 限制用户的操作权限（文件、网络、执行等）
5. 运行环境隔离

### 超时控制

创建一个守护线程，超时后自动中断process实现

```java
// 超时控制
new Thread(() -> {
    try {
        Thread.sleep(TIME_OUT);
        System.out.println("程序运行超时，已经中断");
        runProcess.destroy();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}).start();
```

### 限制资源分配

不能让每个 java 进程的执行占用的 JVM 最大堆内存空间都和系统默认的一致

在启动 Java 程序时，可以指定 JVM 的参数：-Xmx256m（最大堆空间大小）

> 注意！-Xmx 参数、JVM 的堆内存限制，不等同于系统实际占用的最大资源，可能会超出。
>
>
>
> 如果需要更严格的内存限制，要在系统层面去限制，而不是 JVM 层面的限制。
>
>
>
> 如果是 Linux 系统，可以使用 `cgroup` 来实现对某个进程的 CPU、内存等资源的分配
>
> `cgroup`是 Linux 内核提供的一种机制，可以用来限制进程组（包括子进程）的资源使用，例如内存、CPU、磁盘 I/O 等。通过将 Java 进程放置在特定的 `cgroup`中，你可以实现限制其使用的内存和 CPU 数

常用的 JVM 启动参数

1.  内存相关参数
    + `-Xms`: 设置 JVM 的初始堆内存大小
    + `-Xmx`: 设置 JVM 的最大堆内存大小
    + `-Xss`: 设置线程的栈大小
    + `-XX:MaxMetaspaceSize`: 设置 Metaspace（元空间）的最大大小
    + `-XX:MaxDirectMemorySize`: 设置直接内存（Direct Memory）的最大大小
2.  垃圾回收相关参数
    + `-XX:+UseSerialGC`: 使用串行垃圾回收器
    + `-XX:+UseParallelGC`: 使用并行垃圾回收器
    + `-XX:+UseConcMarkSweepGC`: 使用 CMS 垃圾回收器
    + `-XX:+UseG1GC`: 使用 G1 垃圾回收器
3.  线程相关参数
    + `-XX:ParallelGCThreads`: 设置并行垃圾回收的线程数
    + `-XX:ConcGCThreads`: 设置并发垃圾回收的线程数
    + `-XX:ThreadStackSize`: 设置线程的栈大小
4.  JIT 编译器相关参数
    + `-XX:TieredStopAtLevel`: 设置 JIT 编译器停止编译的层次
5.  其他资源限制参数
    + `-XX:MaxRAM`: 设置 JVM 使用的最大内存



### 限制代码 - 黑白名单

先定义一个黑白名单，比如哪些操作是禁止的，可以就是一个列表

```java
// 黑名单
public static final List<String> blackList = Arrays.asList("Files", "exec");
```

还可以使用字典树代替列表存储单词，用 **更少的空间** 存储更多的敏感词汇，并且实现 **更高效** 的敏感词查找

字典树

字典树（Trie树，也称为前缀树）是一种树形数据结构，用于高效地存储和搜索字符串集合

优点如下：

1. 高效地存储和查找字符串集合，特别适合处理大量字符串的前缀匹配和前缀搜索
2. 提供了最长公共前缀的查找能力
3. 可以快速地查找指定前缀的所有字符串

<img src="https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504241303384.png" alt="image-20250424130344235" style="zoom: 50%;" />

在上图中，使用列表存储apple、app、api三个单词需要占用11个字符，而使用字典树只需要占用6个字符，大大节约空间



使用 HuTool 工具库的字典树工具类

1）初始化字典树，插入禁用词

```java
private static final WordTree WORD_TREE;

static {
    // 初始化字典树
    WORD_TREE = new WordTree();
    WORD_TREE.addWords(blackList);
}
```

2）校验用户代码是否包含禁用词

```java
// 校验代码是否包含有黑名单中命令
FoundWord foundWord = WORD_TREE.matchWord(code);
if (foundWord != null) {
    System.out.println("此文件包含敏感词：" + foundWord.getFoundWord());
    return null;
}
```

缺点：

1. 无法遍历所有的黑名单
2. 不同的编程语言，你对应的领域、关键词都不一样，限制人工成本很大

### 限制权限 - Java安全管理器

限制用户对文件、内存、CPU、网络等资源的操作和访问

Java 安全管理器（Security Manager）是 Java 提供的保护 JVM、Java 安全的机制，可以实现更严格的资源和操作限制

编写安全管理器，只需要继承 Security Manager

1. 所有权限放开

   ```java
   /**
    * 默认安全管理器
    */
   public class DefaultSecurityManager extends SecurityManager {
       // 检查所有的权限
       @Override
       public void checkPermission(Permission perm) {
           System.out.println("默认不做任何限制");
           System.out.println(perm);
           // super.checkPermission(perm);
       }
   }
   ```

2. 所有权限拒绝

   ```java
   /**
    * 禁用所有权限安全管理器
    */
   public class DenySecurityManager extends SecurityManager {
   
       // 检查所有的权限
       @Override
       public void checkPermission(Permission perm) {
           throw new SecurityException("权限异常：" + perm.toString());
       }
   }
   ```

3. 限制读权限

   ```java
   @Override
       public void checkRead(String file) {
           System.out.println(file);
           if (file.contains("C:\\code\\yuoj-code-sandbox")) {
               return;
           }
           throw new SecurityException("checkRead 权限异常：" + file);
       }
   ```

4. 限制写权限

   ```java
   @Override
   public void checkWrite(String file) {
       throw new SecurityException("checkWrite 权限异常：" + file);
   }
   ```

5. 限制执行文件权限

   ```java
   @Override
   public void checkExec(String cmd) {
   	throw new SecurityException("checkExec 权限异常：" + cmd);
   }
   ```

6. 限制网络连接权限

   ```java
   public void checkConnect(String host, int port) {
       throw new SecurityException("checkConnect 权限异常：" + host + ":" + port);
   }
   ```





实际情况下，不应该在主类（开发者自己写的程序）中做限制，只需要限制子程序的权限即可。

启动子进程执行命令时，设置安全管理器，而不是在外层设置（会限制住测试用例的读写和子命令的执行）。

具体操作如下：

1. 根据需要开发自定义的安全管理器（比如 `MySecurityManager`）
2. 复制 `MySecurityManager` 类到 `resources/security`目录下，**移除类的包名**
3. 手动输入命令编译 `MySecurityManager`类，得到 `class`文件
4. 在运行 java 程序时，指定安全管理器 class 文件的路径、安全管理器的名称。

```java
public class MySecurityManager extends SecurityManager {
    // 检查所有的权限
    @Override
    public void checkPermission(Permission perm) {
        super.checkPermission(perm);
    }

    // 检测程序是否可执行文件
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec 权限异常：" + cmd);
    }

    // 检测程序是否允许读文件

    @Override
    public void checkRead(String file) {
        System.out.println(file);
        if (file.contains("D:\\Project\\yupiProject\\oj-code-sandbox")) {
            return;
        }
        throw new SecurityException("checkRead 权限异常：" + file);
    }

    // 检测程序是否允许写文件
    @Override
    public void checkWrite(String file) {
        throw new SecurityException("checkWrite 权限异常：" + file);
    }

    // 检测程序是否允许删除文件
    @Override
    public void checkDelete(String file) {
        throw new SecurityException("checkDelete 权限异常：" + file);
    }

    // 检测程序是否允许连接网络
    @Override
    public void checkConnect(String host, int port) {
        throw new SecurityException("checkConnect 权限异常：" + host + ":" + port);
    }
}
```

**优点**：权限控制很灵活、实现简单

**缺点**：

如果要做比较严格的权限限制，需要自己去判断哪些文件、包名需要允许读写。粒度太细了，难以精细化控制

安全管理器本身也是 Java 代码，也有可能存在漏洞。本质上还是程序层面的限制，没深入系统的层面



### 运行环境隔离

操作系统层面上，把用户程序封装到沙箱里，和宿主机（我们的电脑 / 服务器）隔离开，使得用户的程序无法影响宿主机



## 代码沙箱Docker实现

为什么要用 Docker 容器技术？
**为了进一步提升系统的安全性，把不同的程序和宿主机进行隔离，使得某个程序（应用）的执行不会影响到系统本身**，Docker 技术可以实现程序和宿主机的隔离



镜像：用来创建容器的安装包，可以理解为给电脑安装操作系统的系统镜像

容器：通过镜像来创建的一套运行环境，一个容器里可以运行多个程序，可以理解为一个电脑实例
Dockerfile：制作镜像的文件，可以理解为制作镜像的一个清单

镜像仓库：存放镜像的仓库，用户可以从仓库下载现成的镜像，也可以把做好的镜像放到仓库里

![image-20250424162414476](https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504241624645.png)



### Java操作Docker

使用 Docker-Java：https://github.com/docker-java/docker-java
官方入门：https://github.com/docker-java/docker-java/blob/main/docs/getting_started.md
先引入依赖：

```xml
<!-- https://mvnrepository.com/artifact/com.github.docker-java/docker-java -->
<dependency>
    <groupId>com.github.docker-java</groupId>
    <artifactId>docker-java</artifactId>
    <version>3.3.0</version>
</dependency>
<!-- https://mvnrepository.com/artifact/com.github.docker-java/docker-java-transport-httpclient5 -->
<dependency>
    <groupId>com.github.docker-java</groupId>
    <artifactId>docker-java-transport-httpclient5</artifactId>
    <version>3.3.0</version>
</dependency>
```

`DockerClientConfig`：用于定义初始化 `DockerClient` 的配置（类比 MySQL 的连接、线程数配置)

`DockerHttpClient`（不推荐使用）：用于向 Docker 守护进程（操作 Docker 的接口）发送请求的客户端，低层封装，还要自己构建请求参数（简单地理解成 JDBC）

`DockerClient`（推荐）：才是真正和 Docker 守护进程交互的、最方便的 SDK，高层封装，对 `DockerHttpClient`再进行了一层封装（理解成 MyBatis），提供了现成的增删改查

### Dockers常用操作

1. 拉取镜像

   ```java
   public class DockerPullDemo {
       public static void main(String[] args) throws InterruptedException {
           DockerClient dockerClient = DockerClientBuilder.getInstance().build();
           // 1、拉取镜像
           String image = "nginx";
           PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
           PullImageResultCallback resultCallback = new PullImageResultCallback() {
               @Override
               public void onNext(PullResponseItem item) {
                   System.out.println("拉取镜像：" + item.getStatus());
                   super.onNext(item);
               }
           };
           pullImageCmd.exec(resultCallback).awaitCompletion();
           System.out.println("拉取完成");
       }
   }
   ```

2. 创建容器

   ```java
   // 2、创建容器
   String image = "nginx:latest";
   CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
   CreateContainerResponse createContainerResponse = containerCmd.withCmd("echo", "Hello Nginx").exec();
   System.out.println("容器id：" + createContainerResponse.getId());
   ```

3. 查看容器状态

   ```java
   // 3、查看容器状态
   ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
   List<Container> containerList = listContainersCmd.withShowAll(true).exec();
   for (Container container : containerList) {
       System.out.println(container);
   }
   ```

4. 启动容器

   ```java
   dockerClient.startContainerCmd(containerId).exec();
   ```

5. 查看日志

   ```java
   // 5、查看启动容器日志
   LogContainerResultCallback resultCallback = new LogContainerResultCallback() {
       @Override
       public void onNext(Frame item) {
           System.out.println(containerId + " 此容器日志:" + new String(item.getPayload()));
           super.onNext(item);
       }
   };
   dockerClient.logContainerCmd(containerId)
       .withStdErr(true) // 错误输出
       .withStdOut(true) // 标准输出
       .exec(resultCallback)
       .awaitCompletion(); // 异步操作
   ```

6. 删除容器

   ```java
   // 6、删除容器
   dockerClient.removeContainerCmd(containerId)
       .withForce(true) // 强制删除
       .exec();
   // 删除所有容器
   for (Container container : containerList) {
       dockerClient.removeContainerCmd(container.getId())
           .withForce(true) // 强制删除
           .exec();
   }
   ```

7. 删除镜像

   ```java
   // 7、删除镜像
   dockerClient.removeImageCmd(image).exec();
   ```



### Docker实现代码沙箱

docker 负责运行 java 程序，并且得到结果

1. 把用户的代码保存为文件
2. 编译代码，得到 class 文件
3. 把编译好的文件上传到容器环境内
4. 在容器中执行代码，得到输出结果
5. 收集整理输出结果
6. 文件清理，释放空间
7. 错误处理，提升程序健壮性

```java
@Component
public class JavaDockerCodeSandbox extends JavaCodeSandboxTemplate {

    private static final long TIME_OUT = 5000L;

    private static final Boolean FIRST_INIT = true;

    public static void main(String[] args) {
        JavaDockerCodeSandbox javaNativeCodeSandbox = new JavaDockerCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3"));
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/unsafeCode/RunFileError.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/simpleCompute/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    /**
     * 3、创建容器，把文件复制到容器内
     * @param userCodeFile
     * @param inputList
     * @return
     */
    @Override
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        // 获取默认的 Docker Client
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();

        // 拉取镜像
        String image = "openjdk:8-alpine";
        if (FIRST_INIT) {
            PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
            PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                @Override
                public void onNext(PullResponseItem item) {
                    System.out.println("下载镜像：" + item.getStatus());
                    super.onNext(item);
                }
            };
            try {
                pullImageCmd
                        .exec(pullImageResultCallback)
                        .awaitCompletion();
            } catch (InterruptedException e) {
                System.out.println("拉取镜像异常");
                throw new RuntimeException(e);
            }
        }

        System.out.println("下载完成");

        // 创建容器

        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(100 * 1000 * 1000L);
        hostConfig.withMemorySwap(0L);
        hostConfig.withCpuCount(1L);
        hostConfig.withSecurityOpts(Arrays.asList("seccomp=安全管理配置字符串"));
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/app")));
        CreateContainerResponse createContainerResponse = containerCmd
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true)
                .withReadonlyRootfs(true)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withTty(true)
                .exec();
        System.out.println(createContainerResponse);
        String containerId = createContainerResponse.getId();

        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();

        // docker exec keen_blackwell java -cp /app Main 1 3
        // 执行命令并获取结果
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            StopWatch stopWatch = new StopWatch();
            String[] inputArgsArray = inputArgs.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"}, inputArgsArray);
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .exec();
            System.out.println("创建执行命令：" + execCreateCmdResponse);

            ExecuteMessage executeMessage = new ExecuteMessage();
            final String[] message = {null};
            final String[] errorMessage = {null};
            long time = 0L;
            // 判断是否超时
            final boolean[] timeout = {true};
            String execId = execCreateCmdResponse.getId();
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onComplete() {
                    // 如果执行完成，则表示没超时
                    timeout[0] = false;
                    super.onComplete();
                }

                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)) {
                        errorMessage[0] = new String(frame.getPayload());
                        System.out.println("输出错误结果：" + errorMessage[0]);
                    } else {
                        message[0] = new String(frame.getPayload());
                        System.out.println("输出结果：" + message[0]);
                    }
                    super.onNext(frame);
                }
            };

            final long[] maxMemory = {0L};

            // 获取占用的内存
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(new ResultCallback<Statistics>() {

                @Override
                public void onNext(Statistics statistics) {
                    System.out.println("内存占用：" + statistics.getMemoryStats().getUsage());
                    maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
                }

                @Override
                public void close() throws IOException {

                }

                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            });
            statsCmd.exec(statisticsResultCallback);
            try {
                stopWatch.start();
                dockerClient.execStartCmd(execId)
                        .exec(execStartResultCallback)
                        .awaitCompletion(TIME_OUT, TimeUnit.SECONDS);
                stopWatch.stop();
                time = stopWatch.getLastTaskTimeMillis();
                statsCmd.close();
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                throw new RuntimeException(e);
            }
            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setTime(time);
            executeMessage.setMemory(maxMemory[0]);
            executeMessageList.add(executeMessage);
        }
        return executeMessageList;
    }
}
```

### 代码沙箱提供开放API

在 controller 暴露 CodeSandbox 定义的接口：

```java
@Resource
private JavaNativeCodeSandbox javaNativeCodeSandbox;
/**
* 执行代码接口
*
* @param executeCodeRequest
* @return
*/
@PostMapping("/executeCode")
public ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest) {
    if (executeCodeRequest == null) {
        throw new RuntimeException("请求参数错误");
    }
    return javaNativeCodeSandbox.executeCode(executeCodeRequest);
}
```

如果将服务不做任何的权限校验，直接发到公网，是不安全的。

1）调用方与服务提供方之间约定一个字符串 **（最好加密）**

优点：实现最简单，比较适合内部系统之间相互调用（相对可信的环境内部调用）

缺点：不够灵活，如果 key 泄露或变更，需要重启代码

代码沙箱服务，先定义约定的字符串

```java
// 定义鉴权请求头和密钥
private static final String AUTH_REQUEST_HEADER = "auth";

private static final String AUTH_REQUEST_SECRET = "secretKey";
```

改造请求，从请求头中获取认证信息，并校验

```java
    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request, HttpServletResponse response) {
        // 基本的认证
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_SECRET.equals(authHeader)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        return javaNativeCodeSandbox.executeCode(executeCodeRequest);
    }
```

调用方，在调用时补充请求头

```java
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://localhost:8081/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
```



## 微服务

服务：提供某类功能的代码

微服务：专注于提供某类特定功能的代码，而不是把所有的代码全部放到同一个项目里。会把整个大的项目按照一定的功能、逻辑进行拆分，拆分为多个子模块，每个子模块可以独立运行、独立负责一类功能，子模块之间相互调用、互不影响

微服务的几个重要的实现因素：**服务管理、服务调用、服务拆分**

### Spring Cloud Alibaba

在 Spring Cloud 的基础上，进行了增强，补充了一些额外的能力，根据阿里多年的业务沉淀做了一些定制化的开发

https://github.com/alibaba/spring-cloud-alibaba
中文文档：https://sca.aliyun.com/zh-cn/

1. Spring Cloud Gateway：网关
2. Nacos：服务注册和配置中心
3. Sentinel：熔断限流
4. Seata：分布式事务
5. RocketMQ：消息队列，削峰填谷
6. Docker：使用Docker进行容器化部署
7. Kubernetes：使用k8s进行容器化部署

### 分布式登录

1. application.yml 增加 redis 配置

```xml
<!-- redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
```

2. 主类取消Redis自动配置的移除

3. 修改session存储方式：

```yaml
spring:
  session:
    # 取消注释开启分布式 session（须先配置 Redis）
    store-type: redis
```

4. 使用 redis-cli 或者 redis 管理工具，查看是否有登录后的信息



### 微服务划分

**依赖服务**

- 注册中心：Nacos

- 微服务网关（oj-backend-gateway ，8101端口 ）：Gateway 聚合所有的接口，统一接受处理前端的请求



**公共模块**

- common 公共模块（oj-backend-common）：全局异常处理器、请求响应封装类、公用的工具类等

- model 模型模块（oj-backend-model）：很多服务公用的实体类

- 公用接口模块（oj-backend-service-client）：只存放接口，不存放实现（多个服务之间要共享）



**代码沙箱**（code-sandbox，8121 端口，只做判题，得到判题结果）

代码沙箱服务本身就是独立的，不用纳入 Spring Cloud 的管理

+ 执行提交的程序

+ 返回执行程序信息



**业务功能**

1. 用户服务（oj-backend-user-service：8102 端口）：

2. + 注册（后端已实现）
+ 登录（后端已实现，前端已实现）
+ 用户管理

3. 题目服务（oj-backend-question-service：8103 端口）

4. + 创建题目（管理员）
+ 删除题目（管理员）
+ 修改题目（管理员）
+ 搜索题目（用户）
+ 在线做题（题目详情页）
+ 题目提交

5. 判题服务（oj-backend-judge-service，8104端口，较重的操作）

6. + 执行判题逻辑
+ 错误处理（内存溢出、安全性、超时）
+ **自主实现** 代码沙箱（安全沙箱）
+ 开放接口（提供一个独立的新服务）

### 路由划分

用 springboot 的 context-path 统一修改各项目的接口前缀，比如：

1. 用户服务：

    - `/api/user`


- `/api/user/inner`（内部调用，网关层面要做限制）


2. 题目服务：

    - `/api/question`（也包括题目提交信息）


- `/api/question/inner`（内部调用，网关层面要做限制）


3. 判题服务：

    - `/api/judge`


- ``/api/judge/inner`（内部调用，网关层面要做限制）


### Nacos注册中心启动

教程：https://sca.aliyun.com/zh-cn/docs/2021.0.5.0/user-guide/nacos/overview

Nacos 官网教程：https://nacos.io/zh-cn/docs/quick-start.html

到官网下载 Nacos：https://github.com/alibaba/nacos/releases/tag/2.2.0

安装好后，进入 bin 目录启动：（单机启动）

```cmd
startup.cmd -m standalone
```

### 新建工程

创建完初始项目后，补充 Spring Cloud 依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-dependencies</artifactId>
    <version>2021.0.5</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

依次使用 new modules 和 spring boot Initializr 创建各模块

<img src="https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504241818372.png" alt="image-20250424181818194" style="zoom:50%;" />

### 服务内部调用

题目服务依赖用户服务，但是代码已经分到不同的包，找不到对应的 Bean。可以使用 Open Feign 组件实现跨服务的远程调用。

>  Open Feign：Http 调用客户端，提供了更方便的方式来让你远程调用其他服务，不用关心服务的调用地址
>
>  Nacos 注册中心获取服务调用地址



1）梳理服务的调用关系，确定哪些服务（接口）需要给内部调用

1. 用户服务：没有其他的依赖
2. 题目服务

- - `userService.getById(userId)`

- - `userService.getUserVO(user)`

- - `userService.listByIds(userIdSet)`

- - `userService.isAdmin(loginUser)`

- - `userService.getLoginUser(request)`

- - `judgeService.doJudge(questionSubmitId)`

3. 判题服务：

- - `questionService.getById(questionId)`

- - `questionSubmitService.getById(questionSubmitId)`

- - `questionSubmitService.updateById(questionSubmitUpdate)`



2）确认要提供哪些服务

1. 用户服务：没有其他的依赖

- - `userService.getById(userId)`

- - `userService.getUserVO(user)`

- - `userService.listByIds(userIdSet)`

- - `userService.isAdmin(loginUser)`

- - `userService.getLoginUser(request)`

2. 题目服务：

- - `questionService.getById(questionId)`

- - `questionSubmitService.getById(questionSubmitId)`

- - `questionSubmitService.updateById(questionSubmitUpdate)`

3. 判题服务：

- - `judgeService.doJudge(questionSubmitId)`



3）实现 client 接口

对于用户服务，有一些不利于远程调用参数传递、或者实现起来非常简单（工具类），可以直接用默认方法，无需远程调用，节约性能

开启 `openfeign` 的支持，把我们的接口暴露出去（服务注册到 Nacos 注册中心上），作为 API 给其他服务调用（其他服务从注册中心寻找）

```java
@FeignClient(name = "oj-backend-user-service",path = "/api/user")
```

需要修改每个服务提供者的 context-path 全局请求路径

-  服务提供者：理解为接口的实现类，实际提供服务的模块（服务注册到注册中心上）

-  服务消费者：理解为接口的调用方，需要去找到服务提供者，然后调用。（其他服务从注册中心寻找）



注意事项：

1.  要给接口的每个方法打上请求注解，注意区分 Get、Post

2.  要给请求参数打上注解，比如 Get请求 => RequestParam、Post 请求 => RequestBody

3.  FeignClient 定义的请求路径一定要和服务提供方实际的请求路径保持一致
    示例代码：

```java
/**
 * 公共用户服务接口
 *
 * @author Shier
 */
@FeignClient(name = "oj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    /**
     * 根据 id 获取用户信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") long userId);

    /**
     * 根据 id 获取用户列表
     *
     * @param idList
     * @return
     */
    @GetMapping("/get/ids")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
```



4. 修改各业务服务的调用代码为 feignClient

5. 编写 feignClient 服务的实现类，注意要和之前定义的客户端保持一致：在提供服务的模块通过实现公共接口，执行对应操作

```java
/**
 * 用户内部服务
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    /**
     * 根据 id 获取用户信息
     *
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }


    /**
     * 根据 id 获取用户列表
     *
     * @param idList
     * @return
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList) {
        return userService.listByIds(idList);
    }
}
```

6. 开启 Nacos 的配置，让服务之间能够互相发现

- -  所有模块引入 Nacos 依赖，然后给业务服务（包括网关）增加配置，上线得修改地址端口：

```yaml
spring:
  # 注册到 nacos 服务
  cloud:
    nacos:
      server-addr: 127.0.0.1::8848
```

- -  给业务**服务项目启动类**打上注解，开启服务发现、找到对应的客户端 Bean 的位置：

```java
@EnableDiscoveryClient
@EnableFeignClients(basePackages ={"com.lowell.ojbackendserviceclient.service"})
```



- -  全局引入负载均衡器依赖：

```xml
<!--负载均衡-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-loadbalancer</artifactId>
    <version>3.1.5</version>
</dependency>
```

7. 启动项目，测试依赖能否注入，能否完成相互调用

![image-20250424182609917](https://lowell-note.oss-cn-beijing.aliyuncs.com/typora-image202504241826005.png)

### 微服务网关

微服务网关（oj-backend-gateway ，8101端口）：Gateway 聚合所有的接口，统一接受处理前端的请求



为什么要用？

- 所有的服务端口不同，增大了前端调用成本

- 所有服务是分散的，你可需要集中进行管理、操作，比如集中解决跨域、鉴权、接口文档、服务的路由、接口安全性、流量染色、限流



- Gateway：想自定义一些功能，需要对这个技术有比较深的理解

- Gateway 是应用层网关：会有一定的业务逻辑（比如根据用户信息判断权限）

- Nginx 是接入层网关：比如每个请求的日志，通常没有业务逻辑



接口路由：统一地接受前端的请求，转发请求到对应的服务

如何找到路由？可以编写一套路由配置，通过 api 地址前缀来找到对应的服务。

配置如下

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
    gateway:
      routes:
        - id: oj-backend-user-service
          uri: lb://oj-backend-user-service
          predicates:
            - Path=/api/user/**
        - id: oj-backend-question-service
          uri: lb://oj-backend-question-service
          predicates:
            - Path=/api/question/**
        - id: oj-backend-judge-service
          uri: lb://oj-backend-judge-service
          predicates:
            - Path=/api/judge/**
  application:
    name: oj-backend-gateway
  main:
    web-application-type: reactive
server:
  port: 8101
```

首先要将 gateway 配置到nacos服务发现中心，就是要在启动类中配置注解 `@EnableDiscoveryClient` ，能让网关从nacos中获取的服务列表。但前端请求来的时候，网关就会先从nacos中找到对应的服务，然后将该请求转发到对应服务上。

- `routes`：指定了一组路由规则，用于定义客户端请求的转发目标。
-

每一个路由规则包含以下几个配置项：

- `id`：路由的唯一标识符，用于区分不同的路由规则。与实际的服务名字一致

- `uri`：指定了请求转发的目标服务的地址，使用 `lb://` 前缀表示从注册中心中获取服务。

- `predicates`：定义了请求匹配的条件，当请求满足条件时，将会进行转发。这里的 `Path=/api/user/**` 表示匹配以 `/api/user/` 开头的所有请求。



通过这样的配置，当客户端发送符合条件的请求时，Spring Cloud Gateway 网关将会根据路由规则（id）将请求转发到相应的目标服务上。这样，通过网关的统一入口，可以实现请求的动态转发和负载均衡，将请求分发到不同的服务上处理。



总结起来，上述配置项定义了一组路由规则，通过匹配请求的 URL 路径，将请求转发到相应的服务中。这样就实现了网关路由请求转发的功能。



## 消息队列解耦

选用 RabbitMQ 消息队列改造项目，解耦判题服务和题目服务，题目服务只需要向消息队列发消息，判题服务从消息队列中取消息去执行判题，然后异步更新数据库即可

### 代码引入

1. 引入依赖

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-amqp</artifactId>
   </dependency>
   ```

2. yml 中引入配置

   ```yaml
   spring:
     rabbitmq:
       host: localhost
       port: 5672
       password: guest
       username: guest
   ```



3. 创建交换机和队列，创建code队列和死信队列

   ```java
   /**
    * 用于创建测试程序用到的交换机和队列（只用在程序启动前执行一次）
    *
    * @author Shier
    */
   @Slf4j
   public class CodeMqInitMain {
   
       public static void doInitCodeMq() {
           try {
               ConnectionFactory factory = new ConnectionFactory();
               factory.setHost("8.134.37.7");
               factory.setPassword("kcsen123456");
               factory.setUsername("shier");
               Connection connection = factory.newConnection();
               Channel channel = connection.createChannel();
               String codeExchangeName = CODE_EXCHANGE_NAME;
               channel.exchangeDeclare(codeExchangeName, CODE_DIRECT_EXCHANGE);
   
               // 创建 code 队列
               String codeQueue = CODE_QUEUE;
               Map<String, Object> codeMap = new HashMap<>();
   
               // code队列绑定死信交换机
               codeMap.put("x-dead-letter-exchange", CODE_DLX_EXCHANGE);
               codeMap.put("x-dead-letter-routing-key", CODE_DLX_ROUTING_KEY);
               channel.queueDeclare(codeQueue, true, false, false, codeMap);
               channel.queueBind(codeQueue, codeExchangeName, CODE_ROUTING_KEY);
   
               // 创建死信队列和死信交换机
               // 创建死信队列
               channel.queueDeclare(CODE_DLX_QUEUE, true, false, false, null);
               // 创建死信交换机
               channel.exchangeDeclare(CODE_DLX_EXCHANGE, CODE_DIRECT_EXCHANGE);
   
               channel.queueBind(CODE_DLX_QUEUE, CODE_DLX_EXCHANGE, CODE_DLX_ROUTING_KEY);
               log.info("消息队列启动成功！");
           } catch (Exception e) {
               log.error("消息队列启动失败");
               e.printStackTrace();
           }
       }
   
       public static void main(String[] args) {
           doInitCodeMq();
       }
   }
   ```

4. 生产者代码

   ```java
   @Component
   public class CodeMqProducer {
       @Resource
       private RabbitTemplate rabbitTemplate;
   
       /**
        * 生产者发送消息
        *
        * @param exchange   发送消息到指定交换机
        * @param routingKey 消息标识
        * @param message    消息内容
        */
       public void sendMessage(String exchange, String routingKey, String message) {
           rabbitTemplate.convertAndSend(exchange, routingKey, message);
       }
   }
   ```

5. 消费者代码

   ```java
   @Component
   @Slf4j
   public class CodeMqConsumer {
   
       /**
        * 指定程序监听的消息队列和确认机制
        *
        * @param message
        * @param channel
        * @param deliveryTag
        */
       @SneakyThrows
       @RabbitListener(queues = {CODE_QUEUE}, ackMode = "MANUAL")
       private void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
           log.info("接收到消息 ： {}", message);
           if (message == null) {
               // 消息为空，则拒绝消息，进入死信队列
               channel.basicNack(deliveryTag, false, false);
               throw new BusinessException(ErrorCode.NULL_ERROR, "消息为空");
           }
           try {
               // 手动确认消息
               channel.basicAck(deliveryTag, false);
           } catch (IOException e) {
               // 消息为空，则拒绝消息，进入死信队列
               channel.basicNack(deliveryTag, false, false);
               throw new RuntimeException(e);
           }
       }
   }
   ```

6. 死信队列代码

   ```java
   @Component
   @Slf4j
   public class CodeMqFailConsumer {
   
       @Resource
       private QuestionFeignClient questionFeignClient;
   
       /**
        * 监听死信队列
        *
        * @param message
        * @param channel
        * @param dekivery
        */
       @SneakyThrows
       @RabbitListener(queues = {CODE_DLX_QUEUE}, ackMode = "MANUAL")
       public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long dekivery) {
           // 接收到失败的信息
           log.info("死信队列接受到的消息：{}", message);
           if (StringUtils.isBlank(message)) {
               channel.basicNack(dekivery, false, false);
               throw new BusinessException(ErrorCode.PARAMS_ERROR, "消息为空");
           }
           long questionSubmitId = Long.parseLong(message);
           QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
           if (questionSubmit == null) {
               channel.basicNack(dekivery, false, false);
               throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交的题目信息不存在");
           }
           // 把提交题目标为失败
           questionSubmit.setSubmitState(QuestionSubmitStatusEnum.FAILED.getValue());
   
           boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmit);
           if (!update) {
               log.info("处理死信队列消息失败,对应提交的题目id为:{}", questionSubmit.getId());
               throw new BusinessException(ErrorCode.PARAMS_ERROR, "处理死信队列消息失败");
           }
           // 确认消息
           channel.basicAck(dekivery, false);
       }
   }
   ```

7. 单元测试

   ```java
   @SpringBootTest
   class MyMessageProducerTest {
   
       @Resource
       private MyMessageProducer myMessageProducer;
   
       @Test
       void sendMessage() {
           myMessageProducer.sendMessage("code_exchange", "my_routingKey", "你好呀");
       }
   }
   ```

## 项目异步化改造

要传递的消息是什么？题目提交 id

题目服务中QuestionSubmitServiceImpl，把原本的本地异步执行改为向消息队列发送消息：

```java
// 生产者发送消息
codeMqProducer.sendMessage(CODE_EXCHANGE_NAME, CODE_ROUTING_KEY, String.valueOf(questionSubmitId));
// 执行判题服务
// CompletableFuture.runAsync(() -> {
//     judgeFeignClient.doJudge(questionSubmitId);
// });
```

判题服务中，监听消息，执行判题：

```java
/**
 * Mq 消费者
 *
 * @author Shier
 * CreateTime 2023/6/24 15:53
 */
@Component
@Slf4j
public class CodeMqConsumer {

    @Resource
    private JudgeService judgeService;

    /**
     * 指定程序监听的消息队列和确认机制
     *
     * @param message
     * @param channel
     * @param deliveryTag
     */
    @SneakyThrows
    @RabbitListener(queues = {CODE_QUEUE}, ackMode = "MANUAL")
    private void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("接收到消息 ： {}", message);
        if (message == null) {
            // 消息为空，则拒绝消息，进入死信队列
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NULL_ERROR, "消息为空");
        }
        long questionSubmitId = Long.parseLong(message);
        try {
            judgeService.doJudge(questionSubmitId);
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            // 消息为空，则拒绝消息，进入死信队列
            channel.basicNack(deliveryTag, false, false);
            throw new RuntimeException(e);
        }
    }
}
```

