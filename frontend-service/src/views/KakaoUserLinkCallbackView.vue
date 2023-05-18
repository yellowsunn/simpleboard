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
      this.$rotuer.push('/')
      return
    }
    const code = found[0].replace('code=', "")?.trim()
    const response = await this.$boardApi('PUT', '/api/oauth2/link', {
      token: code,
      type: "kakao",
    }, true);

    // 실패하는 경우 알럿창 띄움
    if (response?.code) {
      alert(response?.message)
    }
    this.$router.push('/mypage')
  }
}
</script>

<style scoped>

</style>
