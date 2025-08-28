# 测试添加联系人功能
Write-Host "🚀 开始测试添加联系人功能..." -ForegroundColor Cyan

$baseUrl = "http://localhost:8081"

# 测试数据库连接
Write-Host "`n📡 测试数据库连接..." -ForegroundColor Yellow
try {
    $dbTest = Invoke-RestMethod -Uri "$baseUrl/api/test/db-connection" -Method GET -TimeoutSec 10
    Write-Host "✅ 数据库连接测试: $($dbTest.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ 数据库连接失败: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 测试五楼联系人数据
Write-Host "`n📊 测试五楼联系人数据..." -ForegroundColor Yellow
try {
    $oldContacts = Invoke-RestMethod -Uri "$baseUrl/api/test/contacts-old" -Method GET -TimeoutSec 10
    Write-Host "✅ 五楼联系人数据获取成功，当前数量: $($oldContacts.data.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ 五楼联系人数据获取失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 测试六楼联系人数据  
Write-Host "`n📊 测试六楼联系人数据..." -ForegroundColor Yellow
try {
    $newContacts = Invoke-RestMethod -Uri "$baseUrl/api/test/contacts-new" -Method GET -TimeoutSec 10
    Write-Host "✅ 六楼联系人数据获取成功，当前数量: $($newContacts.data.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ 六楼联系人数据获取失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 测试添加五楼联系人
Write-Host "`n🆕 测试添加五楼联系人..." -ForegroundColor Yellow
$testTenantOld = @{
    name = "测试联系人老"
    roomId = "501"
    phone = "13800138001" 
    idCard = "110101199001011234"
} | ConvertTo-Json

Write-Host "发送数据: $testTenantOld" -ForegroundColor Gray

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/tenants/old" `
        -Method POST `
        -ContentType "application/json" `
        -Body $testTenantOld `
        -TimeoutSec 10
    
    Write-Host "✅ 五楼联系人添加成功!" -ForegroundColor Green
    Write-Host "响应: $($response | ConvertTo-Json -Depth 3)" -ForegroundColor Green
} catch {
    Write-Host "❌ 五楼联系人添加失败: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $errorResponse = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($errorResponse)
        $errorBody = $reader.ReadToEnd()
        Write-Host "错误详情: $errorBody" -ForegroundColor Red
    }
}

# 测试添加六楼联系人
Write-Host "`n🆕 测试添加六楼联系人..." -ForegroundColor Yellow
$testTenantNew = @{
    name = "测试联系人新"
    roomId = "601"
    phone = "13800138002"
    idCard = "110101199001011235"
} | ConvertTo-Json

Write-Host "发送数据: $testTenantNew" -ForegroundColor Gray

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/tenants/new" `
        -Method POST `
        -ContentType "application/json" `
        -Body $testTenantNew `
        -TimeoutSec 10
    
    Write-Host "✅ 六楼联系人添加成功!" -ForegroundColor Green
    Write-Host "响应: $($response | ConvertTo-Json -Depth 3)" -ForegroundColor Green
} catch {
    Write-Host "❌ 六楼联系人添加失败: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $errorResponse = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($errorResponse)
        $errorBody = $reader.ReadToEnd()
        Write-Host "错误详情: $errorBody" -ForegroundColor Red
    }
}

# 验证添加后的数据
Write-Host "`n🔍 验证添加后的数据..." -ForegroundColor Yellow

# 查看五楼数据
try {
    $oldContactsAfter = Invoke-RestMethod -Uri "$baseUrl/api/tenants/old" -Method GET -Body @{page=0; size=10} -TimeoutSec 10
    Write-Host "✅ 五楼联系人当前数量: $($oldContactsAfter.data.totalElements)" -ForegroundColor Green
} catch {
    Write-Host "❌ 获取五楼联系人列表失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 查看六楼数据
try {
    $newContactsAfter = Invoke-RestMethod -Uri "$baseUrl/api/tenants/new" -Method GET -Body @{page=0; size=10} -TimeoutSec 10
    Write-Host "✅ 六楼联系人当前数量: $($newContactsAfter.data.totalElements)" -ForegroundColor Green
} catch {
    Write-Host "❌ 获取六楼联系人列表失败: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🏁 测试完成!" -ForegroundColor Cyan 