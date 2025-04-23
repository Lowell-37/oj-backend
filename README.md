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
2. 删除题目（管理员）
3. 搜索题目
4. 在线做题
5. 提交题目代码

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

