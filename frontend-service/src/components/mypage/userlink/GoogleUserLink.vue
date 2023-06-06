<template>
    <div class="btn_google border" :class="{ unlink_btn : isLinked }"
         @click="isLinked ? handleUserUnlink() : undefined">
        <div class="btn_google_logo">
            <GoogleLogo></GoogleLogo>
        </div>
        <div v-if="isLinked">구글 계정 연동 끊기</div>

        <div v-show="!isLinked">구글 계정 연동하기</div>
        <div ref="googleButton" :style="googleStyle" v-show="!isLinked"
             style="z-index: 1; position: absolute; opacity: 0"></div>
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
    const clientId = process.env.VUE_APP_GOOGLE_CLIENT_ID
    google.accounts.id.initialize({
      client_id: clientId,
      callback: this.handleUserLink,
      auto_select: 'false',
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
    async handleUserLink(response) {
      const {isError, data} = await this.$boardApi('PUT', '/api/v2/auth/oauth2/link', {
        token: response?.credential,
        type: "google",
      }, true)

      if (isError) {
        alert(data.message)
        return
      }

      if (data === true) {
        this.$store.commit('addUserProvider', 'GOOGLE')
      }
    },
    async handleUserUnlink() {
      const isConfirmed = confirm('소셜 계정 연동을 끊으시겠습니까?')
      if (!isConfirmed) {
        return
      }

      const {isError, data} = await this.$boardApi('DELETE', '/api/v2/auth/oauth2/link?type=google', null, true);
      if (isError) {
        alert(data?.message)
      }
      if (data === true) {
        this.$store.commit('deleteUserProvider', 'GOOGLE')
      }
    }
  },
}
</script>

<style scoped>
.btn_google {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 48px;
}

.btn_google_logo {
    display: flex;
    margin: 0.25rem;
    width: 24px;
    height: 100%;
}

.unlink_btn {
    cursor: pointer;
}
</style>
