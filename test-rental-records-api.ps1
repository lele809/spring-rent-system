# 测试缴费记录API
Write-Host "=== 测试缴费记录API ===" -ForegroundColor Green

# 等待服务启动
Write-Host "等待服务启动..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# 测试六楼数据
Write-Host "测试六楼数据..." -ForegroundColor Cyan
try {
    $response6 = Invoke-WebRequest -Uri "http://localhost:8080/api/rental-records/6?page=0&size=10" -Method GET -ContentType "application/json"
    Write-Host "六楼API响应状态: $($response6.StatusCode)" -ForegroundColor Green
    Write-Host "六楼响应内容: $($response6.Content)" -ForegroundColor White
} catch {
    Write-Host "六楼API错误: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n" -NoNewline

# 测试五楼数据
Write-Host "测试五楼数据..." -ForegroundColor Cyan
try {
    $response5 = Invoke-WebRequest -Uri "http://localhost:8080/api/rental-records/5?page=0&size=10" -Method GET -ContentType "application/json"
    Write-Host "五楼API响应状态: $($response5.StatusCode)" -ForegroundColor Green
    Write-Host "五楼响应内容: $($response5.Content)" -ForegroundColor White
} catch {
    Write-Host "五楼API错误: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n测试完成!" -ForegroundColor Green 