import {createStore} from "vuex";
import {clearToken, getAccessToken, getRefreshToken, getUserInfo, setToken} from "@/utils/tokenUtils";


const store = createStore({
  state() {
    return {
      userToken: {
        accessToken: getAccessToken(),
        refreshToken: getRefreshToken(),
      },
      userInfo: getUserInfo(),
      userProviders: null,
      isEditFinished: false,
    }
  },
  getters: {
    isLogin(state) {
      return !!state?.userToken?.accessToken
    },
  },
  mutations: {
    setUserToken(state, userToken) {
      if (!userToken?.accessToken) {
        return
      }

      state.userToken = userToken
      setToken(userToken)
      state.userInfo = getUserInfo()
    },
    deleteUserToken(state) {
      state.userToken = {}
      clearToken()
      state.userInfo = null
    },
    setUserProviders(state, providers) {
      state.userProviders = new Set(providers)
    },
    addUserProvider(state, provider) {
      state.userProviders?.add(provider)
    },
    deleteUserProvider(state, provider) {
      state.userProviders?.delete(provider)
    },
    setEditFinished(state) {
      state.isEditFinished = true
    },
    unsetEditFinished(state) {
      state.isEditFinished = false
    },
  },
  actions: {
    setEditFinished({commit}) {
      commit('setEditFinished')
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve()
        }, 1500)
      })
    },
    async editFinished({dispatch, commit}) {
      await dispatch('setEditFinished')
      commit('unsetEditFinished')
    }
  }
})

export default store;
