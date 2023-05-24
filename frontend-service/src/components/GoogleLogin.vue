<template>
    <div class="d-flex justify-content-center">
        <div class="border rounded-circle" style="width: 3.125rem; padding: 0.5rem">
            <GoogleLogo></GoogleLogo>
        </div>
        <div ref="googleButton" class="btn_google"></div>
    </div>
</template>

<script>
import GoogleLogo from "@/components/logo/GoogleLogo.vue";

export default {
  name: "GoogleLogin",
  components: {GoogleLogo},
  data() {
    return {}
  },
  mounted() {
    const google = window.google
    const clientId = process.env.VUE_APP_GOOGLE_CLIENT_ID
    google.accounts.id.initialize({
      client_id: clientId,
      callback: this.handleCallback,
      auto_select: 'false',
    })

    google.accounts.id.renderButton(
      this.$refs.googleButton, {
        type: 'icon', //NOTE: 버튼 유형, standard,icon
        theme: 'outline', //NOTE: 테마 , outline,filled_blue,filled_black
        size: 'large', //NOTE: 버튼 크기 large,medium,small
        text: 'signin_with',//NOTE: 버튼 텍스트 , signin_with,wignup_with,continue_with,signIn
        shape: 'circle', //NOTE: 버튼 모양 rectangular,pill,circle,square
        logo_alignment: 'center',
      }
    )
  },
  methods: {
    async handleCallback(callbackData) {
      const {isError, data} = await this.$boardApi('POST', '/api/v2/auth/oauth2/login-signup', {
        state: this.$setSessionState(),
        token: callbackData?.credential,
        type: "google",
      })
      if (isError) {
        alert(data.message)
        return
      }

      if (data.isLogin === true) {
        this.$store.commit('setUserToken', data)
        this.$router.push('/')
      } else if (data.isLogin === false) {
        this.$router.push(`/oauth2/signup?token=${data.tempUserToken}`)
      }
    },
  },
}
</script>

<style scoped>
.btn_google {
    position: absolute;
    z-index: 1;
    opacity: 0;
    display: flex;
    align-items: center;
    height: 50px;
    transform: scale(1.25);
}
</style>
