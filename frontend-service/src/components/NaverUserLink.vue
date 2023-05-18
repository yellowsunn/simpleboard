<template>
    <div class="btn_naver border" @click="isLinked ? handleUserUnlink() : handleUserLink()">
        <div class="d-flex">
            <img src="../assets/btn_naver.png" width="40"/>
        </div>
        <div v-if="isLinked" class="btn_naver_text">네이버 계정 연동 끊기</div>
        <div v-else class="btn_naver_text">네이버 계정 연동하기</div>
        <div id="naverIdLogin" ref="naverIdLogin" v-show="false"></div>
    </div>
</template>

<script>
export default {
  name: "NaverUserLink",
  props: {
    isLinked: Set,
  },
  mounted() {
    this.naverLogin = new window.naver.LoginWithNaverId({
      clientId: process.env.VUE_APP_NAVER_CLIENT_ID,
      callbackUrl: `${process.env.VUE_APP_FRONT_BASE_URL}/mypage/naver/link`,
      isPopup: false,
      loginButton: {color: 'green', type: 1, height: 50}
    })
    this.naverLogin.init()
  },
  methods: {
    handleUserLink() {
      this.$refs.naverIdLogin.firstChild.click()
    },
    async handleUserUnlink() {
      const isConfirmed = confirm('소셜 계졍 연동을 끊으시겠습니까?')
      if (!isConfirmed) {
        return
      }

      const data = await this.$boardApi('DELETE', '/api/oauth2/link?type=naver', null, true);
      if (data?.code) {
        alert(data.message)
      }
      if (data === true) {
        this.$store.commit('deleteUserProvider', 'NAVER')
      }
    }
  }
}
</script>

<style scoped>
.btn_naver {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 48px;
    background-color: #03C75A;
    cursor: pointer;
}

.btn_naver_text {
    color: #ffffff;
}
</style>
