import {createStore} from "vuex";
import {ACCESS_TOKEN, REFRESH_TOKEN, getAccessToken, getRefreshToken} from "@/utils/tokenUtils";


const store = createStore({
  state() {
    return {
      userToken: {
        access: getAccessToken(),
        refresh: getRefreshToken(),
      },
      userProviders: null,
      isEditFinished: false,
    }
  },
  getters: {
    isLogin(state) {
      return !!state?.userToken?.access
    },
  },
  mutations: {
    setUserToken(state, userToken) {
      state.userToken = {
        access: userToken.accessToken,
        refresh: userToken.refreshToken,
      }
      document.cookie = `${ACCESS_TOKEN}=${userToken.accessToken}; SameSite=Strict; Domain=${process.env.VUE_APP_DOMAIN}; path=/;`
      document.cookie = `${REFRESH_TOKEN}=${userToken.refreshToken}; SameSite=Strict; Domain=${process.env.VUE_APP_DOMAIN}; path=/;`
    },
    deleteUserToken(state) {
      state.userToken = {}
      document.cookie = `${ACCESS_TOKEN}=; max-age=0; Domain=${process.env.VUE_APP_DOMAIN}`
      document.cookie = `${REFRESH_TOKEN}=; max-age=0; Domain=${process.env.VUE_APP_DOMAIN}`
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
