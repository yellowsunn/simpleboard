<template>
    <div class="login_wrap mx-auto my-5">
        <h1>로그인</h1>
        <div class="mb-3">이메일 혹은 소셜 계정으로 로그인을 진행해주세요.</div>
        <div class="form-floating mb-2">
            <input type="email" v-model="email" class="form-control" placeholder="name@example.com">
            <label>이메일</label>
        </div>
        <div class="form-floating mb-2">
            <input type="password" v-model="password" class="form-control" placeholder="password">
            <label>비밀번호</label>
        </div>
        <button type="button" class="btn btn-primary btn-lg mb-1 w-100" @click="handleLogin">로그인</button>
        <div class="d-flex flex-row m-1">
            <div class="text-secondary" @click="$router.push('/email/signup')"
                 style="font-size: 0.9rem; cursor: pointer">이메일로 회원가입하기
            </div>
        </div>
        <div class="social_login_tit py-3">
            <div class="tit">소셜 계정으로 간편 로그인</div>
        </div>
        <div style="display: flex; justify-content: space-around; align-items: center;">
            <NaverLogin></NaverLogin>
            <GoogleLogin></GoogleLogin>
            <KakaoLogin height="50px"></KakaoLogin>
        </div>
    </div>
</template>

<script>
import NaverLogin from "@/components/oauth2-login/NaverLogin.vue";
import GoogleLogin from "@/components/oauth2-login/GoogleLogin.vue";
import KakaoLogin from "@/components/oauth2-login/KakaoLogin.vue";

export default {
  name: "LoginView",
  components: {KakaoLogin, GoogleLogin, NaverLogin},
  data() {
    return {
      email: '',
      password: '',
    }
  },
  methods: {
    async handleLogin() {
      if (!this.email || !this.password) {
        return
      }
      const {isError, data} = await this.$boardApi('POST', '/api/v2/auth/email/login', {
        email: this.email,
        password: this.password,
      })
      if (isError) {
        alert(data.message)
        return
      }

      this.$store.commit('setUserToken', data)
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.login_wrap {
    width: 360px;
}

.tit {
    font-size: 15px;
    color: #5c667b;
    letter-spacing: -1px;
}

.tit:before, .tit:after {
    width: 60px;
    height: 1px;
    border: 1px;
    background-color: #eaedf4;
    display: inline-block;
    vertical-align: super;
    content: '';
}

.tit:before {
    margin-right: 8px;
}

.tit:after {
    margin-left: 8px;
}

.signup {
    display: flex;
    justify-items: flex-start;
}
</style>
