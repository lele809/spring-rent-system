import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue'),
    meta: { title: '仪表盘' }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/floor/:floor',
    name: 'FloorManagement',
    component: () => import('../views/FloorManagement.vue'),
    meta: { title: '楼层管理' },
    redirect: (to) => `/floor/${to.params.floor}/home`,
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { title: '主页' }
      },
      {
        path: 'rooms',
        name: 'Rooms',
        component: () => import('../views/Rooms.vue'),
        meta: { title: '房间管理' }
      },
      {
        path: 'tenants',
        name: 'Tenants',
        component: () => import('../views/Tenants.vue'),
        meta: { title: '联系人管理' }
      },
      {
        path: 'rentalInfo',
        name: 'RentalInfo',
        component: () => import('../views/RentInfo.vue'),
        meta: { title: '租房信息' }
      },
      {
        path: 'contracts',
        name: 'Contracts',
        component: () => import('../views/Contracts.vue'),
        meta: { title: '合同管理' }
      },
      {
        path: 'payments',
        name: 'Payments',
        component: () => import('../views/Payments.vue'),
        meta: { title: '费用管理' }
      },
      {
        path: 'rental-records',
        name: 'RentalRecord',
        component: () => import('../views/RentalRecord.vue'),
        meta: { title: '缴费记录' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta?.title) {
    document.title = `${to.meta.title} - 房客管理系统`
  }
  next()
})

export default router