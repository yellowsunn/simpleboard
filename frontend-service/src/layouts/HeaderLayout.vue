<template>
    <div class="header-layout">
        <section class="header-section mx-auto">
            <div class="header-list" v-if="isLogin">
                <div class="header-element noti-button">
                    <font-awesome-icon icon="fa-regular fa-bell"/>
                    <div class="ps-1">알림 없음</div>
                </div>
                <div class="header-element">마이페이지</div>
                <div class="header-element" @click="logoutClickEvent">로그아웃</div>
            </div>
            <div class="header-list" v-else>
                <div class="header-element" @click="loginClickEvent">로그인</div>
            </div>
        </section>
    </div>
</template>

<script>
export default {
  name: "HeaderLayout",
  methods: {
    loginClickEvent() {
      this.$router.push("/login")
    },
    logoutClickEvent() {
      this.$store.commit('deleteUserToken')
      window.location = "/";
    }
  },
  computed: {
    isLogin() {
      return this.$store.getters.isLogin
    }
  }
}
</script>

<style scoped>
.header-layout {
    border-bottom: 1px solid rgb(0 0 0 / 10%);
}

.header-section {
    display: flex;
    justify-content: flex-end;
    width: 950px;
}

.header-list {
    display: flex;
}

.header-element {
    padding: 10px;
    cursor: pointer;
}

.noti-button {
    display: flex;
    align-items: center;
}
</style>
