#!/bin/bash

echo "=== Render Build Script Started ==="

# 检查项目结构
echo "当前目录: $(pwd)"
echo "列出文件:"
ls -la

# 检查 house 目录
if [ -d "house" ]; then
    echo "找到 house 目录"
    ls -la house/
    
    # 检查 house/house 目录
    if [ -d "house/house" ]; then
        echo "找到 house/house 目录，使用正确路径"
        cd house/house
        
        # 确保 gradlew 有执行权限
        chmod +x gradlew
        
        # 执行构建
        echo "开始构建..."
        ./gradlew build -x test
        
        echo "构建完成！"
        echo "检查构建输出:"
        ls -la build/libs/
    else
        echo "错误：找不到 house/house 目录"
        exit 1
    fi
else
    echo "错误：找不到 house 目录"
    exit 1
fi 