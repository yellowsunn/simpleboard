import {createStore} from "vuex";
import {ACCESS_TOKEN, getAccessToken, getRefreshToken, REFRESH_TOKEN} from "@/utils/tokenUtils";


const store = createStore({
  state() {
    return {
      userToken: {
        accessToken: getAccessToken(),
        refreshToken: getRefreshToken(),
      },
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
      if (!userToken) {
        return
      }
      state.userToken = userToken
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
