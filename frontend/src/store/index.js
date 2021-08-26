import Vue from 'vue';
import Vuex from 'vuex';
import actions from '@/store/actions';
import mutations from '@/store/mutations';
import { Deque } from '@/common/Deque';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    isLoggedIn: false,
    userInfo: '',
    admin: {
      data: {
        users: [],
        totalSize: 0,
        lastPage: false
      }
    },
    boardDto: '',
    postDto: '',
    postHit: '',
    commentDto: '',
    usersDto: '',

    chatDto: '',
    chatDeque: new Deque(),
    chatSet: new Set(),

    page: 0,
    infiniteId: +new Date() // +는 숫자변환
  },
  getters: {
    adminData(state) {
      return state.admin.data;
    },
    boardData(state) {
      return state.boardDto;
    }
  },
  actions,
  mutations,
});