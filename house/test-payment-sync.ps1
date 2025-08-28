#!/usr/bin/env pwsh
# 测试缴费记录同步功能

Write-Host "🚀 开始测试缴费记录同步功能..." -ForegroundColor Green

# 测试数据
$testData = @{
    room_number = "TEST_SYNC_001"
    tenant_name = "测试租客_同步"
    total_rent = 1500.50
    payment_date = (Get-Date -Format "yyyy-MM-dd")
}

Write-Host "📝 测试数据:" -ForegroundColor Yellow
$testData | ConvertTo-Json -Depth 2

# 测试新楼缴费记录API
Write-Host "`n🏢 测试六楼缴费记录API..." -ForegroundColor Blue
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/test-rental-records/new" `
        -Method POST `
        -ContentType "application/json" `
        -Body ($testData | ConvertTo-Json)
    
    Write-Host "✅ 六楼API响应:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ 六楼API测试失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 测试旧楼缴费记录API
Write-Host "`n🏢 测试五楼缴费记录API..." -ForegroundColor Blue
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/test-rental-records/old" `
        -Method POST `
        -ContentType "application/json" `
        -Body ($testData | ConvertTo-Json)
    
    Write-Host "✅ 五楼API响应:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ 五楼API测试失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 验证数据库记录
Write-Host "`n📊 验证数据库记录..." -ForegroundColor Blue
try {
    # 查询新楼记录
    $newRecords = Invoke-RestMethod -Uri "http://localhost:8080/api/rental-records/6?keyword=TEST_SYNC_001" `
        -Method GET
    
    Write-Host "✅ 六楼记录查询结果:" -ForegroundColor Green
    $newRecords | ConvertTo-Json -Depth 3
    
    # 查询旧楼记录
    $oldRecords = Invoke-RestMethod -Uri "http://localhost:8080/api/rental-records/5?keyword=TEST_SYNC_001" `
        -Method GET
    
    Write-Host "✅ 五楼记录查询结果:" -ForegroundColor Green
    $oldRecords | ConvertTo-Json -Depth 3
    
} catch {
    Write-Host "❌ 数据库验证失败: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 测试完成！" -ForegroundColor Green
Write-Host "请检查上面的输出结果，确认缴费记录同步功能是否正常工作。" -ForegroundColor Yellow 