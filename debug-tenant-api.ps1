# 详细诊断添加联系人功能
Write-Host "=== 开始诊断添加联系人功能 ===" -ForegroundColor Cyan

$baseUrl = "http://localhost:8081"

# 1. 测试基础连接
Write-Host "`n1. 测试后端服务连接..." -ForegroundColor Yellow
try {
    $healthCheck = Invoke-RestMethod -Uri "$baseUrl/api/test/db-connection" -Method GET -TimeoutSec 5
    Write-Host "✅ 后端服务连接成功: $($healthCheck.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ 后端服务连接失败: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 2. 查看现有数据
Write-Host "`n2. 查看现有联系人数据..." -ForegroundColor Yellow
try {
    # 获取五楼联系人
    $oldContacts = Invoke-RestMethod -Uri "$baseUrl/api/tenants/old" -Method GET -Body @{page=0; size=5} -TimeoutSec 10
    Write-Host "✅ 五楼联系人总数: $($oldContacts.data.totalElements)" -ForegroundColor Green
    
    # 获取六楼联系人
    $newContacts = Invoke-RestMethod -Uri "$baseUrl/api/tenants/new" -Method GET -Body @{page=0; size=5} -TimeoutSec 10
    Write-Host "✅ 六楼联系人总数: $($newContacts.data.totalElements)" -ForegroundColor Green
} catch {
    Write-Host "❌ 获取现有数据失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 3. 测试具体的API端点
Write-Host "`n3. 测试API端点响应..." -ForegroundColor Yellow

# 3.1 测试OPTIONS请求（检查CORS）
try {
    $options = Invoke-WebRequest -Uri "$baseUrl/api/tenants/old" -Method Options -UseBasicParsing
    Write-Host "✅ OPTIONS请求成功，状态码: $($options.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "⚠️ OPTIONS请求失败: $($_.Exception.Message)" -ForegroundColor Yellow
}

# 4. 使用最简单的测试数据
Write-Host "`n4. 尝试添加测试联系人..." -ForegroundColor Yellow

$timestamp = Get-Date -Format "yyyyMMddHHmmss"
$testData = @{
    name = "测试$timestamp"
    roomId = "T$timestamp"
    phone = "199${timestamp.Substring(8)}"
    idCard = "11010119901231${timestamp.Substring(10, 4)}"
} | ConvertTo-Json -Depth 10

Write-Host "测试数据: $testData" -ForegroundColor Gray

# 4.1 测试五楼API
Write-Host "`n4.1 测试五楼联系人添加..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/tenants/old" `
        -Method POST `
        -ContentType "application/json; charset=utf-8" `
        -Body $testData `
        -TimeoutSec 10 `
        -ErrorAction Stop
    
    Write-Host "✅ 五楼联系人添加成功!" -ForegroundColor Green
    Write-Host "响应码: $($response.code)" -ForegroundColor Green
    Write-Host "消息: $($response.message)" -ForegroundColor Green
    if ($response.data) {
        Write-Host "返回ID: $($response.data.id)" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ 五楼联系人添加失败" -ForegroundColor Red
    Write-Host "错误消息: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $statusCode = $_.Exception.Response.StatusCode.value__
        Write-Host "HTTP状态码: $statusCode" -ForegroundColor Red
        
        try {
            $errorStream = $_.Exception.Response.GetResponseStream()
            $reader = New-Object System.IO.StreamReader($errorStream)
            $errorBody = $reader.ReadToEnd()
            Write-Host "详细错误: $errorBody" -ForegroundColor Red
        } catch {
            Write-Host "无法读取错误详情" -ForegroundColor Red
        }
    }
}

# 4.2 测试六楼API（使用不同数据）
Write-Host "`n4.2 测试六楼联系人添加..." -ForegroundColor Cyan
$testData2 = @{
    name = "测试新$timestamp"
    roomId = "N$timestamp"
    phone = "188${timestamp.Substring(8)}"
    idCard = "11010119901231${timestamp.Substring(10, 3)}5"
} | ConvertTo-Json -Depth 10

try {
    $response2 = Invoke-RestMethod -Uri "$baseUrl/api/tenants/new" `
        -Method POST `
        -ContentType "application/json; charset=utf-8" `
        -Body $testData2 `
        -TimeoutSec 10 `
        -ErrorAction Stop
    
    Write-Host "✅ 六楼联系人添加成功!" -ForegroundColor Green
    Write-Host "响应码: $($response2.code)" -ForegroundColor Green
    Write-Host "消息: $($response2.message)" -ForegroundColor Green
    if ($response2.data) {
        Write-Host "返回ID: $($response2.data.id)" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ 六楼联系人添加失败" -ForegroundColor Red
    Write-Host "错误消息: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $statusCode = $_.Exception.Response.StatusCode.value__
        Write-Host "HTTP状态码: $statusCode" -ForegroundColor Red
        
        try {
            $errorStream = $_.Exception.Response.GetResponseStream()
            $reader = New-Object System.IO.StreamReader($errorStream)
            $errorBody = $reader.ReadToEnd()
            Write-Host "详细错误: $errorBody" -ForegroundColor Red
        } catch {
            Write-Host "无法读取错误详情" -ForegroundColor Red
        }
    }
}

# 5. 验证数据是否真的添加到数据库
Write-Host "`n5. 验证添加后的数据..." -ForegroundColor Yellow
Start-Sleep -Seconds 2

try {
    $oldContactsAfter = Invoke-RestMethod -Uri "$baseUrl/api/tenants/old" -Method GET -Body @{page=0; size=5} -TimeoutSec 10
    Write-Host "五楼联系人总数（添加后）: $($oldContactsAfter.data.totalElements)" -ForegroundColor Green
    
    $newContactsAfter = Invoke-RestMethod -Uri "$baseUrl/api/tenants/new" -Method GET -Body @{page=0; size=5} -TimeoutSec 10
    Write-Host "六楼联系人总数（添加后）: $($newContactsAfter.data.totalElements)" -ForegroundColor Green
} catch {
    Write-Host "❌ 验证数据失败: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== 诊断完成 ===" -ForegroundColor Cyan 