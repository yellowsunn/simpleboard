<template>
  <section>
    <i class="fas" :class="{
      'fa-user-alt-slash': !isLoggedIn,
      'fa-user-alt': isLoggedIn && (userInfo.role === 'USER'),
      'fa-user-cog': isLoggedIn && (userInfo.role === 'ADMIN')
    }"></i>
    <span v-if="!isLoggedIn" class="title">로그인이 필요합니다</span>
    <div v-else class="title">
      <div>어서오세요! <span class="name">'{{ userInfo.username }}'</span>님</div>
      <div class="role">(권한: {{ userInfo.role }})</div>
    </div>
  </section>
</template>

<script>
export default {
  computed: {
    userInfo() {
      return this.$store.state.userInfo;
    },
    isLoggedIn() {
      return this.$store.state.isLoggedIn;
    }
  },
};
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap');

section {
  font-family: 'Noto Sans KR', sans-serif;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: calc(100vh - 66.97px);
  font-size: 2rem;
  .fas {
    font-size: 5.333em;
    margin-bottom: 0.125em;
  }
  .title {
    display: flex;
    flex-direction: column;
    align-items: center;
    .name {
      font-weight: bold;
    }
    .role {
      margin-top: 0.375em;
      font-size: 0.667em;
      color: #4e555b;
    }
  }
}

@media screen and (max-width: 48rem) {
  section {
    font-size: 1.8rem;
    height: calc(100vh - 58.67px);
  }
}

@media screen and (max-width: 30rem) {
  section {
    font-size: 1.6rem;
    height: calc(100vh - 55.19px);
  }
}
</style>