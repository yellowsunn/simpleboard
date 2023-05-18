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
      callbackUrl: 'http://localhost:3000/login/naver',
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
