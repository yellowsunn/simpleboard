<template>
    <div class="position-absolute top-50 start-50">
        <div class="spinner-border" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
    </div>
</template>

<script>
export default {
  name: "NaverUserLinkCallbackView",
  async created() {
    const regex = /access_token=([^&]+)/gm
    const found = window.location.hash?.match(regex);
    console.log(found)
    if (!found || found.length === 0) {
      alert('잘못된 요청입니다.')
      this.$router.push('/')
      return
    }
    const token = found[0].replace("access_token=", "")?.trim()
    const response = await this.$boardApi('PUT', '/api/oauth2/link', {
      token,
      type: "NAVER",
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
