<template>
  <div class="test-data">
    <h1>数据库连接测试</h1>
    
    <div class="test-section">
      <h2>连接测试</h2>
      <el-button @click="testConnection" type="primary">测试数据库连接</el-button>
      <div v-if="connectionResult" class="result">{{ connectionResult }}</div>
    </div>
    
    <div class="test-section">
      <h2>六楼数据测试</h2>
      <el-button @click="testNewData" type="success">获取六楼数据</el-button>
      <div v-if="newDataResult" class="result">
        <p>数据数量: {{ newDataResult.length }}</p>
        <pre>{{ JSON.stringify(newDataResult, null, 2) }}</pre>
      </div>
    </div>
    
    <div class="test-section">
      <h2>五楼数据测试</h2>
      <el-button @click="testOldData" type="warning">获取五楼数据</el-button>
      <div v-if="oldDataResult" class="result">
        <p>数据数量: {{ oldDataResult.length }}</p>
        <pre>{{ JSON.stringify(oldDataResult, null, 2) }}</pre>
      </div>
    </div>
    
    <div class="test-section">
      <h2>房间数据测试</h2>
      <el-button @click="testRoomsNewAPI" type="success">测试六楼房间数据</el-button>
      <el-button @click="testRoomsOldAPI" type="warning">测试五楼房间数据</el-button>
      <div v-if="roomsResult" class="result">
        <p>房间数据数量: {{ roomsResult.length }}</p>
        <pre>{{ JSON.stringify(roomsResult, null, 2) }}</pre>
      </div>
    </div>
    
    <div class="test-section">
      <h2>API测试</h2>
      <el-button @click="testRentalsAPI" type="info">测试Rentals API</el-button>
      <div v-if="apiResult" class="result">
        <pre>{{ JSON.stringify(apiResult, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../api/request'
import { getRentals } from '../api/rentals'

const connectionResult = ref('')
const newDataResult = ref(null)
const oldDataResult = ref(null)
const apiResult = ref(null)
const roomsResult = ref(null)

const testConnection = async () => {
  try {
    const response = await request.get('/test/db-connection')
    connectionResult.value = response.data.message
    ElMessage.success('连接测试成功')
  } catch (error) {
    connectionResult.value = '连接失败: ' + (error as any).message
    ElMessage.error('连接测试失败')
  }
}

const testNewData = async () => {
  try {
    const response = await request.get('/test/rental-new')
    newDataResult.value = response.data.data
    ElMessage.success('六楼数据获取成功')
  } catch (error) {
    newDataResult.value = null
    ElMessage.error('六楼数据获取失败')
  }
}

const testOldData = async () => {
  try {
    const response = await request.get('/test/rental-old')
    oldDataResult.value = response.data.data
    ElMessage.success('五楼数据获取成功')
  } catch (error) {
    oldDataResult.value = null
    ElMessage.error('五楼数据获取失败')
  }
}

const testRentalsAPI = async () => {
  try {
    const response = await getRentals('6', {
      page: 0,
      size: 20
    })
    apiResult.value = response.data
    ElMessage.success('Rentals API测试成功')
  } catch (error) {
    apiResult.value = { error: (error as any).message }
    ElMessage.error('Rentals API测试失败')
  }
}

const testRoomsNewAPI = async () => {
  try {
    const response = await request.get('/test/rooms-new')
    roomsResult.value = response.data.data
    ElMessage.success('六楼房间数据获取成功')
  } catch (error) {
    roomsResult.value = null
    ElMessage.error('六楼房间数据获取失败')
  }
}

const testRoomsOldAPI = async () => {
  try {
    const response = await request.get('/test/rooms-old')
    roomsResult.value = response.data.data
    ElMessage.success('五楼房间数据获取成功')
  } catch (error) {
    roomsResult.value = null
    ElMessage.error('五楼房间数据获取失败')
  }
}
</script>

<style scoped>
.test-data {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.test-section {
  margin: 20px 0;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.result {
  margin-top: 15px;
  padding: 10px;
  background: #f5f5f5;
  border-radius: 4px;
  max-height: 400px;
  overflow-y: auto;
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style> 