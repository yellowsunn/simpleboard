import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: "/login",
    name: "Login",
    component: () => import(/* webpackChunkName: "login" */ '@/views/LoginView.vue'),
  },
  {
    path: "/login/naver",
    name: "NaverLoginCallback",
    component: () => import(/* webpackChunkName: "login" */ '@/views/NaverLoginCallbackView.vue'),
  },
  {
    path: "/oauth2/signup",
    name: "OAuth2SignUp",
    component: () => import(/* webpackChunkName: "login" */ '@/views/OAuth2SignUpView.vue'),
  },
  {
    path: "/mypage",
    name: "MyPage",
    component: () => import(/* webpackChunkName: "login" */ '@/views/MyPageView.vue'),
  },
  {
    path: "/mypage/naver/link",
    name: "NaverUserCallbackLink",
    component: () => import(/* webpackChunkName: "login" */ '@/views/NaverUserLinkCallbackView.vue'),
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
