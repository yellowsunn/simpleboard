import {callBoardApi, reAcquireAccessToken} from "@/utils/apiUtils";
import AccessTokenExpiredError from "@/utils/AccessTokenExpiredError";

export default {
  methods: {
    $getRequestParam(param) {
      const urlSearchParams = new URLSearchParams(window.location.search);
      const params = Object.fromEntries(urlSearchParams.entries());
      return params[param]
    },
    async $boardApi(method, url, data, isRequireAuth = false, headers = {"Content-Type": "application/json"}) {
      try {
        return await callBoardApi(method, url, data, isRequireAuth, headers)
      } catch (e) {
        if (e instanceof AccessTokenExpiredError) {
          const res = await reAcquireAccessToken()
          await this.$store.commit('setUserToken', res.data)
          return await callBoardApi(method, url, data, isRequireAuth, headers)
        }
      }
    },
    // csrf 토큰 용도
    $setSessionState() {
      const uuid = self.crypto.randomUUID()
      sessionStorage.setItem("state", uuid)
      return uuid
    },
    $getSessionState() {
      return sessionStorage.getItem("state")
    },
  }
}
