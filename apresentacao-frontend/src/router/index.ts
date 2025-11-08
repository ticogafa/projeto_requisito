import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue')
    },
    {
      path: '/',
      name: 'profile-selection',
      component: () => import('@/views/ProfileSelectionView.vue')
    },
    {
      path: '/professional-panel',
      name: 'professional-panel',
      component: () => import('@/views/ProfessionalView.vue')
    },
    {
      path: '/client-panel',
      name: 'client-panel',
      component: () => import('@/views/ClienteView.vue')
    }
  ],
})

export default router
