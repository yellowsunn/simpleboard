<template>
  <section>
    <i class="fas fa-home" @click="$router.push('/')"></i>
    <div class="login_container">
      <b-form class="form" @submit="onSubmit">
        <div class="title">
          <span>Log in</span>
        </div>
        <div class="info">
          <b-input class="input_box" v-model="account.username" placeholder="username" :required="true"></b-input>
          <b-input class="input_box" v-model="account.password" type="password" placeholder="password"
                   :required="true"></b-input>
        </div>
        <b-button class="btn btn-lg btn-block submit_btn" type="submit" variant="success">Log in</b-button>
      </b-form>
      <div class="etc">
        <div class="register">Don't have an account? <a href="/register">Register now</a></div>
        <div class="error_log">{{ error.message }}</div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  data() {
    return {
      account: {
        username: '',
        password: '',
      },
      error: {
        message: '',
      }
    };
  },
  methods: {
    async onSubmit(event) {
      event.preventDefault();
      try {
        await this.$store.dispatch(
            'FETCH_LOGIN',
            { account: this.account },
        );
        const nextUrl = this.$route.query.nextUrl || '/';
        await this.$router.push(nextUrl);
      } catch (error) {
        this.error.message = error.data.message;
        this.account.username = '';
        this.account.password = '';
        console.log(error.data);
      }
    }
  },
};
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik&display=swap');

label {
  margin: 0;
}

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
  .login_container {
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
          height: 3em;
          margin-bottom: 1.25em;
          font-size: 1em;
        }
        .check_box {
          display: flex;
          align-items: center;
          margin-bottom: 1.25em;
          font-size: 0.8em;
          color: #4e555b;
          text-align: center;
          #checkbox {
            zoom: 1.5;
            margin-right: 0.5em;
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
      .register {
        color: #8e8e8e;
        font-size: 0.8em;
        margin-bottom: 0.6em;
      }
      .error_log {
        color: #b71c1c;
        margin-left: 0.25em;
        font-style: italic;
      }
    }
  }
}

@media screen and (max-width: 48em) {
  section {
    font-size: 1rem;
    .login_container .form .info .check_box #checkbox {
      zoom: 1.2;
    }
  }
}

@media screen and (max-width: 30em) {
  section {
    font-size: 0.8rem;
    .login_container .form .info .check_box #checkbox {
      zoom: 0.96;
    }
  }
}
</style>