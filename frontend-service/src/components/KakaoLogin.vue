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
      kakao.init('b9c163d3f9dab27b02c9010289851a21')
    }
  },
  methods: {
    loginWithKakao() {
      window.Kakao.Auth.authorize({
        redirectUri: "http://localhost:8080/login",
        scope: "account_email"
      })
    },
  }
}
</script>
<style scoped>

</style>
