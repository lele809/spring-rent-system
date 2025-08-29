#!/bin/bash

# 转发到实际的 gradlew 文件
cd "$(dirname "$0")/house"
exec ./gradlew "$@"
