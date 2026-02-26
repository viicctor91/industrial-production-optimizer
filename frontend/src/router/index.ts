import { createRouter, createWebHistory } from 'vue-router'
import RawMaterialView from '@/views/RawMaterialView.vue'
import ProductListView from '@/views/ProductListView.vue'
import ProductFormView from '@/views/ProductFormView.vue'
import ProductionView from '@/views/ProductionView.vue'
import LoginView from '@/views/LoginView.vue'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/raw-materials'
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/raw-materials',
      name: 'raw-materials',
      component: RawMaterialView
    },
    {
      path: '/products',
      name: 'products',
      component: ProductListView
    },
    {
      path: '/products/new',
      name: 'product-new',
      component: ProductFormView
    },
    {
      path: '/products/:id/edit',
      name: 'product-edit',
      component: ProductFormView
    },
    {
      path: '/production',
      name: 'production',
      component: ProductionView
    }
  ]
})

router.beforeEach((to) => {
  const auth = useAuthStore();
  if (to.name !== 'login' && !auth.isAuthenticated) {
    return { name: 'login' };
  }
});

export default router
