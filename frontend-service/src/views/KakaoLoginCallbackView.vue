<template>
    <div class="position-absolute top-50 start-50">
        <div class="spinner-border" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
    </div>
</template>

<script>
export default {
  name: "KaKaoLoginCallbackView",
  async created() {
    const regex = /code=([^&]+)/gm
    const found = window.location.search?.match(regex)

    if (!found || found.length === 0) {
      alert('잘못된 요청입니다.')
      this.$rotuer.push('/')
      return
    }
    const code = found[0].replace('code=', "")?.trim()
    const data = await this.$boardApi('POST', '/api/oauth2/login-signup', {
      state: this.$setSessionState(),
      token: code,
      type: "kakao",
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
  }
}
</script>

<style scoped>

</style>
