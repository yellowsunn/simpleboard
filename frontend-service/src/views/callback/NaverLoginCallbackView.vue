<template>
    <div class="position-absolute top-50 start-50">
        <div class="spinner-border" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
    </div>
</template>

<script>
export default {
  name: "NaverLoginCallbackView",
  async created() {
    const regex = /access_token=([^&]+)/gm
    const found = window.location.hash?.match(regex);

    if (!found || found.length === 0) {
      alert('잘못된 요청입니다.')
      this.$router.push('/')
      return
    }
    const token = found[0].replace("access_token=", "")?.trim()
    const {isError, data} = await this.$boardApi('POST', '/api/v2/auth/oauth2/login-signup', {
      state: this.$setSessionState(),
      token,
      type: "naver",
    })
    if (isError) {
      alert(data.message)
      this.$router.push('/')
      return
    }

    if (data.isLogin === true) {
      this.$store.commit('setUserToken', data)
      this.$router.push('/')
    } else if (data.isLogin === false) {
      this.$router.push(`/oauth2/signup?token=${data.tempUserToken}`)
    }
  }
}
</script>

<style scoped>

</style>
