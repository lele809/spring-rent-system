# 简单测试添加联系人功能
Write-Host "开始测试添加联系人功能..." -ForegroundColor Cyan

$baseUrl = "http://localhost:8081"

# 测试数据库连接
Write-Host "测试数据库连接..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/test/db-connection" -Method GET -TimeoutSec 10
    Write-Host "数据库连接成功: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "数据库连接失败: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 测试添加五楼联系人
Write-Host "测试添加五楼联系人..." -ForegroundColor Yellow

$testData = @"
{
    "name": "测试用户2025",
    "roomId": "999", 
    "phone": "19912345678",
    "idCard": "110101199912311111"
}
"@

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/tenants/old" -Method POST -ContentType "application/json" -Body $testData -TimeoutSec 10
    Write-Host "五楼联系人添加成功!" -ForegroundColor Green
    Write-Host "返回数据: $($response | ConvertTo-Json)" -ForegroundColor Green
} catch {
    Write-Host "五楼联系人添加失败: $($_.Exception.Message)" -ForegroundColor Red
    
    # 获取详细错误信息
    if ($_.Exception.Response) {
        try {
            $errorStream = $_.Exception.Response.GetResponseStream()
            $reader = New-Object System.IO.StreamReader($errorStream)
            $errorBody = $reader.ReadToEnd()
            Write-Host "错误详情: $errorBody" -ForegroundColor Red
        } catch {
            Write-Host "无法获取错误详情" -ForegroundColor Red
        }
    }
}

# 测试添加六楼联系人  
Write-Host "测试添加六楼联系人..." -ForegroundColor Yellow

$testData2 = @"
{
    "name": "测试用户2026",
    "roomId": "888",
    "phone": "19912345679", 
    "idCard": "110101199912311112"
}
"@

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/tenants/new" -Method POST -ContentType "application/json" -Body $testData2 -TimeoutSec 10
    Write-Host "六楼联系人添加成功!" -ForegroundColor Green
    Write-Host "返回数据: $($response | ConvertTo-Json)" -ForegroundColor Green
} catch {
    Write-Host "六楼联系人添加失败: $($_.Exception.Message)" -ForegroundColor Red
    
    # 获取详细错误信息
    if ($_.Exception.Response) {
        try {
            $errorStream = $_.Exception.Response.GetResponseStream()
            $reader = New-Object System.IO.StreamReader($errorStream)
            $errorBody = $reader.ReadToEnd()
            Write-Host "错误详情: $errorBody" -ForegroundColor Red
        } catch {
            Write-Host "无法获取错误详情" -ForegroundColor Red
        }
    }
}

Write-Host "测试完成!" -ForegroundColor Cyan 