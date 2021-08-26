import Vue from 'vue';
import { store } from '@/store';
import VueRouter from 'vue-router';
import HomeView from '@/view/HomeView';
import Login from '@/components/Login';
import Register from '@/components/Register';
import UserView from '@/view/UserView';
import AdminView from '@/view/AdminView';
import WriteView from '@/view/WriteView';
import PostView from '@/view/PostView';
import MyInfoView from '@/view/MyInfoView';

Vue.use(VueRouter);

export const router = new VueRouter({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: HomeView,
      beforeEnter: checkLoggedIn
    },
    {
      path: '/register',
      component: Register
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/logout',
      beforeEnter: async (to, from, next) => {
        try {
          await store.dispatch('FETCH_LOGOUT');
        } catch (error) {
          console.log(error.data);
        }
        next("/");
      }
    },
    {
      path: '/posts',
      component: UserView,
      beforeEnter: redirectLoginPage
    },
    {
      path: '/posts/write',
      component: WriteView,
      beforeEnter: async (to, from, next) => {
        await redirectLoginPage(to, from, next);
        next();
      }
    },
    {
      path: '/posts/:postId',
      component: PostView,
      beforeEnter: async (to, from, next) => {
        try {
          await redirectLoginPage(to, from, next);
          await store.dispatch('GET_POST_DATA', to.params.postId);
          await store.dispatch('GET_POST_HIT', to.params.postId);
          await store.dispatch('GET_COMMENT_DATA', { postId: to.params.postId });
          next();
        } catch (error) {
          next('/');
        }
      }
    },
    {
      path: '/posts/:postId/edit',
      component: WriteView,
      beforeEnter: async (to, from, next) => {
        await redirectLoginPage(to, from, next);
        if (to.params.postId !== undefined) {
          await store.dispatch('GET_POST_DATA', to.params.postId);
        }
        next();
      }
    },
    {
      path: '/users',
      component: AdminView,
      beforeEnter: async (to, from, next) => {
        await redirectLoginPage(to, from, next);
      }
    },
    {
      path: '/users/myinfo',
      children: [
        {
          path: 'change',
          component: MyInfoView
        }
      ],
      component: MyInfoView,
      beforeEnter: async (to, from, next) => {
        await redirectLoginPage(to, from, next);
      }
    }
  ]
});

async function checkLoggedIn(to, from, next) {
  if (!store.state.userInfo) {
    await store.dispatch('FETCH_CURRENT_USER');
  }
  next();
}

async function redirectLoginPage(to, from, next) {
  if (!store.state.userInfo) {
    await store.dispatch('FETCH_CURRENT_USER');
  }

  if (!store.state.isLoggedIn) {
    await next({
      path: '/login',
      query: {
        nextUrl: to.fullPath
      }
    });
  } else {
    next();
  }
}