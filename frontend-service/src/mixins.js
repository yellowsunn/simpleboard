import axios from "axios";

const handleUnAuthorizedStatus = (e, store) => {
  if (e?.response?.status === 401) {
    alert("로그인이 필요합니다.")
    store.commit('deleteUserToken')
    window.location = "/";
  }
};

export default {
  methods: {
    $getRequestParam(param) {
      const urlSearchParams = new URLSearchParams(window.location.search);
      const params = Object.fromEntries(urlSearchParams.entries());
      return params[param]
    },
    async $boardApi(method, url, data, isRequireAuth = false, headers = {"Content-Type": "application/json"}) {
      if (isRequireAuth) {
        headers = {
          ...headers,
          'Authorization': 'bearer ' + this.$store.state?.userToken?.access,
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
        return response?.data || {}
      } catch (e) {
        console.log(e)
        handleUnAuthorizedStatus(e, this.$store)
        return e?.response?.data || {}
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
