#!/usr/bin/env bash

# 获取当前日期和时间
msg="$(date)"

# 添加所有文件到git
git add *

# 提交更改到仓库
git commit -m "$msg"

git remote set-url o https://Jason-Chen-2017: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@github.com/Jason-Chen-2017/note-ai.git

git push o master