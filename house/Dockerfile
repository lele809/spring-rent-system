# 多阶段构建 Dockerfile for Spring Boot with Gradle

# Stage 1: 构建阶段
FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /app

# 复制 Gradle 配置文件
COPY build.gradle settings.gradle ./
COPY gradle gradle

# 复制源代码
COPY src src

# 构建应用程序（跳过测试以提高构建速度）
RUN gradle build -x test --no-daemon

# Stage 2: 运行时阶段
FROM eclipse-temurin:17-jre-alpine

# 设置工作目录
WORKDIR /app

# 从构建阶段复制 JAR 文件
COPY --from=build /app/build/libs/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 运行应用程序
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"] 