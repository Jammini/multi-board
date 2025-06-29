import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import WriteView from '../views/WriteView.vue'
import ReadView from "../views/ReadView.vue";
import EditView from "../views/EditView.vue";
import ListView from "@/views/ListView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/list/:category',
      name: 'list',
      component: ListView,
    },
    {
      path: '/list',
      name: 'list-all',
      component: HomeView,
    },
    {
      path: "/write/:category",
      name: "write",
      component: WriteView,
      beforeEnter: (to, from, next) => {
        const token = localStorage.getItem('jwt_token');
        if (!token) {
          next('/login');  // 로그인되지 않은 경우 login 페이지로 리디렉션
        } else {
          next();  // 로그인 상태라면 계속 진행
        }
      }
    },
    {
      path: "/read/:postId",
      name: "read",
      component: ReadView,
      props: true,
    },
    {
      path: "/edit/:postId",
      name: "edit",
      component: EditView,
      props: true,
      beforeEnter: (to, from, next) => {
        const token = localStorage.getItem('jwt_token');
        if (!token) {
          next('/login');  // 로그인되지 않은 경우 login 페이지로 리디렉션
        } else {
          next();  // 로그인 상태라면 계속 진행
        }
      }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue')
    },
    {
      path: '/signup',
      name: 'signup',
      component: () => import('@/views/SignupView.vue')
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: () => import('@/views/MyPageView.vue')
    },
    {
      path: '/password-reset-request',
      name: 'password-reset-request',
      component: () => import('@/views/PasswordResetRequestView.vue')
    },
    {
      path: '/password-reset/confirm/:token',
      name: 'password-reset-confirm',
      component: () => import('@/views/PasswordResetConfirmView.vue'),
      props: true
    }
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue'),
    // },
  ],
})

// 라우터 가드 추가 - 모든 라우트에 적용
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('jwt_token');
  const permitAllUrls = [ // 로그인 없이 허용된 URL들 (Spring Security 용어와도 잘 맞음)
    "login",
    "signup",
    "password-reset-request",
    "password-reset-confirm",
  ];
  const name = typeof to.name === 'string' ? to.name : '';

  if (!permitAllUrls.includes(name) && !token) { // 로그인 필요
    return next('/login');
  }
  next();
});

export default router
