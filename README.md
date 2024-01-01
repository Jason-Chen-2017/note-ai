# note-ai
Note AI


# MySQL Config



## Error: 13 (权限不够)

``` 
> systemctl status mysql.service                                                                                                                                                                           13:44:25
× mysql.service - MySQL Community Server
     Loaded: loaded (/lib/systemd/system/mysql.service; enabled; vendor preset: enabled)
     Active: failed (Result: exit-code) since Sat 2023-12-16 13:44:28 CST; 5s ago
    Process: 2275187 ExecStartPre=/usr/share/mysql/mysql-systemd-start pre (code=exited, status=0/SUCCESS)
    Process: 2275195 ExecStart=/usr/sbin/mysqld (code=exited, status=1/FAILURE)
   Main PID: 2275195 (code=exited, status=1/FAILURE)
     Status: "Server shutdown complete"
      Error: 13 (权限不够)
        CPU: 266ms

12月 16 13:44:28 ai systemd[1]: mysql.service: Scheduled restart job, restart counter is at 5.
12月 16 13:44:28 ai systemd[1]: Stopped MySQL Community Server.
12月 16 13:44:28 ai systemd[1]: mysql.service: Start request repeated too quickly.
12月 16 13:44:28 ai systemd[1]: mysql.service: Failed with result 'exit-code'.
12月 16 13:44:28 ai systemd[1]: Failed to start MySQL Community Server.
```

## 解决方案

``` 
sudo chmod -R 777 /var/lib/mysql
sudo chmod -R 777 /var/log

sudo systemctl restart mysql 

systemctl status mysql.service                                                                                                                                                                           


---------------------------------------------------------------------------------------------------
● mysql.service - MySQL Community Server
     Loaded: loaded (/lib/systemd/system/mysql.service; enabled; vendor preset: enabled)
     Active: active (running) since Sat 2023-12-16 13:47:20 CST; 20s ago
    Process: 2275498 ExecStartPre=/usr/share/mysql/mysql-systemd-start pre (code=exited, status=0/SUCCESS)
   Main PID: 2275506 (mysqld)
     Status: "Server is operational"
      Tasks: 38 (limit: 18841)
     Memory: 502.3M
        CPU: 954ms
     CGroup: /system.slice/mysql.service
             └─2275506 /usr/sbin/mysqld

12月 16 13:47:18 ai systemd[1]: Starting MySQL Community Server...
12月 16 13:47:20 ai systemd[1]: Started MySQL Community Server.
[13:47:41] me@ai /home/me  
>                                                                                                                                                                                                          13:47:41
[13:47:45] me@ai /home/me  
>                                                                                                                                                                                                          13:47:45
[13:47:45] me@ai /home/me  
> systemctl status mysql.service                                                                                                                                                                           13:47:45
● mysql.service - MySQL Community Server
     Loaded: loaded (/lib/systemd/system/mysql.service; enabled; vendor preset: enabled)
     Active: active (running) since Sat 2023-12-16 13:47:20 CST; 25s ago
    Process: 2275498 ExecStartPre=/usr/share/mysql/mysql-systemd-start pre (code=exited, status=0/SUCCESS)
   Main PID: 2275506 (mysqld)
     Status: "Server is operational"
      Tasks: 38 (limit: 18841)
     Memory: 502.3M
        CPU: 971ms
     CGroup: /system.slice/mysql.service
             └─2275506 /usr/sbin/mysqld

12月 16 13:47:18 ai systemd[1]: Starting MySQL Community Server...
12月 16 13:47:20 ai systemd[1]: Started MySQL Community Server.

```


# DB Schemas



```sql 

CREATE TABLE `notes` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(520) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_create` (`created_at`),
  KEY `idx_update` (`updated_at`),
  KEY `idx_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

```




Clean Data:

```sql

select count(*) as titles from notes where updated_at=created_at;
select count(*) as articles from notes where updated_at!=created_at;
select count(*) from notes;



DELETE FROM notes WHERE ( updated_at < DATE(NOW()) - INTERVAL 3 DAY ) AND ( updated_at!=created_at ) ;


truncate table notes;

```









好的，以下是创建一个名为 `note_ai` 的 MySQL 数据库，并设置为 utf8mb4 字符集（支持表情包编码）的 SQL 语句：

```
CREATE DATABASE note_ai CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

这个语句将创建一个空的数据库，并设置字符集为 utf8mb4，使用 utf8mb4_unicode_ci 校对规则。接下来，你可以使用 `USE` 语句来进入数据库：

```
USE note_ai;
```

现在你已经成功进入 `note_ai` 数据库。接下来，你可以创建一个名为 `notes` 的表，用于存储笔记信息。以下是创建这个表的 SQL 语句：

```
CREATE TABLE notes (
  id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  content TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

这个表包括以下列：

- `id`: 整数类型，自增长，作为笔记的唯一标识符。
- `title`: 字符串类型，表示笔记的标题。长度限制为 255 个字符。使用 utf8mb4 字符集，支持表情包编码。
- `content`: 文本类型，表示笔记的正文内容。使用 utf8mb4 字符集，支持表情包编码。
- `created_at`: 时间戳类型，表示笔记创建的时间。默认为当前时间。
- `updated_at`: 时间戳类型，表示笔记上次更新的时间。默认为当前时间，且在每次更新时自动更新为当前时间。

这个表的设计是基本的，你可以根据实际需求添加其他列或者进行修改。同时请注意，如果你的应用程序或者页面需要支持表情包等特殊字符，需要在连接 MySQL 数据库时设置字符集为 utf8mb4。


```sql 
ALTER TABLE `note_ai`.`notes` 
CHANGE COLUMN `content` `content` LONGTEXT CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL ;

```



## 测试 Prompt

```
现在你是一位人工智能专家,程序员,软件架构师,CTO，请以《数据挖掘：如何从销售数据中挖掘出有价值的商业机会》为标题，写一篇有深度有思考有见解的专业的技术博客文章， 文章目录如下：

## 1. 引言

- 1.1. 背景介绍
- 1.2. 文章目的
- 1.3. 目标受众

## 2. 技术原理及概念

- 2.1. 基本概念解释
- 2.2. 技术原理介绍:算法原理，操作步骤，数学公式等
- 2.3. 相关技术比较

## 3. 实现步骤与流程

- 3.1. 准备工作：环境配置与依赖安装
- 3.2. 核心模块实现
- 3.3. 集成与测试

## 4. 应用示例与代码实现讲解

- 4.1. 应用场景介绍
- 4.2. 应用实例分析
- 4.3. 核心代码实现
- 4.4. 代码讲解说明


## 5. 优化与改进

- 5.1. 性能优化
- 5.2. 可扩展性改进
- 5.3. 安全性加固

## 6. 结论与展望

- 6.1. 技术总结
- 6.2. 未来发展趋势与挑战

## 7. 附录：常见问题与解答


请保持逻辑清晰、结构紧凑，以便读者更容易理解和掌握所讲述的技术知识。
文章字数要求：文章字数大于 8000 字。
文章格式请严格按照目录来写，使用markdown格式。数学公式使用 latex 格式$$

```


# 使用 token 方式推送到 GitHub 仓库，你需要先生成一个个人访问令牌（Personal Access Token, PAT）。以下是生成 PAT 并使用它来推送到 GitHub 仓库的步骤：

1. 生成个人访问令牌（PAT）：
- 登录你的 GitHub 账户。
- 点击右上角的头像，然后选择“Settings（设置）”。
- 在侧边栏中选择“Developer settings（开发者设置）”。
- 点击“Personal access tokens（个人访问令牌）”。
- 点击“Generate new token（生成新令牌）”。
- 给你的令牌命名，并设置过期时间。
- 选择所需的权限（scopes），例如 `repo` 权限允许访问私有仓库。
- 点击“Generate token（生成令牌）”。
- 复制生成的令牌，确保保存在安全的地方，因为你不会再次看到这个令牌。

2. 使用 PAT 推送到 GitHub 仓库：
- 打开命令行工具（例如 Git Bash、终端等）。
- 切换到你的本地仓库目录。
- 如果你是第一次使用 PAT，你可能需要更新远程仓库的 URL 格式。可以使用以下命令：
```bash
git remote set-url origin https://<username>:<token>@github.com/<username>/<repository>.git
```
其中 `<username>` 是你的 GitHub 用户名，`<token>` 是你刚刚生成的个人访问令牌，`<repository>` 是你的仓库名称。

- 现在，你可以使用正常的 `git push` 命令来推送你的代码：
```bash
git push origin main
```
或者推送到你想要的任何分支：
```bash
git push origin <branch-name>
```
其中 `<branch-name>` 是你想要推送的分支名称。

请注意，从 2021 年 8 月 13 日起，GitHub 不再接受账户密码进行认证，因此你必须使用 PAT 或 SSH 密钥进行认证。此外，如果你的 Git 客户端支持，则可以使用 Git Credential Manager 来安全地存储和管理你的凭据。