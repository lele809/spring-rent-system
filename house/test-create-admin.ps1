# 测试创建管理员账号的脚本

Write-Host "正在测试创建管理员账号..."

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/create-admin" -Method POST -Body @{
        adminName = "test"
        password = "123456"
    } -ContentType "application/x-www-form-urlencoded"
    
    Write-Host "成功！" -ForegroundColor Green
    Write-Host "管理员账号创建结果: $($response.message)"
    Write-Host ""
    Write-Host "现在你可以使用以下账号登录："
    Write-Host "用户名: test"
    Write-Host "密码: 123456"
} catch {
    Write-Host "失败: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "状态码: $($_.Exception.Response.StatusCode)"
    Write-Host "响应内容: $($_.Exception.Response.Content)"
}