// 前端API调用测试脚本
// 可以在浏览器控制台中运行此脚本来测试API调用

console.log('🔧 开始测试前端API调用...');

// 测试数据
const testPaymentData = {
    room_number: 'FRONTEND_TEST_001',
    tenant_name: '前端测试租客',
    total_rent: 1500.75,
    payment_date: new Date().toISOString().split('T')[0]
};

console.log('📝 测试数据:', testPaymentData);

// 测试新楼API
async function testNewFloorAPI() {
    try {
        console.log('🏢 测试六楼API...');
        const response = await fetch('/api/test-rental-records/new', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(testPaymentData)
        });
        
        const result = await response.json();
        console.log('✅ 六楼API响应:', result);
        
        if (result.code === 200) {
            console.log('🎉 六楼API测试成功！');
        } else {
            console.log('⚠️ 六楼API返回错误:', result.message);
        }
    } catch (error) {
        console.error('❌ 六楼API测试失败:', error);
    }
}

// 测试旧楼API
async function testOldFloorAPI() {
    try {
        console.log('🏢 测试五楼API...');
        const response = await fetch('/api/test-rental-records/old', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(testPaymentData)
        });
        
        const result = await response.json();
        console.log('✅ 五楼API响应:', result);
        
        if (result.code === 200) {
            console.log('🎉 五楼API测试成功！');
        } else {
            console.log('⚠️ 五楼API返回错误:', result.message);
        }
    } catch (error) {
        console.error('❌ 五楼API测试失败:', error);
    }
}

// 测试查询API
async function testQueryAPI() {
    try {
        console.log('📊 测试查询API...');
        const response = await fetch('/api/rental-records/6?size=5');
        const result = await response.json();
        console.log('✅ 查询API响应:', result);
        
        if (result.code === 200) {
            console.log('🎉 查询API测试成功！');
            console.log('📋 记录数量:', result.data.totalElements);
        } else {
            console.log('⚠️ 查询API返回错误:', result.message);
        }
    } catch (error) {
        console.error('❌ 查询API测试失败:', error);
    }
}

// 运行所有测试
async function runAllTests() {
    console.log('🚀 开始运行所有测试...');
    
    await testNewFloorAPI();
    await new Promise(resolve => setTimeout(resolve, 1000)); // 等待1秒
    
    await testOldFloorAPI();
    await new Promise(resolve => setTimeout(resolve, 1000)); // 等待1秒
    
    await testQueryAPI();
    
    console.log('🎉 所有测试完成！');
}

// 修复Payments.vue中的createPaymentRecord函数
const fixedCreatePaymentRecord = `
// 修复后的createPaymentRecord函数
const createPaymentRecord = async (payment, retryCount = 0) => {
  const maxRetries = 2
  
  try {
    console.log(\`🔄 开始创建缴费记录 (尝试次数: \${retryCount + 1}/\${maxRetries + 1}):\`, payment)
    
    // 构建缴费记录数据 - 使用正确的字段名
    const totalRentValue = payment.totalDue || payment.monthlyRent || 0
    const rentalRecordData = {
      room_number: payment.roomNumber || '',
      tenant_name: payment.tenantName || '',
      total_rent: totalRentValue,
      payment_date: new Date().toISOString().split('T')[0]
    }

    console.log('📝 缴费记录数据:', rentalRecordData)

    // 数据验证
    if (!rentalRecordData.room_number) {
      throw new Error('房间号不能为空')
    }
    if (!rentalRecordData.tenant_name) {
      throw new Error('租客姓名不能为空')
    }
    if (!totalRentValue || totalRentValue <= 0) {
      throw new Error('缴费金额必须大于0')
    }

    // 根据楼层选择对应的API
    let response
    if (currentFloor.value === '6') {
      response = await addNewRentalRecord(rentalRecordData)
    } else {
      response = await addOldRentalRecord(rentalRecordData)
    }

    console.log('📡 缴费记录创建响应:', response)

    // 检查响应状态
    if (response.data && response.data.code === 200) {
      console.log('✅ 缴费记录创建成功')
      return response
    } else {
      throw new Error(response.data?.message || '缴费记录创建失败')
    }
  } catch (error) {
    console.error(\`❌ 创建缴费记录失败 (尝试 \${retryCount + 1}):\`, error)
    
    if (retryCount < maxRetries) {
      console.log(\`🔄 正在重试创建缴费记录...\`)
      await new Promise(resolve => setTimeout(resolve, 1000 * (retryCount + 1)))
      return createPaymentRecord(payment, retryCount + 1)
    }
    
    throw new Error(\`缴费记录同步失败: \${error.message}\`)
  }
}
`;

console.log('📋 修复后的函数代码:');
console.log(fixedCreatePaymentRecord);

console.log('\n🎯 使用说明:');
console.log('1. 运行 runAllTests() 来测试所有API');
console.log('2. 确保后端服务在 http://localhost:8080 运行');
console.log('3. 检查浏览器网络面板中的API调用');

// 自动运行测试（可选）
// runAllTests(); 