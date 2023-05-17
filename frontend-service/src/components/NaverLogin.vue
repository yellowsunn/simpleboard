<template>
    <div>
        <div id="naverIdLogin" ref="naverIdLogin" v-show="false"></div>
        <img src="../assets/btn_naver.png" class="btn_naver" alt="btn_naver" @click="clickEvent"/>
    </div>
</template>

<script>
export default {
  name: "NaverLogin",
  data() {
  },
  mounted() {
    this.naverLogin = new window.naver.LoginWithNaverId({
      clientId: 'zJUlDFqIoVQ0qNY3rJW9',
      callbackUrl: 'http://localhost:8080/login',
      isPopup: false,
      loginButton: {color: 'green', type: 1, height: 50}
    })
    this.naverLogin.init()

    this.naverLogin.getLoginStatus(status => {
      if (status) {
        console.log(status)
        console.log(this.naverLogin.user)
      } else {
        console.log("callback 처리에 실패하였습니다.")
      }
    })
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
