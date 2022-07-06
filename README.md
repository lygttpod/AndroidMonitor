# 基于OKHttp的抓包工具

## 切记：monitor需要配合monitor-plugin使用

### 1、monitor接入

添加依赖
```
   debugImplementation 'io.github.lygttpod:monitor:0.0.1'
```
-备注： 使用debugImplementation是为了只在测试环境中引入

### 2、monitor-plugin接入

1. 根目录build.gradle下添加如下依赖
```
    buildscript {
        dependencies {
            ......
            //monitor-plugin需要
            classpath 'io.github.lygttpod:monitor-plugin:0.0.1'
        }
    }

```
2.添加插件
```
    在APP的build.gradle中添加：

    //插件内部会自动判断debug模式下hook到okhttp
    apply plugin: 'monitor-plugin'

```

### 3、 个性化配置

    1、修改桌面抓包工具入口名字：在主项目string.xml中添加 monitor_app_name即可，例如：
    ```
       <string name="monitor_app_name">XXX-抓包</string>
    ```
    2、单个项目使用的话，添加依赖后可直接使用，无需初始化，库里会通过ContentProvider方式自动初始化
     
    默认端口8080(端口号要唯一)
        
    3、多个项目都集成抓包工具，需要对不同项目设置不同的端口和数据库名字，用来做区分
        
    在主项目assets目录下新建 monitor.properties 文件，文件内如如下：对需要变更的参数修改即可
    ```
        # 抓包助手参数配置
        # Default port = 8080
        # Default dbName = monitor_db
        # ContentTypes白名单，默认application/json,application/xml,text/html,text/plain,text/xml
        # Default whiteContentTypes = application/json,application/xml,text/html,text/plain,text/xml
        # Host白名单，默认全部是白名单
        # Default whiteHosts = 
        # Host黑名单，默认没有黑名单
        # Default blackHosts = 
        # 如何多个项目都集成抓包工具，可以设置不同的端口进行访问
        monitor.port=8080
        monitor.dbName=app_name_monitor_db
    ```

### 4、 proguard（默认已经添加混淆，如遇到问题可以添加如下混淆代码）
    ```
        # monitor
        -keep class com.lygttpod.monitor.** { *; }
    ```

### 5、 温馨提示
```
    虽然monitor-plugin只会在debug环境hook代码，
    但是release版编译的时候还是会走一遍Transform操作（空操作），
    为了保险起见建议生产包禁掉此插件。

    在jenkins打包机器的《生产环境》的local.properties中添加monitor.enablePlugin=false，全面禁用monitor插件
```

### 6、如何使用
- 集成之后编译运行项目即可在手机上自动生成一个抓包入口的图标，点击即可打开可视化页面查看网络请求数据，这样就可以随时随地的查看我们的请求数据了。
- 虽然可以很方便的查看请求数据了但是手机屏幕太小，看起来不方便怎么办呐，那就去寻找在PC上展示的方法，首先想到的是能不能直接在浏览器里边直接看呐，这样不用安装任何程序在浏览输入一个地址就可以直接查看数据
- PC和手机在同一局域网的前提下：直接在任意浏览器输入 手机ip地址+抓包工具设置的端口号即可（地址可以在抓包app首页TitleBar上可以看到）

### 7、原理剖析
- 拦截APP的OKHTTP请求(添加拦截器处理抓包请求)
- 数据保存到本地数据库（room）
- APP本地开启一个socket服务
- 与本地socket服务通信
- UI展示数据