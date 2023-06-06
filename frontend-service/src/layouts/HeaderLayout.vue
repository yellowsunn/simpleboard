<template>
  <div class="header-layout mx-auto">
    <div class="px-3" style="font-weight: bold; cursor: pointer" @click="$router.push('/')">HOME</div>
    <section class="header-section mx-auto">
      <div class="header-list" v-if="isLogin">
        <div class="header-element noti-button" @click="routeNotificationPage">
          <font-awesome-icon v-if="hasAlarm" icon="fa-solid fa-bell" shake/>
          <font-awesome-icon v-else icon="fa-regular fa-bell"/>
          <div class="ps-1" :class="[hasAlarm && 'has-alarm']">{{ hasAlarm ? '새 알림' : '알림 없음' }}</div>
        </div>
        <div class="header-element" @click="$router.push('/mypage')">마이페이지</div>
        <div class="header-element" @click="logoutClickEvent">로그아웃</div>
      </div>
      <div class="header-list" v-else>
        <div class="header-element" @click="$router.push('/login')">로그인</div>
      </div>
    </section>
  </div>
</template>

<script>

export default {
  name: "HeaderLayout",
  data() {
    return {
      hasAlarm: false,
    }
  },
  methods: {
    logoutClickEvent() {
      const isConfirmed = confirm('로그아웃 하시겠습니까?');
      if (!isConfirmed) {
        return true
      }
      this.$store.commit('deleteUserToken')
      this.$router.push('/')
    },
    async existUnreadNotifications() {
      const {isError, data} = await this.$boardApi('GET', '/api/v1/notifications/me/unread', undefined, true)
      if (isError) {
        return false
      }
      return data
    },
    routeNotificationPage() {
      this.hasAlarm = false
      this.$router.push('/notifications')
    }
  },
  computed: {
    isLogin() {
      return this.$store.getters.isLogin
    }
  },
  async mounted() {
    this.hasAlarm = await this.existUnreadNotifications()
    setInterval(async () => {
      this.hasAlarm = await this.existUnreadNotifications()
    }, 30 * 1_000)
  },
}
</script>

<style scoped>
.header-layout {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid rgb(0 0 0 / 10%);
  max-width: 950px;
}

.header-section {
  display: flex;
  width: 100%;
  justify-content: flex-end;
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

.has-alarm {
  font-weight: bold;
}
</style>
