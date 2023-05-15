import axios from "axios";

export default {
  methods: {
    $getRequestParam(param) {
      const urlSearchParams = new URLSearchParams(window.location.search);
      const params = Object.fromEntries(urlSearchParams.entries());
      return params[param]
    },
    async $boardApi(method, url, data, headers = {"Content-Type": "application/json"}) {
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
