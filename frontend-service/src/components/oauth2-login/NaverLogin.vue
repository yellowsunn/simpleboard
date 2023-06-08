<template>
    <div>
        <div id="naverIdLogin" ref="naverIdLogin" v-show="false"></div>
        <img src="../../assets/btn_naver.png" class="btn_naver" alt="btn_naver" @click="clickEvent"/>
    </div>
</template>

<script>
export default {
  name: "NaverLogin",
  data() {
  },
  mounted() {
    this.naverLogin = new window.naver.LoginWithNaverId({
      clientId: process.env.VUE_APP_NAVER_CLIENT_ID,
      callbackUrl: `${process.env.VUE_APP_FRONT_BASE_URL}/login/naver`,
      isPopup: false,
      loginButton: {color: 'green', type: 1, height: 50}
    })
    this.naverLogin.init()
  },
  methods: {
    clickEvent() {
      this.$refs.naverIdLogin.firstChild.click()
    }
  }
}
</script>

<style scoped>
.btn_naver {
    cursor: pointer;
    height: 3.125rem;
}
</style>
