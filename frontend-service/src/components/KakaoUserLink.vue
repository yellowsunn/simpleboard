<template>
    <div class="btn_kakao border" @click="isLinked ? handleUserUnlink() : handleUserLink()">
        <KakaoLogo class="btn_kakao_logo"></KakaoLogo>
        <div v-if="isLinked">카카오 계정 연동 끊기</div>
        <div v-else>카카오 계정 연동하기</div>
    </div>
</template>

<script>
import KakaoLogo from "@/components/logo/KakaoLogo.vue";

export default {
  name: "KakaoUserLink",
  props: {
    isLinked: Boolean,
  },
  mounted() {
    const kakao = window.Kakao
    if (!kakao.isInitialized()) {
      kakao.init(process.env.VUE_APP_KAKAO_CLIENT_ID)
    }
  },
  components: {KakaoLogo},
  methods: {
    async handleUserLink() {
      window.Kakao.Auth.authorize({
        redirectUri: `${process.env.VUE_APP_FRONT_BASE_URL}/mypage/kakao/link`,
        scope: "account_email"
      })
    },
    async handleUserUnlink() {
      const isConfirmed = confirm('소셜 계졍 연동을 끊으시겠습니까?')
      if (!isConfirmed) {
        return
      }

      const {isError, data} = await this.$boardApi('DELETE', '/api/v2/auth/oauth2/link?type=kakao', null, true);
      if (isError) {
        alert(data.message)
        return
      }
      if (data === true) {
        this.$store.commit('deleteUserProvider', 'KAKAO')
      }
    }
  }
}
</script>

<style scoped>
.btn_kakao {
    background-color: #fee500;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 48px;
    cursor: pointer;
}

.btn_kakao_logo {
    height: 40px;
}
</style>
