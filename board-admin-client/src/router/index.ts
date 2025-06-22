import { createRouter, createWebHistory } from 'vue-router'
import HomeView from "@/views/HomeView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue')
    },
    {
      path: '/category-manage',
      name: 'category-manage',
      component: () => import('@/views/CategoryManageView.vue')
    },
  ],
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('jwt_token');
  const permitAllUrls = [ // 로그인 없이 허용된 URL들 (Spring Security 용어와도 잘 맞음)
    "login",
  ];
  const name = typeof to.name === 'string' ? to.name : '';

  if (!permitAllUrls.includes(name) && !token) { // 로그인 필요
    return next('/login');
  }
  next();
});


export default router
