# 员工考勤信息管理系统

本项目是数据库课程设计项目，完整功能版本使用 Spring Boot + MyBatis + MySQL + Thymeleaf + Bootstrap 实现。

## GitHub Pages 静态演示版

项目中的 `docs/` 目录是 GitHub Pages 静态演示目录。

静态演示版说明：

- 不连接 MySQL 数据库。
- 不启动 Spring Boot 后端。
- 页面数据均为写死的演示数据。
- 只用于展示页面效果、系统结构和功能入口。
- 新增、编辑、注销、保存、查询等按钮只弹出提示，不会真正修改数据。
- 完整业务功能需要在本地运行 Spring Boot + MySQL 后端系统。

GitHub Pages 开启方式：

1. 打开 GitHub 仓库页面。
2. 进入 `Repository Settings`。
3. 进入 `Pages`。
4. `Source` 选择 `Deploy from a branch`。
5. `Branch` 选择 `main`。
6. `Folder` 选择 `/docs`。
7. 保存后，访问 GitHub Pages 给出的 `github.io` 链接。

访问地址格式通常为：

```text
https://你的GitHub用户名.github.io/仓库名/
```

## 完整后端版

完整后端版需要本地准备：

- Java 21
- Maven
- MySQL 8
- 已执行 `attendance_system.sql`

启动方式：

```bash
mvn spring-boot:run
```

本地访问：

```text
http://localhost:8080/
```

## 静态演示版与完整后端版区别

| 项目 | GitHub Pages 静态演示版 | Spring Boot 完整后端版 |
| --- | --- | --- |
| 运行方式 | 浏览器直接访问静态 HTML | 本地启动 Spring Boot |
| 数据来源 | 写死的演示数据 | MySQL 数据库 |
| 是否需要数据库 | 不需要 | 需要 |
| 是否能保存数据 | 不能 | 可以 |
| 主要用途 | GitHub 预览、课程展示 | 完整业务演示、答辩运行 |
