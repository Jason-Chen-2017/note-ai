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