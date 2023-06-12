import {createRouter, createWebHistory} from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import(/* webpackChunkName: "article" */ '@/views/ArticleListView.vue'),
  },
  {
    path: "/login",
    name: "Login",
    component: () => import(/* webpackChunkName: "login" */ '@/views/LoginView.vue'),
  },
  {
    path: "/login/naver",
    name: "NaverLoginCallback",
    component: () => import(/* webpackChunkName: "login-naver" */ '@/views/callback/NaverLoginCallbackView.vue'),
  },
  {
    path: "/login/kakao",
    name: "KakaoLoginCallback",
    component: () => import(/* webpackChunkName: "login-kakao" */ '@/views/callback/KakaoLoginCallbackView.vue'),
  },
  {
    path: "/email/signup",
    name: "EmailSignUp",
    component: () => import(/* webpackChunkName: "email-signup" */ '@/views/EmailSignUpView.vue'),
  },
  {
    path: "/oauth2/signup",
    name: "OAuth2SignUp",
    component: () => import(/* webpackChunkName: "oauth-signup" */ '@/views/OAuth2SignUpView.vue'),
  },
  {
    path: "/mypage",
    name: "MyPage",
    component: () => import(/* webpackChunkName: "mypage" */ '@/views/MyPageView.vue'),
  },
  {
    path: "/mypage/naver/link",
    name: "NaverUserLinkCallbackLink",
    component: () => import(/* webpackChunkName: "mypage-naver-link" */ '@/views/callback/NaverUserLinkCallbackView.vue'),
  },
  {
    path: "/mypage/kakao/link",
    name: "KakaoUserLinkCallback",
    component: () => import(/* webpackChunkName: "mypage-kakao-link" */ '@/views/callback/KakaoUserLinkCallbackView.vue'),
  },
  {
    path: "/articles",
    name: "Articles",
    component: () => import(/* webpackChunkName: "articles" */ '@/views/ArticleListView.vue'),
  },
  {
    path: "/articles/new",
    name: "ArticleNew",
    component: () => import(/* webpackChunkName: "article-new" */ '@/views/ArticleWriteView.vue'),
  },
  {
    path: "/articles/:id",
    name: "Article",
    component: () => import(/* webpackChunkName: "article" */ '@/views/ArticleView.vue'),
  },
  {
    path: "/articles/:id/edit",
    name: "ArticleEdit",
    component: () => import(/* webpackChunkName: "article-edit" */ '@/views/ArticleWriteView.vue'),
  },
  {
    path: "/notifications",
    name: "Notifications",
    component: () => import(/* webpackChunkName: "notifications" */ '@/views/NotificationListView.vue'),
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  scrollBehavior(to) {
    if (to.hash) {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve({
            el: to.hash
          })
        }, 300)
      })
    }
  }
})

export default router
