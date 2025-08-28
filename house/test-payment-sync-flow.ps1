# 测试支付状态同步到缴费记录的完整流程
# 确保后端服务正在运行在 http://localhost:8080

$baseUrl = "http://localhost:8080/api"
$headers = @{
    "Content-Type" = "application/json"
    "Accept" = "application/json"
}

Write-Host "=== 测试支付状态同步到缴费记录流程 ===" -ForegroundColor Green

# 1. 测试获取未支付的租房记录
Write-Host "`n1. 获取六楼未支付记录..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/rentals/6?paymentStatus=2&size=5" -Method GET -Headers $headers
    if ($response.code -eq 200) {
        $unpaidRecords = $response.data.content
        Write-Host "✅ 找到 $($unpaidRecords.Count) 条未支付记录" -ForegroundColor Green
        
        if ($unpaidRecords.Count -gt 0) {
            $testRecord = $unpaidRecords[0]
            Write-Host "📝 测试记录: 房间=$($testRecord.roomNumber), 租客=$($testRecord.tenantName), 金额=$($testRecord.totalDue)" -ForegroundColor Cyan
            
            # 2. 获取缴费记录更新前的数量
            Write-Host "`n2. 获取更新前缴费记录数量..." -ForegroundColor Yellow
            $beforeResponse = Invoke-RestMethod -Uri "$baseUrl/rental-records/6?size=1" -Method GET -Headers $headers
            $beforeCount = $beforeResponse.data.totalElements
            Write-Host "✅ 更新前缴费记录数量: $beforeCount" -ForegroundColor Green
            
            # 3. 更新支付状态为已缴费
            Write-Host "`n3. 更新支付状态为已缴费..." -ForegroundColor Yellow
            $updateBody = @{
                status = 1
            } | ConvertTo-Json
            
            $updateResponse = Invoke-RestMethod -Uri "$baseUrl/rentals/6/$($testRecord.id)/status" -Method PATCH -Headers $headers -Body $updateBody
            if ($updateResponse.code -eq 200) {
                Write-Host "✅ 支付状态更新成功" -ForegroundColor Green
                
                # 等待一秒确保数据同步
                Start-Sleep -Seconds 2
                
                # 4. 验证缴费记录是否自动创建
                Write-Host "`n4. 验证缴费记录是否自动创建..." -ForegroundColor Yellow
                $afterResponse = Invoke-RestMethod -Uri "$baseUrl/rental-records/6?size=1" -Method GET -Headers $headers
                $afterCount = $afterResponse.data.totalElements
                Write-Host "✅ 更新后缴费记录数量: $afterCount" -ForegroundColor Green
                
                if ($afterCount -gt $beforeCount) {
                    Write-Host "🎉 缴费记录自动创建成功！新增 $($afterCount - $beforeCount) 条记录" -ForegroundColor Green
                    
                    # 5. 查看最新的缴费记录
                    Write-Host "`n5. 查看最新缴费记录..." -ForegroundColor Yellow
                    $latestRecords = Invoke-RestMethod -Uri "$baseUrl/rental-records/6?size=3&sortBy=createdAt&sortDirection=desc" -Method GET -Headers $headers
                    $latestRecord = $latestRecords.data.content[0]
                    Write-Host "📋 最新缴费记录:" -ForegroundColor Cyan
                    Write-Host "   房间号: $($latestRecord.roomNumber)" -ForegroundColor White
                    Write-Host "   租客: $($latestRecord.tenantName)" -ForegroundColor White
                    Write-Host "   金额: ¥$($latestRecord.totalRent)" -ForegroundColor White
                    Write-Host "   缴费日期: $($latestRecord.paymentDate)" -ForegroundColor White
                    Write-Host "   创建时间: $($latestRecord.createdAt)" -ForegroundColor White
                    
                    # 验证数据是否匹配
                    if ($latestRecord.roomNumber -eq $testRecord.roomNumber -and $latestRecord.tenantName -eq $testRecord.tenantName) {
                        Write-Host "✅ 数据匹配正确！" -ForegroundColor Green
                    } else {
                        Write-Host "❌ 数据不匹配，可能有问题" -ForegroundColor Red
                    }
                } else {
                    Write-Host "❌ 缴费记录未自动创建，请检查后端逻辑" -ForegroundColor Red
                }
                
                # 6. 恢复支付状态为未缴费（用于下次测试）
                Write-Host "`n6. 恢复支付状态为未缴费..." -ForegroundColor Yellow
                $revertBody = @{
                    status = 2
                } | ConvertTo-Json
                
                $revertResponse = Invoke-RestMethod -Uri "$baseUrl/rentals/6/$($testRecord.id)/status" -Method PATCH -Headers $headers -Body $revertBody
                if ($revertResponse.code -eq 200) {
                    Write-Host "✅ 支付状态已恢复为未缴费" -ForegroundColor Green
                } else {
                    Write-Host "⚠️ 恢复支付状态失败: $($revertResponse.message)" -ForegroundColor Yellow
                }
                
            } else {
                Write-Host "❌ 支付状态更新失败: $($updateResponse.message)" -ForegroundColor Red
            }
        } else {
            Write-Host "⚠️ 没有找到未支付记录，请先创建一些测试数据" -ForegroundColor Yellow
        }
    } else {
        Write-Host "❌ 获取租房记录失败: $($response.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ 测试过程中发生错误: $($_.Exception.Message)" -ForegroundColor Red
}

# 7. 测试五楼的情况
Write-Host "`n=== 测试五楼支付状态同步 ===" -ForegroundColor Green
try {
    $response5 = Invoke-RestMethod -Uri "$baseUrl/rentals/5?paymentStatus=2&size=3" -Method GET -Headers $headers
    if ($response5.code -eq 200 -and $response5.data.content.Count -gt 0) {
        $testRecord5 = $response5.data.content[0]
        Write-Host "📝 五楼测试记录: 房间=$($testRecord5.roomNumber), 租客=$($testRecord5.tenantName)" -ForegroundColor Cyan
        
        # 获取五楼缴费记录更新前的数量
        $before5Response = Invoke-RestMethod -Uri "$baseUrl/rental-records/5?size=1" -Method GET -Headers $headers
        $before5Count = $before5Response.data.totalElements
        
        # 更新五楼支付状态
        $update5Body = @{ status = 1 } | ConvertTo-Json
        $update5Response = Invoke-RestMethod -Uri "$baseUrl/rentals/5/$($testRecord5.id)/status" -Method PATCH -Headers $headers -Body $update5Body
        
        if ($update5Response.code -eq 200) {
            Start-Sleep -Seconds 2
            $after5Response = Invoke-RestMethod -Uri "$baseUrl/rental-records/5?size=1" -Method GET -Headers $headers
            $after5Count = $after5Response.data.totalElements
            
            if ($after5Count -gt $before5Count) {
                Write-Host "🎉 五楼缴费记录自动创建成功！" -ForegroundColor Green
            } else {
                Write-Host "❌ 五楼缴费记录未自动创建" -ForegroundColor Red
            }
            
            # 恢复状态
            $revert5Body = @{ status = 2 } | ConvertTo-Json
            Invoke-RestMethod -Uri "$baseUrl/rentals/5/$($testRecord5.id)/status" -Method PATCH -Headers $headers -Body $revert5Body
            Write-Host "✅ 五楼支付状态已恢复" -ForegroundColor Green
        }
    } else {
        Write-Host "⚠️ 五楼没有未支付记录" -ForegroundColor Yellow
    }
} catch {
    Write-Host "⚠️ 五楼测试失败: $($_.Exception.Message)" -ForegroundColor Yellow
}

Write-Host "`n=== 测试完成 ===" -ForegroundColor Green
Write-Host "请检查前端页面，确认缴费记录页面能正确显示同步的数据" -ForegroundColor Cyan 