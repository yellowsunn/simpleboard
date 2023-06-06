<template>
  <header>
    <div class="logoAndToggleBtn">
      <div class="toggleBtn">
        <i class="fas fa-bars" @click="toggle = !toggle"></i>
      </div>
      <router-link  class="logo" tag="div" to="/">
        <div class="image">
          <img src="@/assets/springsecurity.png">
        </div>
        <span class="title">Forum</span>
      </router-link>
    </div>
    <VueSlideToggle class="slide_toggle" v-if="isMobile" :open="toggle">
      <Menu></Menu>
    </VueSlideToggle>
    <Menu v-else></Menu>
    <div class="user_info">
      <router-link v-if="!isLoggedIn" to="/login" class="login">Login</router-link>
      <router-link v-if="!isLoggedIn" to="/register" class="register">Register</router-link>
      <a v-if="isLoggedIn" href="/logout" class="logout">Sign out</a>
    </div>
  </header>
</template>

<script>
import Menu from '@/components/layout/header/children/Menu';
import { VueSlideToggle } from 'vue-slide-toggle';


export default {
  components: {
    Menu,
    VueSlideToggle
  },
  data() {
    const mql = window.matchMedia("screen and (max-width: 48rem)");
    return {
      mql,
      isMobile: mql.matches,
      toggle: false,
    }
  },
  mounted() {
    this.mql.addEventListener('change', e => {
      this.isMobile = e.matches;
      if (e.matches === false) {
        this.toggle = false;
      }
    })
  },
  computed: {
    isLoggedIn() {
      return this.$store.state.isLoggedIn;
    }
  },
};
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap');

$header-color: #282c35;
$button-color: #1a72e6;
$button-hover-color: #4383ee;
$white-button-hover-color: #e3ebff;

a {
  color: white;
  text-decoration: none;
}

ul {
  margin: 0;
}

.slide_toggle {
  width: 100%;
}

header {
  font-family: 'Noto Sans KR', sans-serif;
  display: flex;
  background: $header-color;
  justify-content: space-between;
  align-items: center;
  padding: 0.6em 1em;
  color: white;
  .logoAndToggleBtn {
    flex: 1 1 20%;
    font-size: 1.8rem;
    display: flex;
    .toggleBtn {
      display: none;
      margin-right: 1rem;
      .fa-bars {
        cursor: pointer;
        &:hover {
          color: #bcbcbc;
        }
      }
    }
    .logo {
      display: flex;
      align-items: center;
      justify-content: flex-start;
      cursor: pointer;
      img {
        width: 1.5em;
      }
      .title {
        color: #65ba40;
      }
    }
  }
  .menu {
    flex: 1 1 60%;
    display: flex;
    justify-content: space-around;
    margin-bottom: 0;
  }
  .user_info {
    flex: 1 1 20%;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    .login {
      background-color: $button-color;
      color: white;
      padding: 0.563em 1.5em;
      border-radius: .25em;
      margin-right: 1em;
      &:hover {
        background-color: $button-hover-color;
      }
    }
    .register {
      background-color: white;
      padding: 0.542em 0.938em;
      color: $button-color;
      border: .0625em solid $button-color;
      border-radius: .25em;
      &:hover {
        background-color: $white-button-hover-color;
      }
    }
    .logout {
      background-color: $button-color;
      color: white;
      padding: 0.563em 0.938em;
      border-radius: .25em;
      &:hover {
        background-color: $button-hover-color;
      }
    }
  }
}

@media screen and (max-width: 48rem) {
  header {
    flex-direction: column;
    .logoAndToggleBtn {
      font-size: 1.5rem;
      align-self: flex-start;
      .toggleBtn {
        display: block;
      }
    }
    .menu {
      flex-direction: column;
      align-items: center;
    }
    .user_info {
      position: absolute;
      right: 0.875rem;
      .login, .register, .logout {
        font-size: 0.9rem;
      }
    }
  }
}

@media screen and (max-width: 30rem){
  header {
    .logoAndToggleBtn {
      .logo {
        font-size: .75em;
      }
      .toggleBtn {
        margin-right: .75rem;
      }
    }
    .user_info {
      top: 0.688em;
      .login, .register, .logout {
        font-size: 0.75em;
      }
    }
  }
}
</style>