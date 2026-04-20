import { createRouter, createWebHistory } from 'vue-router'
import PortfolioView from '../views/PortfolioView.vue'
import AdminLogin from '../views/AdminLogin.vue'
import AdminDashboard from '../views/AdminDashboard.vue'
import ProjectDetail from '../views/ProjectDetail.vue'

const routes = [
  { path: '/', component: PortfolioView },
  { path: '/projects/:id', component: ProjectDetail },
  { path: '/admin', component: AdminLogin },
  {
    path: '/admin/dashboard',
    component: AdminDashboard,
    beforeEnter: (to, from, next) => {
      const token = localStorage.getItem('admin_token')
      if (!token) {
        next('/admin')
      } else {
        next()
      }
    }
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
