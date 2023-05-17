<template>
    <div class="d-flex justify-content-center align-items-center border" style="height: 48px">
        <div class="d-flex m-1 h-100" style="width: 24px;">
            <GoogleLogo></GoogleLogo>
        </div>
        <div v-if="isLinked">구글 계정 연동 끊기</div>
        <div v-else>구글 계정 연동하기</div>
        <div ref="googleButton" :style="googleStyle" style="z-index: 1; position: absolute; opacity: 0"></div>
    </div>
</template>

<script>
import GoogleLogo from "@/components/logo/GoogleLogo.vue";

export default {
  name: "GoogleUserLink",
  props: {
    isLinked: Boolean,
  },
  components: {GoogleLogo},
  data() {
    return {
      googleStyle: {
        display: "flex",
        alignItems: "center",
      },
    }
  },
  mounted() {
    const google = window.google
    const clientId = '260450438379-kc2vpk07h60qn6ojmb0t9u048dckm6a5.apps.googleusercontent.com'
    google.accounts.id.initialize({
      client_id: clientId,
      callback: this.handleCallback,
      auto_select: 'true',
    })

    google.accounts.id.renderButton(
      this.$refs.googleButton, {
        type: 'standard', //NOTE: 버튼 유형, standard,icon
        theme: 'outline', //NOTE: 테마 , outline,filled_blue,filled_black
        size: 'large', //NOTE: 버튼 크기 large,medium,small
        text: 'signin_with',//NOTE: 버튼 텍스트 , signin_with,wignup_with,continue_with,signIn
        shape: 'rectangular', //NOTE: 버튼 모양 rectangular,pill,circle,square
        logo_alignment: 'center',
        width: '360',
        prompt_parent_id: 'gggg',
      }
    )
  },
  methods: {
    async handleCallback(response) {
      const data = await this.$boardApi('POST', '/api/oauth2/login-signup', {
        state: this.$setSessionState(),
        token: response?.credential,
        type: "google",
      })
      if (data.isLogin === true) {
        this.$store.commit('setUserToken', {
          accessToken: data.accessToken,
          refreshToken: data.refreshToken,
        })
        this.$router.push('/')
      } else if (data.isLogin === false) {
        this.$router.push(`/oauth2/signup?token=${data.tempUserToken}`)
      }
    },
  },
}
</script>

<style scoped>

</style>
