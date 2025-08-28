# 房客管理系统部署指南

## 项目架构
- 后端：Spring Boot + PostgreSQL
- 前端：Vue 3 + TypeScript + Element Plus
- 部署平台：Render
- 数据库：Supabase PostgreSQL

## 配置步骤

### 1. Supabase 数据库配置

1. 在 [Supabase](https://supabase.com) 创建新项目
2. 获取数据库连接信息：
   - Database URL
   - Database Username  
   - Database Password
3. 导入数据库结构：
   ```sql
   -- 使用 rent_system.sql 或 fix-database-schema.sql 创建表结构
   ```

### 2. Render 部署配置

#### 方式一：使用 render.yaml (推荐)
1. 在 Render 控制台创建新的 Blueprint
2. 连接 GitHub 仓库
3. Render 会自动读取 `render.yaml` 配置文件
4. 设置环境变量：
   - `DATABASE_URL`: Supabase 数据库连接字符串
   - `DATABASE_USERNAME`: Supabase 用户名
   - `DATABASE_PASSWORD`: Supabase 密码

#### 方式二：手动配置
1. **后端服务配置**：
   - Service Type: Web Service
   - Environment: Java
   - Build Command: `cd house && ./gradlew build -x test`
   - Start Command: `cd house && java -Dspring.profiles.active=prod -jar build/libs/house-0.0.1-SNAPSHOT.jar`
   - Environment Variables:
     ```
     PORT=8080
     DATABASE_URL=你的Supabase数据库URL
     DATABASE_USERNAME=你的Supabase用户名
     DATABASE_PASSWORD=你的Supabase密码
     SPRING_PROFILES_ACTIVE=prod
     ```

2. **前端服务配置**：
   - Service Type: Static Site
   - Build Command: `cd house-fronted && npm ci && npm run build`
   - Publish Directory: `house-fronted/dist`
   - Environment Variables:
     ```
     VITE_API_BASE_URL=https://your-backend-service.onrender.com
     ```

### 3. 本地开发环境

1. **后端启动**：
   ```bash
   cd house
   ./gradlew bootRun
   ```

2. **前端启动**：
   ```bash
   cd house-fronted
   npm install
   npm run dev
   ```

### 4. 数据库迁移

如果从 MySQL 迁移到 PostgreSQL：
1. 使用 `fix-database-schema.sql` 文件创建 PostgreSQL 表结构
2. 导出 MySQL 数据并转换为 PostgreSQL 格式
3. 注意数据类型差异：
   - MySQL `DATETIME` → PostgreSQL `TIMESTAMP`
   - MySQL `DECIMAL` → PostgreSQL `NUMERIC`
   - MySQL `AUTO_INCREMENT` → PostgreSQL `SERIAL`

### 5. 环境变量说明

#### 后端环境变量
- `PORT`: 服务端口 (默认: 8080)
- `DATABASE_URL`: PostgreSQL 连接字符串
- `DATABASE_USERNAME`: 数据库用户名
- `DATABASE_PASSWORD`: 数据库密码
- `SPRING_PROFILES_ACTIVE`: Spring 配置文件 (prod/dev)

#### 前端环境变量
- `VITE_API_BASE_URL`: 后端 API 地址
- `VITE_API_TIMEOUT`: API 请求超时时间

### 6. 部署检查清单

- [ ] Supabase 数据库已创建并导入数据
- [ ] 后端服务能够连接数据库
- [ ] 前端能够访问后端 API
- [ ] CORS 配置正确
- [ ] 环境变量设置完整
- [ ] SSL 证书配置 (Render 自动提供)

### 7. 常见问题

1. **CORS 错误**：检查后端 CORS 配置和前端 API 地址
2. **数据库连接失败**：验证数据库 URL 和凭据
3. **构建失败**：检查依赖版本和构建命令
4. **404 错误**：确保前端路由配置正确

### 8. 监控和日志

- Render 提供服务日志查看
- 可以配置健康检查端点
- 建议设置错误监控和性能监控 