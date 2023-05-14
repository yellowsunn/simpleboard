<template>
    <div>
        <div ref="googleButton" :style="googleStyle"></div>
    </div>
</template>

<script>
export default {
  name: "GoogleLogin",
  props: {
    height: String,
  },
  data() {
    return {
      googleStyle: {
        display: "flex",
        alignItems: "center",
        height: this.height,
        transform: `scale(${this.getHeightRatio()})`
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
    async handleCallback(response) {
      console.log(response)
    },
    getHeightRatio() {
      const heightNum = Number(this.height?.replace("px", ""))
      if (isNaN(heightNum)) {
        return 1
      }
      return heightNum / 40
    },
  },
}
</script>

<style scoped>

</style>
