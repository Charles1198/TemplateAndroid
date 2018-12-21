# TemplateAndroid

## 概述

这是一个 Android 模版工程，根据本人三年来开发经验制作，规定了项目基础模块划分、文件夹结构、代码规范、变量命名规范等。目的是规范开发流程，省去项目初始化步骤，方便新人上手，方便合作开发。

## 功能

## 项目结构

### 层次划分

目前工程结构比较简单，工程结构分为 3 层：App 壳工程、业务层、基础组件层，如下图所示。

![](./readmeImage/image_layer.png)

#### App 壳工程

App 壳工程作为程序的唯一入口，仅执行项目初始化的一些操作，负责管理各个业务组件，不参与任何业务实现。

#### 业务层

业务层包含了应用程序每个业务模块的具体实现，包括公共业务登陆/注册等。

#### 基础组件层

基础组件层包括程序中每个业务模块用到的开源库的集成、二次封装， 各种工具类，通用 UI 等，可以用在本公司各个项目中，其中的代码一般不用改动。

### 模块划分

该项目按照层次和功能进一步划分为若干个模块：基础组件模块 common、resource，App 壳工程模块 app， 以及基础业务模块 Begin、 Login、User，其余就是根据每个项目不同建立各自的业务模块（如 Module1）。各个模块及依赖关系如下图所示。

![](./readmeImage/image_modules.png)

#### common

common 模块属于业务不相关模块，包含工程中所有需要引入的第三方库、第三方库的封装、各种 Util、各种基类、通用的自定义 View 等，不包含任何具体业务实现，可以用在不同的项目中，其中的代码一般不用动。

#### resource

resource 模块属于业务强相关模块，存放项目中用到的视频、图片、字符串、颜色值等资源。

#### Begin

Begin 模块属于业务不相关模块，是程序的入口，有一个初始化页面和引导页，在程序被唤起的时候进入初始化页同时进行程序的初始化。

#### Login

Login 模块属于业务不相关模块，不同的程序可以共用，在某些程序中可以没有该模块。包含三个页面：登陆页面、注册页面、找回密码页面，执行用户登录注册等基础业务。

#### User

User 模块即个人中心模块，属于业务弱相关模块，需要根据业务需求做调整。User 模块提供个人信息管理、用户反馈、检查更新、退出账号等基本功能。

#### Module1

Module1 模块是业务模块，根据业务需求进行编写，有必要的话可以建立多个不同模块。

## 模块功能及使用说明

### common

common 模块文件结构如下所示：

```
-commom
|...
|-java
    |-com
        |-charles
            |-common
                |-base
                |-device
                |-erroe
                |-kv
                |-multiple
                |-network
                |-update
                |-videoview
                |-Constant.java
```

#### base 文件夹

base 文件夹中存放一些基类，其文件结构如下所示：

```
-base
    |-BaseActivity.java
    |-BaseApplicition.java
    |-BaseFragment.java
    |-BasePresenter.java
    |-BaseView.java
```

- BaseApplicition.java

  BaseApplicition.java 继承自 Application， 提供了全局的 Context， 执行某些初始化操作。

  每个 Android App 运行时，会首先自动创建 Application 类并实例化 Application 对象，且只有一个，它的生命周期贯穿 App 的始终。

- BaseActivity.java

  BaseActivity.java 继承自 AppCompatActivity，是程序中所有 Activity 的基类（也可根据情况不继承它），提供了每个 Activity 都可能用到的一些公用方法，如显示 Loading、Alert 弹框、监听与安装程序更新等。

- BaseFragment.java

  BaseFragment.java 继承自 Fragment， 是程序中所有 Fragment 的基类（也可根据情况不继承它），提供了每个 Fragment 都可能用到的一些公用方法。

- BasePresenter.java

  BasePresenter.java （其作用会在后面讲到）是程序中所有 Fragment 的基类（也可根据情况不继承它），提供了每个 Fragment 都可能用到的一些公用方法。

- BaseView

  BaseView 是一个接口（其作用会在后面讲到），是 presenter 和 Activity 之间通信的桥梁。程序中每个 xxView 都应该继承它。

#### device 文件夹

device 文件夹中有两个类，顾名思义是收集设备硬件信息，其文件结构如下所示：

```
device
    |-DeviceInfo.java
    |-GetDeviceInfo.java
```

- DeviceInfo.java

  DeviceInfo.java 是一个对象，规定了设备硬件信息的属性。属性含义详见类内注释。

  ```java
  public class DeviceInfo {
      private String deviceID;
      private String model;
      private String size;
      private String cpu;
      private String memory;

      ...
  }
  ```

- GetDeviceInfo.java

  GetDeviceInfo.java 是一个获取 DeviceInfo 各个属性的工具类。

#### error

error 文件夹下存放程序崩溃信息和网络连接错误信息。

```
-error
    |-ApiErrorInfo.java
    |-ExceptionInfo.java
```

- ApiErrorInfo.java

  ApiErrorInfo.java 规定了网络连接失败时需要收集哪些信息报告给中心服务器，方便开发人员定位崩溃原因，及时修复程序 bug。属性含义详见类内注释。

  ```java
  public class ApiErrorInfo {
      private String requestURL;
      private String requestHeader;
      private String requestMethod;
      private String requestParams;
      private String responseHTTPStatusCode;
      private String responseStatusCode;
      private String responseBody;
      private String responseAt;
  }
  ```

- ExceptionInfo.java

  ExceptionInfo.java 规定了程序崩溃时需要收集哪些信息报告给中心服务器，方便开发人员定位崩溃原因，及时修复程序 bug。属性含义详见类内注释。

  ```java
  public class ExceptionInfo {
      private String shortName;
      private String exceptionType;
      private String appVersion;
      private String deviceUUID;
      private String phoneNetwork;
      private String OSRelease;
      private String exceptionReason;
      private String exceptionTrace;
      private String threadName;
      private String threadTrace;
      private String exceptionAt;

      ...
  }
  ```

#### kv

kv 文件夹是对腾讯的 MMKV 库做的简单封装。MMKV 用来存储 key-value 数据对，类似于 SharedPreferences，比 SharedPreferences 性能更好。在我们项目中使用它来替代 SharedPreferences。

```
-kv
    |-Kv.java
```

#### network

network 文件夹存放网络操作相关类，是对 retrofit 的简单封装。

```
-network
    |-response
        |-BaseResponse.java
        ...
    |-AbstractMyCallBack.java
    |-ApiManager.java
    |-NetworkUtil.java
    |-SubmitError.java
```

- response/BaseResponse.java

  BaseResponse.java 规定了网络请求返回的基本格式（只在我们自己的网络请求中生效，是内部约定，基本不变）。

  ```java
  public class BaseResp<T> {
      private T data;
      private String statusCode;
      private String message;
  }
  ```

- AbstractMyCallBack.java

  对请求响应做初步处理，如果成功直接将响应数据传给调用方，如果失败则记录失败原因并将失败信息上传到中心服务器。

- ApiManager.java

  管理请求地址与参数。

- NetworkUtil.java

  网络操作工具类。

- SubmitError.java

  提交程序崩溃信息和网络连接错误信息的工具类。

#### update

update 文件夹存放检查程序版本更新和安装  更新的工具类。

```
-update
    |-AndroidOPermissionActivity.java
    |-CheckUpdate.java
    |-UpdateService.java
```

- AndroidOPermissionActivity.java

  AndroidOPermissionActivity.java 是在 Android 8.0 以上的版本中，用来提示用户向程序授予安装更新 apk 权限的页面。

- CheckUpdate.java

  CheckUpdate.java 是用来检查版本更新的工具类。

- UpdateService.java

  UpdateService.java 是用来在后台下载和更新程序的服务。

#### util

util 文件夹存放一些基础工具类。

```
-util
    |-AlertUtil.java
    |-AnimUtil.java
    |-AppUtil.java
    |-DateUtil.java
    |-DisplayUtil.java
    |-FileUtil.java
    |-ImageUtil.java
    |-LogUtil.java
    |-StringUtil.java
    |-TimeCount.java
    |-ToastUtil.java
    |-TokenUtil.java
```

- AlertUtil.java

  AlertUtil.java 对 AlertDialog 的简单封装，用一行代码唤起一个 AlertDialog。

- AnimUtil.java

  执行动画的工具类。

- AppUtil.java

  获取程序基本信息。

- DateUtil.java

  操作日期。

- DisplayUtil.java

  px、dp、sp 之间相互转换。

- FileUtil.java

  文件操作。

- ImageUtil.java

  图片操作。

- LogUtil.java

  对 Log 的简单封装。在程序上线时要将其 showLog 属性设为 false，不打印 log。

- StringUtil.java

  字符串操作。

- TimeCount.java

   倒计时。

- ToastUtil.java

  对开源库 com.hjq:toast 的简单封装，代替系统 Toast，解决了有些机型关闭通知权限无法弹出 Toast 的问题。

- TokenUtil.java

  封装了 token 的存、取、刷新。

#### view

view 文件夹存放通用的自定义 View。

```
-view
    MultipleStateView.java
```

- MultipleStateView.java

  通用的多状态占位页面，包括加载页面、空数据页面、网络连接错误页面、无网络页面。

#### Constant.java

Constant.java 保存项目中使用的常量。

## 代码规范

项目代码规范统一使用[阿里巴巴 Java 开发手册](https://github.com/alibaba/p3c)。

在 Android Studio 中安装 **Alibaba Java Coding Guidelines** 插件对代码自动进行格式检查。

**补充**：

https://blog.csdn.net/vipzjyno1/article/details/23542617

1. 所有非 app 模块中的资源文件（.xml）明明要加前缀（模块名\_XX.xml）。

   在 build.gradle(模块) 中添加以下代码，程序在编译过程中会自动对不符合规范的文件进行提示。

   ```
   android {
       ...
       defaultConfig {
           ...
           //防止资源文件冲突，为每个模块下的资源文件加前缀
           resourcePrefix "模块名_"
       }
   }
   ```

2. 布局文件控件 id 命名规则：

## 使用

1. 克隆到本地。

2. 修改 app 包名与 build.gradle(app)中的 applicationId。
