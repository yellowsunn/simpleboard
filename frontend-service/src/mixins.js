import {callBoardApi, reAcquireAccessToken} from "@/utils/apiUtils";
import AccessTokenExpiredError from "@/utils/AccessTokenExpiredError";
import {isInvalidUser} from "@/utils/httpErrorHandler";
import uuid4 from 'uuid4';

export default {
  methods: {
    $getRequestParam(param) {
      const urlSearchParams = new URLSearchParams(window.location.search);
      const params = Object.fromEntries(urlSearchParams.entries());
      return params[param]
    },
    async $boardApi(method, url, data, isRequireAuth = false, headers = {"Content-Type": "application/json"}) {
      try {
        const response = await callBoardApi(method, url, data, isRequireAuth, headers);
        if (response?.isError && isInvalidUser(response?.data)) {
          this.$store.commit('deleteUserToken')
        }
        return response
      } catch (e) {
        if (e instanceof AccessTokenExpiredError) {
          const res = await reAcquireAccessToken()
          await this.$store.commit('setUserToken', res?.data?.data)
          return await callBoardApi(method, url, data, isRequireAuth, headers)
        }
      }
    },
    // csrf 토큰 용도
    $setSessionState() {
      const uuid = uuid4()
      sessionStorage.setItem("state", uuid)
      return uuid
    },
    $getSessionState() {
      return sessionStorage.getItem("state")
    },
  }
}
