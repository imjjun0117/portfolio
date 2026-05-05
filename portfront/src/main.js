import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import axios from 'axios'

// 401 응답 시 자동 로그아웃 처리
axios.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) {
      const isAdminRoute = window.location.pathname.startsWith('/admin')
      if (isAdminRoute && window.location.pathname !== '/admin') {
        localStorage.removeItem('admin_token')
        router.push('/admin')
      }
    }
    return Promise.reject(err)
  }
)

createApp(App).use(router).mount('#app')
