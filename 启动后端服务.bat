@echo off
cd /d %~dp0house
echo 正在启动后端服务...
echo 请等待服务启动完成，看到 "Started HouseApplication" 消息后即可使用
.\gradlew.bat bootRun
pause 