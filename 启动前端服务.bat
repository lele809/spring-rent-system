@echo off
cd /d %~dp0house-fronted
echo 正在启动前端服务...
echo 请确保已安装 Node.js 和依赖包
npm run dev
pause 