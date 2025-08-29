#!/bin/bash

# Render 部署转发脚本
# 将调用转发到实际的 gradlew 文件位置

echo "=== Render Gradlew Forwarder ==="
echo "当前目录: $(pwd)"
echo "转发到: house/gradlew"

# 切换到实际的 gradle 项目目录
cd house

# 确保实际的 gradlew 有执行权限
chmod +x gradlew

# 执行实际的 gradlew 命令
exec ./gradlew "$@"
