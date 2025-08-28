# 测试合同下载功能
Write-Host "=== 测试合同下载API ===" -ForegroundColor Green

# 等待服务启动
Write-Host "等待服务启动..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# 测试六楼合同下载
Write-Host "测试六楼合同下载..." -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/contracts/new/1/download" -Method GET
    Write-Host "六楼合同下载API响应状态: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "响应内容类型: $($response.Headers.'Content-Type')" -ForegroundColor White
    Write-Host "文件大小: $($response.Content.Length) 字节" -ForegroundColor White
} catch {
    Write-Host "六楼合同下载API错误: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n" -NoNewline

# 测试五楼合同下载
Write-Host "测试五楼合同下载..." -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/contracts/old/1/download" -Method GET
    Write-Host "五楼合同下载API响应状态: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "响应内容类型: $($response.Headers.'Content-Type')" -ForegroundColor White
    Write-Host "文件大小: $($response.Content.Length) 字节" -ForegroundColor White
} catch {
    Write-Host "五楼合同下载API错误: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n测试完成!" -ForegroundColor Green 