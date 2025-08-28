#!/usr/bin/env pwsh
# 测试API可用性

Write-Host "🔍 测试API服务可用性..." -ForegroundColor Green

# 基础URL
$baseUrl = "http://localhost:8080"

# 测试基础健康检查
Write-Host "`n1. 测试基础连接..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl" -Method GET -TimeoutSec 10
    Write-Host "✅ 基础连接成功，状态码: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ 基础连接失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 测试API路径
$apiPaths = @(
    "/api",
    "/api/test-rental-records", 
    "/api/test-rental-records/new",
    "/api/rental-records",
    "/api/rentals"
)

foreach ($path in $apiPaths) {
    Write-Host "`n2. 测试路径: $path" -ForegroundColor Yellow
    try {
        $response = Invoke-WebRequest -Uri "$baseUrl$path" -Method GET -TimeoutSec 5
        Write-Host "✅ 路径 $path 可访问，状态码: $($response.StatusCode)" -ForegroundColor Green
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        if ($statusCode -eq 404) {
            Write-Host "⚠️ 路径 $path 不存在 (404)" -ForegroundColor Yellow
        } elseif ($statusCode -eq 405) {
            Write-Host "⚠️ 路径 $path 方法不允许 (405) - 但端点存在" -ForegroundColor Cyan
        } else {
            Write-Host "❌ 路径 $path 错误: $($_.Exception.Message)" -ForegroundColor Red
        }
    }
}

# 测试POST请求到测试API
Write-Host "`n3. 测试POST请求到测试API..." -ForegroundColor Yellow
$testData = @{
    room_number = "API_TEST_001"
    tenant_name = "API测试租客"
    total_rent = 1000.00
    payment_date = (Get-Date -Format "yyyy-MM-dd")
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/test-rental-records/new" `
        -Method POST `
        -ContentType "application/json" `
        -Body $testData `
        -TimeoutSec 10
    
    Write-Host "✅ POST请求成功:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ POST请求失败: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "响应状态码: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    }
}

Write-Host "`n🏁 API测试完成" -ForegroundColor Green 