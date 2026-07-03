# 员工考勤信息管理系统

本项目是数据库课程设计《员工考勤信息管理系统》的实现版本。系统采用 Java Spring Boot + MyBatis + MySQL + Thymeleaf + Bootstrap 开发，不设计登录功能，按任务书要求划分为员工端和管理员端两个子系统。

## 技术栈

- 后端：Spring Boot
- 数据访问：MyBatis + XML Mapper
- 数据库：MySQL 8
- 页面模板：Thymeleaf
- 页面样式：Bootstrap
- 构建工具：Maven
- Excel 导入预留：Apache POI

## 功能模块

### 员工端

- 员工通过工号进入员工端页面。
- 查看个人基本信息。
- 修改电话、邮箱、地址等个人联系方式。
- 查询本人出勤记录、请假记录、加班记录和出差记录。
- 员工不能修改工号、部门、岗位、职务和薪资信息。

### 管理员端

- 员工管理：新增员工、修改员工基本信息、维护部门、岗位、职务和薪资信息。
- 员工注销：离职员工只允许注销，不进行物理删除。
- 出勤管理：录入、修改、逻辑删除出勤记录。
- 请假管理：录入、修改、取消请假记录。
- 加班管理：录入、修改、逻辑删除加班记录。
- 出差管理：录入、修改、逻辑删除出差记录。
- 综合查询：支持按工号、姓名、部门、日期范围等条件查询。
- 月度统计：按部门统计每月正常、迟到、早退、缺勤、请假、加班和出差数据，并提供统计图展示。

## 数据库设计说明

数据库名：`attendance_system`

主要数据表：

- `department`：部门表
- `position`：岗位表
- `job_title`：职务表
- `employee`：员工表
- `employee_salary`：员工薪资表
- `attendance_record`：出勤记录表
- `leave_record`：请假记录表
- `overtime_record`：加班记录表
- `business_trip_record`：出差记录表
- `monthly_dept_attendance_summary`：月度部门考勤统计表

设计特点：

- 部门、岗位、职务独立成表，员工表只保存外键，避免重复存储名称。
- 薪资信息独立成表，便于管理员维护员工薪资。
- 出勤、请假、加班、出差按业务类型分表，结构清晰，便于统计。
- 使用外键体现主从关系。
- 使用触发器实现禁止物理删除员工、注销自动填写离职日期、自动计算请假天数、加班小时和出差天数。
- 整体设计满足第三范式要求。

## 本地运行步骤

1. 安装 Java、Maven 和 MySQL 8。
2. 创建并初始化数据库。
3. 修改 `src/main/resources/application.yml` 中的 MySQL 用户名和密码。
4. 在项目根目录运行：

```bash
mvn spring-boot:run
```

5. 浏览器访问：

```text
http://localhost:8080/
```

## MySQL 初始化步骤

在 MySQL 客户端中执行数据库脚本：

```text
attendance_system.sql
```

如果需要验证触发器和约束，可分别执行：

```text
attendance_system_test_success.sql
attendance_system_test_failure.sql
```

其中 failure 文件中的 SQL 建议单条执行，用于展示外键、唯一约束和触发器是否生效。

## 演示路径

- 系统首页：`http://localhost:8080/`
- 员工端入口：`http://localhost:8080/employee/entry`
- 管理员端首页：`http://localhost:8080/admin/home`
- 员工管理：`http://localhost:8080/admin/employee/list`
- 出勤管理：`http://localhost:8080/admin/attendance/list`
- 请假管理：`http://localhost:8080/admin/leave/list`
- 加班管理：`http://localhost:8080/admin/overtime/list`
- 出差管理：`http://localhost:8080/admin/trip/list`
- 综合查询：`http://localhost:8080/admin/search`
- 月度统计：`http://localhost:8080/admin/statistics`

## GitHub Pages 静态演示版

项目中的 `docs/` 目录是 GitHub Pages 静态演示目录。

静态演示版说明：

- 不连接 MySQL 数据库。
- 不启动 Spring Boot 后端。
- 页面数据均为固定演示数据。
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
7. 保存后访问 GitHub Pages 给出的 `github.io` 链接。

访问地址格式通常为：

```text
https://你的GitHub用户名.github.io/仓库名/
```

## 静态演示版与完整后端版区别

| 项目 | GitHub Pages 静态演示版 | Spring Boot 完整后端版 |
| --- | --- | --- |
| 运行方式 | 浏览器直接访问静态 HTML | 本地启动 Spring Boot |
| 数据来源 | 固定演示数据 | MySQL 数据库 |
| 是否需要数据库 | 不需要 | 需要 |
| 是否能保存数据 | 不能 | 可以 |
| 主要用途 | GitHub 预览、课程展示 | 完整业务演示、答辩运行 |

## 课程设计亮点

- 完整体现员工端和管理员端两个子系统。
- 不设计登录功能，符合任务书要求。
- 数据库包含主从关系、外键、触发器和 3NF 说明。
- 员工离职采用注销方式，不进行物理删除。
- MyBatis XML 查询便于展示 SQL 和数据库设计思路。
- 管理员首页提供统计卡片，月度统计页面提供图表展示。
- 同时提供 Spring Boot 完整版和 GitHub Pages 静态演示版，便于本地答辩和线上预览。
