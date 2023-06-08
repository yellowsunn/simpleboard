<template>
    <div @click="loginWithKakao" style="width: 3.125rem; cursor: pointer;">
        <KakaoLogo></KakaoLogo>
    </div>
</template>

<script>
import KakaoLogo from "@/components/logo/KakaoLogo.vue";

export default {
  name: "KakaoLogin",
  components: {KakaoLogo},
  props: {
    height: String,
  },
  mounted() {
    const kakao = window.Kakao
    if (!kakao.isInitialized()) {
      kakao.init(process.env.VUE_APP_KAKAO_CLIENT_ID)
    }
  },
  methods: {
    loginWithKakao() {
      window.Kakao.Auth.authorize({
        redirectUri: `${process.env.VUE_APP_FRONT_BASE_URL}/login/kakao`,
        scope: "account_email"
      })
    },
  }
}
</script>
<style scoped>

</style>
