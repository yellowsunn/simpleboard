<template>
    <div class="signup_wrap mx-auto my-5">
        <h1>회원가입</h1>
        <div class="mb-3">고유한 이메일 및 닉네임으로 회원가입을 진행해주세요.</div>
        <div class="form-floating mb-2">
            <input type="email" v-model="email" class="form-control"
                   :class="[ isValidEmail ? 'is-valid' : (email && 'is-invalid')]" placeholder="name@example.com">
            <label>이메일</label>
            <div class="invalid-feedback">
                올바른 이메일 형식이 아닙니다.
            </div>
        </div>
        <div class="form-floating mb-2">
            <input type="text" v-model="nickName" class="form-control"
                   :class="[ isValidNickName ? 'is-valid': (nickName && 'is-invalid') ]" placeholder="닉네임">
            <label>닉네임</label>
            <div class="invalid-feedback">
                3~20자의 한글, 영어 소문자, 숫자 조합만 입력 가능합니다.
            </div>
        </div>
        <div class="form-floating mb-2">
            <input type="password" v-model="password" class="form-control"
                   :class="[ isValidPassword ? 'is-valid': (password && 'is-invalid') ]"
                   placeholder="password">
            <label>비밀번호</label>
            <div class="invalid-feedback">
                8~24자의 영문대소문자, 숫자, 특수문자만 가능합니다.
            </div>
        </div>
        <div class="form-floating mb-2">
            <input type="password" v-model="confirmPassword" class="form-control"
                   :class="[ isSamePassword ? (confirmPassword && 'is-valid'): 'is-invalid' ]" placeholder="password">
            <label>비밀번호 확인</label>
            <div class="invalid-feedback">
                비밀번호가 일치하지 않습니다.
            </div>
        </div>
        <button class="btn btn-primary btn-lg mb-1 w-100" @click="handleSignUp">회원가입</button>
        <div class="d-flex flex-row m-1">
            <div class="text-secondary" @click="$router.push('/login')" style="font-size: 0.9rem; cursor: pointer">이메일
                혹은 소셜 계정으로 로그인하기
            </div>
        </div>
    </div>
</template>

<script>
export default {
  name: "EmailSignUpView",
  data() {
    return {
      email: '',
      nickName: '',
      password: '',
      confirmPassword: '',
    }
  },
  methods: {
    async handleSignUp() {
      if (this.isValidEmail && this.isValidNickName && this.isValidPassword && this.isSamePassword) {
        const {isError, data} = await this.$boardApi('POST', '/api/v2/auth/email/signup', {
          email: this.email,
          password: this.password,
          nickName: this.nickName
        })
        if (isError) {
          alert(data.message)
          return
        }

        if (data === true) {
          this.$router.push('/')
        }
      }
    }
  },
  computed: {
    isValidEmail() {
      const regex = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/gm
      return this.email?.match(regex)
    },
    isValidPassword() {
      const regex = /^[a-zA-Z0-9~`!@#$%^&*()-+=]{8,24}$/gm
      return this.password?.match(regex)
    },
    isSamePassword() {
      return this.password === this.confirmPassword
    },
    isValidNickName() {
      const regex = /^[ㄱ-ㅎ가-힣a-z0-9]{3,20}$/gm
      return this.nickName?.match(regex)
    }
  }
}
</script>

<style scoped>
.signup_wrap {
    width: 360px;
}

.invalid-feedback {
    text-align: start;
    padding-left: 0.5rem;
}
</style>
