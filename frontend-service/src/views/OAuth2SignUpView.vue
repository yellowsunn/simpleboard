<template>
    <div class="oauth-signup-wrap mx-auto my-5">
        <h1>추가 회원 정보 입력</h1>
        <div class="mb-3">회원가입을 완료하기 위해 추가 정보를 입력해주세요.</div>
        <div class="form-floating mb-2">
            <input type="text" v-model="nickName" class="form-control" placeholder="nickName">
            <label>닉네임</label>
        </div>
        <button type="button" class="btn btn-primary btn-lg mb-1 w-100" @click="completeClickEvent">완료</button>
        <div class="d-flex flex-row my-2 mx-1">
            <div class="text-secondary" @click="cancelClickEvent" style="font-size: 0.9rem; cursor: pointer">취소하고 돌아가기
            </div>
        </div>
    </div>
</template>

<script>
export default {
  name: "OAuth2SignUpView",
  data() {
    return {
      nickName: '',
    }
  },
  methods: {
    async completeClickEvent() {
      const state = this.$getSessionState()
      const response = await this.$boardApi("POST", "/api/oauth2/signup", {
        state,
        tempUserToken: this.$getRequestParam("token"),
        nickName: this.nickName?.slice(0, 100),
      })
      if (response.code) {
        this.handleSignUpError(response.code, response.message)
        return
      }
      this.$setLoginToken(response)
      this.$router.push("/")
    },
    cancelClickEvent() {
      this.$router.push('/login')
    },
    handleSignUpError(code, message) {
      if (code === 'INVALID_TEMP_USER') {
        alert('기존 정보를 불러올 수 없습니다. 다시 시도해주세요.')
        this.$router.push("/login")
      } else {
        alert(message)
      }
    },
  },
  watch: {
    nickName: function (val) {
      if (val?.length > 20) {
        this.nickName = val.slice(0, 20)
      }
    }
  }
}
</script>

<style scoped>
.oauth-signup-wrap {
    width: 360px;
}
</style>
