import axios from "axios";
import {isAccessTokenExpired} from "@/utils/httpErrorHandler";

export default {
  methods: {
    $getRequestParam(param) {
      const urlSearchParams = new URLSearchParams(window.location.search);
      const params = Object.fromEntries(urlSearchParams.entries());
      return params[param]
    },
    async $boardApi(method, url, data, isRequireAuth = false, headers = {"Content-Type": "application/json"}) {
      for (let i = 0; i < 2; i++) {
        if (isRequireAuth) {
          headers = {
            ...headers,
            'Authorization': 'bearer ' + this.$store.state?.userToken?.accessToken,
          }
        }
        try {
          const response = await axios({
            baseURL: process.env.VUE_APP_BOARD_API_BASE_URL,
            url,
            method,
            data,
            headers,
          });
          const responseData = response?.data || {};
          return {
            isError: false,
            data: responseData,
          }
        } catch (e) {
          const errorResponse = e?.response?.data
          if (isRequireAuth && isAccessTokenExpired(errorResponse)) {
            if (i === 0) {
              try {
                const res = await axios({
                  baseURL: process.env.VUE_APP_BOARD_API_BASE_URL,
                  url: '/api/v2/auth/token',
                  method: "POST",
                  data: this.$store.state?.userToken
                });
                await this.$store.commit('setUserToken', res?.data)
                continue
              } catch (e) {
                this.$store.commit('deleteUserToken')
                this.$router.push('/')
              }
            }
            this.$store.commit('deleteUserToken')
            this.$router.push('/')
          }

          return {
            isError: true,
            data: {
              message: "알 수 없는 에러가 발생하였습니다.",
              ...errorResponse
            }
          }
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
