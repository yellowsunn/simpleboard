import {createStore} from "vuex";

const ACCESS_TOKEN = "board-access-token"
const REFRESH_TOKEN = "board-refresh-token"

function getCookieValue(key) {
  return document.cookie.split("; ")
    .find((row) => row.startsWith(`${key}=`))
    ?.split("=")[1]
}

const store = createStore({
  state() {
    return {
      userToken: {
        access: getCookieValue(ACCESS_TOKEN),
        refresh: getCookieValue(REFRESH_TOKEN),
      },
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
      document.cookie = `${ACCESS_TOKEN}=${userToken.accessToken}; SameSite=Strict; Secure`
      document.cookie = `${REFRESH_TOKEN}=${userToken.refreshToken}; SameSite=Strict; Secure`
    },
    deleteUserToken(state) {
      state.userToken = {}
      document.cookie = `${ACCESS_TOKEN}=; max-age=0`
      document.cookie = `${REFRESH_TOKEN}=; max-age=0`
    }
  }
})

export default store;
