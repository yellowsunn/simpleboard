<template>
  <section>
    <i class="fas fa-home" @click="$router.push('/')"></i>
    <div class="register_container">
      <b-form class="form" @submit="onSubmit" :validated="false">
        <div class="title">
          <span>Register</span>
        </div>
        <div class="info">
          <b-input class="input_box" v-model="account.username" placeholder="user name" :required="true"></b-input>
          <b-input class="input_box" v-model="account.password" type="password" placeholder="password" :state="validPasswordSize" :required="true"></b-input>
          <div class="confirm_input_box">
            <b-input class="input_box" v-model="confirmPassword" type="password" placeholder="confirm password" :state="validPasswordSize && checkSamePassword" aria-describedby="input-live-feedback" :required="true"></b-input>
            <b-form-invalid-feedback id="input-live-feedback">
              {{ invalidateFeedback }}
            </b-form-invalid-feedback>
          </div>
        </div>
        <b-button class="btn btn-lg btn-block submit_btn" type="submit" variant="success">Create Account</b-button>
      </b-form>
      <div class="etc">
        <div class="login">Already have an account? <a href="/login">Login here</a></div>
        <div class="error_log">{{error.message}}</div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  data() {
    return {
      account: {
        username: "",
        password: ""
      },
      confirmPassword: "",
      error: {
        message: ""
      },
    }
  },
  computed: {
    checkSamePassword() {
      return this.account.password === this.confirmPassword;
    },
    validPasswordSize() {
      return this.account.password.length >= 8 && this.account.password.length <= 16;
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
        await this.$store.dispatch('FETCH_REGISTER', this.account);
        await this.$router.push('/');
      } catch (error) {
        this.error.message = error.data.message;
        this.account.username = '';
        this.account.password = '';
        this.confirmPassword = '';
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik&display=swap');

section {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: #f7f7f7;
  font-family: 'Rubik', sans-serif;
  font-size: 1.25rem;
  .fa-home {
    font-size: 3em;
    margin-bottom: 0.500em;
    cursor: pointer;
  }
  .register_container {
    display: flex;
    flex-direction: column;
    background: #fdfdfd;
    width: 25em;
    height: 26.875em;
    padding: 0 2em;
    border-radius: 0.625em;
    box-shadow: 0.0625em 0.0625em 0.625em 0.0625em #e2e2e2;
    .form {
      width: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      .title {
        font-size: 2.25em;
        text-align: center;
        color: #373c3f;
        margin: .622em;
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