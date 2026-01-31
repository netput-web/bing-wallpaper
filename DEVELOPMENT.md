# Bing Wallpaper 项目二次开发文档

## 项目概述

这是一个基于Java开发的Bing壁纸采集和展示系统，每日自动获取Bing壁纸并生成多种格式的展示页面。

## 项目结构

### 核心Java文件

#### 1. `Wallpaper.java` - 主程序入口
- **功能**: 程序入口，控制整个数据获取和处理流程
- **关键配置**:
  - `BING_API_TEMPLATE`: Bing API模板，支持多地区
  - `regions`: 支持的地区数组，默认为 `["en-US", "zh-CN"]`
  - `CURRENT_REGION`: 当前处理的地区
- **主要流程**:
  1. 遍历所有支持的地区
  2. 调用Bing API获取壁纸数据
  3. 更新本地数据文件
  4. 生成各种格式的展示页面

#### 2. `Images.java` - 图片数据模型
- **功能**: 存储图片信息，提供多种格式化方法
- **属性**:
  - `desc`: 图片描述
  - `date`: 图片日期
  - `url`: 图片URL
- **重要方法**:
  - `toString()`: 生成Markdown格式输出
  - `formatMarkdown()`: 生成简化的Markdown格式
  - `toLarge()`: 生成大图展示格式
  - `getDetailUrlPath()`: 生成详情页路径

#### 3. `BingFileUtils.java` - 文件操作工具类
- **功能**: 处理所有文件读写操作
- **关键路径变量**:
  - `README_PATH`: README.md文件路径
  - `BING_PATH`: bing-wallpaper.md数据文件路径
  - `MONTH_PATH`: 月度图片目录路径
- **主要方法**:
  - `readBing()`: 读取历史壁纸数据
  - `writeBing()`: 写入壁纸数据
  - `writeReadme()`: 生成README.md
  - `writeMonthInfo()`: 生成月度页面

#### 4. `HttpUtls.java` - HTTP请求工具类
- **功能**: 发送HTTP请求获取API数据
- **关键方法**:
  - `getHttpContent()`: 获取URL内容
  - `getHttpUrlConnection()`: 创建HTTP连接

#### 5. `LogUtils.java` - 日志工具类
- **功能**: 简单的控制台日志输出

### HTML生成相关

#### 6. `WebSiteGenerator.java` - 网站生成器
- **功能**: 生成完整的静态HTML网站
- **生成内容**:
  - 网站首页 (`index.html`)
  - 月度页面 (如 `2026-01.html`)
  - 图片详情页 (`day/yyyymm/dd.html`)
  - JSON数据文件 (`images.json`)
  - 今日壁纸信息 (`today`)

#### 7. `HtmlFileUtils.java` - HTML文件操作工具
- **功能**: 处理HTML模板的读取和生成文件的写入
- **关键路径**:
  - `BING_HTML_ROOT`: HTML网站根目录
  - `BING_HTML_INDEX_TEMPLATE`: 首页模板文件
  - `BING_HTML_DETAIL_TEMPLATE`: 详情页模板文件

#### 8. `HtmlConstant.java` - HTML模板常量
- **功能**: 定义HTML模板中的变量和组件
- **主要组件**:
  - `Sidebar`: 侧边栏导航
  - `Head`: 页面头部信息
  - `ImgCard`: 图片卡片组件
  - `MonthHistory`: 月度历史链接

### 配置和数据文件

#### 9. `pom.xml` - Maven配置
- Java版本: 1.8
- 主要依赖: FastJSON 2.0.47
- 打包方式: jar-with-dependencies

#### 10. 数据文件
- `README.md`: 项目主页，显示最近30天壁纸
- `bing-wallpaper.md`: 核心数据存储文件
- `picture/`: 按月组织的图片展示目录
- `docs/`: 生成的HTML网站目录

## 运作原理

### 数据流程
```
Bing API → 数据解析 → 本地存储 → 多格式生成
```

1. **API获取**: 通过Bing API获取每日壁纸信息
2. **数据解析**: 使用FastJSON解析API响应
3. **本地存储**: 将数据保存到`bing-wallpaper.md`
4. **多格式生成**: 
   - 更新`README.md`
   - 生成月度页面
   - 生成HTML网站

### 多地区支持
- 支持英文（en-US）和中文（zh-CN）
- 不同地区数据存储在不同目录:
  - 英文: 根目录
  - 中文: `zh-cn/`目录
- 通过`changeConfig()`方法切换地区配置

### 网站生成机制
- 使用模板系统生成静态网站
- 模板文件: `docs/bing-template.html`
- 变量替换机制: `${variable_name}`
- 支持响应式设计和图片懒加载

## 二次开发指南

### 修改API源
1. 在`Wallpaper.java`中修改`BING_API_TEMPLATE`
2. 根据新API调整JSON解析逻辑
3. 更新`Images.java`中的数据模型

### 添加新地区支持
1. 在`Wallpaper.java`的`regions`数组中添加新地区代码
2. 在`changeConfig()`方法中添加对应的路径配置
3. 创建对应的语言目录结构

### 自定义HTML模板
1. 修改`docs/bing-template.html`和`docs/bing-detail.html`
2. 在`HtmlConstant.java`中添加新的变量定义
3. 更新`WebSiteGenerator.java`中的替换逻辑

### 修改输出格式
1. 在`Images.java`中添加新的格式化方法
2. 在`BingFileUtils.java`中添加新的文件生成逻辑
3. 更新相关的模板和常量

### 添加新功能
1. 确定功能涉及的文件
2. 添加必要的数据模型和方法
3. 更新配置文件和模板
4. 测试功能完整性

## 重要配置项

### API配置
- API地址: `Wallpaper.BING_API_TEMPLATE`
- 请求频率: 根据需要调整
- 图片质量: 通过URL参数控制

### 路径配置
- 数据文件路径: `BingFileUtils`中的静态变量
- HTML输出路径: `HtmlFileUtils.BING_HTML_ROOT`
- 模板文件路径: `HtmlFileUtils`中的模板路径

### 地区配置
- 支持地区: `Wallpaper.regions`数组
- 当前地区: `Wallpaper.CURRENT_REGION`
- 路径映射: `Wallpaper.changeConfig()`方法

## 注意事项

1. **文件路径**: 所有路径都使用相对路径，注意运行时的工作目录
2. **编码格式**: 统一使用UTF-8编码
3. **依赖管理**: 使用Maven管理依赖，注意版本兼容性
4. **数据备份**: 修改前备份`bing-wallpaper.md`数据文件
5. **模板更新**: 修改HTML模板时注意变量名称的一致性

## 常见修改场景

### 更换壁纸源
1. 修改API地址和请求参数
2. 调整JSON解析逻辑
3. 更新数据模型

### 自定义网站样式
1. 修改HTML模板文件
2. 更新CSS样式
3. 调整JavaScript交互

### 添加新的输出格式
1. 在`Images.java`中添加格式化方法
2. 在`BingFileUtils.java`中添加文件写入逻辑
3. 更新主程序调用

### 修改图片处理逻辑
1. 调整URL生成规则
2. 修改图片尺寸参数
3. 更新缓存策略

## 开发环境要求

- Java 8+
- Maven 3.x
- IDE: IntelliJ IDEA 或 Eclipse
- Git: 用于版本控制

## 构建和运行

```bash
# 编译项目
mvn clean compile

# 打包项目
mvn clean package

# 运行项目
java -jar target/bing-wallpaper-jar-with-dependencies.jar
```

## 版本历史

- v1.0: 初始版本，支持基本的壁纸采集和展示
- 支持多地区和HTML网站生成
- 包含完整的模板系统

---

*此文档记录了项目的完整结构和开发指南，修改项目时请先阅读相关部分。*
