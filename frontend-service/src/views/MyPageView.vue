<template>
  <div v-if="userInfo" class="mx-auto" style="max-width: 950px">
    <div class="d-flex justify-content-between user-info-content">
      <div class="mx-3" style="width: 360px">
        <MyPageUserInfo :userInfo="userInfo"
                        @update-thumbnail="updateThumbnail"
                        @update-user-info="updateUserInfo"></MyPageUserInfo>
      </div>
      <div class="vr my-3"></div>
      <div class="mx-3" style="width: 360px">
        <MyPageUserLink v-if="userProviders" :userProviders="userProviders"></MyPageUserLink>
      </div>
    </div>
    <div class="d-flex justify-content-end my-5 me-3">
      <button type="button" class="btn btn-danger btn-lg" @click="deleteUserInfo">회원탈퇴</button>
    </div>
  </div>
  <EditFinished></EditFinished>
</template>

<script>
import MyPageUserInfo from "@/components/mypage/MyPageUserInfo.vue";
import MyPageUserLink from "@/components/mypage/MyPageUserLink.vue";
import EditFinished from "@/components/EditFinshed.vue";

export default {
  name: "MyPageView",
  components: {EditFinished, MyPageUserLink, MyPageUserInfo},
  async mounted() {
    const {isError, data} = await this.$boardApi('GET', '/api/v2/users/me', undefined, true)
    if (isError) {
      alert(data.message)
      return
    }

    const {email, nickName, thumbnail, providers} = data
    this.userInfo = {
      email, nickName, thumbnail
    }
    this.$store.commit('setUserProviders', providers)
  },
  data() {
    return {
      userInfo: null,
    }
  },
  computed: {
    userProviders() {
      return this.$store.state.userProviders
    },
  },
  methods: {
    updateThumbnail(thumbnail) {
      this.userInfo = {
        ...this.userInfo,
        thumbnail,
      }
    },
    updateUserInfo({nickName}) {
      this.userInfo = {
        ...this.userInfo,
        nickName,
      }
    },
    async deleteUserInfo() {
      const isConfirmed = confirm('회원을 탈퇴하시겠습니까?')
      if (!isConfirmed) {
        return
      }

      const {isError, data} = await this.$boardApi('DELETE', '/api/v2/users/me', undefined, true)
      if (isError) {
        alert(data.message)
        return
      }

      if (data === true) {
        this.$store.commit('deleteUserToken')
        this.$router.push('/')
      }
    }
  }
}
</script>

<style scoped>
@media (max-width: 720px) {
  .user-info-content {
    align-items: center;
    flex-direction: column;
  }
}
</style>
