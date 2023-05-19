<template>
    <div class="position-absolute top-50 start-50">
        <div class="spinner-border" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
    </div>
</template>

<script>

export default {
  name: "KakaoUserLinkCallbackView.vue",
  async created() {
    const regex = /code=([^&]+)/gm
    const found = window.location.search?.match(regex)

    if (!found || found.length === 0) {
      alert('잘못된 요청입니다.')
      this.$router.push('/')
      return
    }

    const code = found[0].replace('code=', "")?.trim()
    const {isError, data} = await this.$boardApi('PUT', '/api/v2/auth/oauth2/link', {
      token: code,
      type: "kakao",
    }, true);

    if (isError) {
      alert(data.message)
    }
    this.$router.push('/mypage')
  }
}
</script>

<style scoped>

</style>
