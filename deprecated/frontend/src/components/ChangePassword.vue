<template>
  <section>
    <div class="register_container">
      <b-form class="form" @submit="onSubmit" :validated="false">
        <div class="title">
          <span>비밀번호 변경</span>
        </div>
        <div class="info">
          <b-input class="input_box" v-model="password" type="password" placeholder="기존 비밀번호를 입력하세요" :required="true"></b-input>
          <b-input class="input_box" v-model="newPassword" type="password" placeholder="변경할 비밀번호를 입력하세요" :state="validPasswordSize" :required="true"></b-input>
          <div class="confirm_input_box">
            <b-input class="input_box" v-model="confirmNewPassword" type="password" placeholder="변경할 비밀번호를 다시 입력하세요" :state="validPasswordSize && checkSamePassword" aria-describedby="input-live-feedback" :required="true"></b-input>
            <b-form-invalid-feedback id="input-live-feedback">
              {{ invalidateFeedback }}
            </b-form-invalid-feedback>
          </div>
        </div>
        <b-button class="btn btn-lg btn-block submit_btn" type="submit" variant="success">Change Password</b-button>
      </b-form>
      <div class="etc">
        <div class="error_log">{{error.message}}</div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  data() {
    return {
      password: "",
      newPassword: "",
      confirmNewPassword: "",
      error: {
        message: ""
      },
    }
  },
  computed: {
    checkSamePassword() {
      return this.newPassword === this.confirmNewPassword;
    },
    validPasswordSize() {
      return this.newPassword.length >= 8 && this.newPassword.length <= 16;
    },
    invalidateFeedback() {
      if (!this.validPasswordSize) {
        return "password must be 8-16 characters";
      } 
      return !this.checkSamePassword ? "password mismatch" : "";
    }
  },
  methods: {
    async onSubmit(event) {
      event.preventDefault();
      if (!this.validPasswordSize || !this.checkSamePassword) return;

      try {
        await this.$store.dispatch('CHANGE_PASSWORD', { password: this.password, newPassword: this.newPassword });
        await alert("비밀번호가 변경되었습니다.");
        await this.$router.push('/users/myinfo');
      } catch (error) {
        console.log(error);
        this.error.message = error.data.message;
        this.password = '';
        this.newPassword = '';
        this.confirmNewPassword = '';
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik&display=swap');

section {
  width: 100vw;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: 'Rubik', sans-serif;
  font-size: 1rem;
  padding: 24px;
  .fa-home {
    font-size: 3em;
    margin-bottom: 0.500em;
    cursor: pointer;
  }
  .register_container {
    display: flex;
    flex-direction: column;
    background: #fdfdfd;
    width: 100%;
    height: 22.875em;
    padding: 0 2em;
    border-radius: 0.625em;
    box-shadow: 0.0625em 0.0625em 0.625em 0.0625em #e2e2e2;
    .form {
      width: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      .title {
        font-size: 1.2em;
        text-align: center;
        color: #373c3f;
        margin: 1em;
        display: flex;
        justify-content: center;
        align-items: center;
      }
      .info {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        .input_box {
          background: #e3e9e9;
          height: 2.6em;
          margin-bottom: 1.25em;
          font-size: 1em;
        }
        .confirm_input_box {
          height: 3.85em;
          margin-bottom: .45em;
          .input_box {
            margin-bottom: 0;
          }
        }
      }
      .submit_btn {
        margin-bottom: 1.25em;
        font-size: 1.125em;
        height: 3em;
      }
    }
    .etc {
      .login {
        color: #8e8e8e;
        font-size: 0.8em;
        margin-bottom: 0.6em;
      }
      .error_log {
        color: #b71c1c;
        font-size: 0.9em;
        font-style: italic;
      }
    }
  }
}

@media screen and (max-width: 48em) {
  section {
    font-size: 1rem;
  }
}

@media screen and (max-width: 30em) {
  section {
    font-size: 0.8rem;
  }
}
</style>