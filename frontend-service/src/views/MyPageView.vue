<template>
    <div v-if="userInfo" class="mx-auto" style="width: 950px">
        <div class="mb-2">
            <MyPageHeader></MyPageHeader>
        </div>
        <div class="d-flex justify-content-between">
            <div class="ms-3" style="width: 360px">
                <MyPageUserInfo :userInfo="userInfo"
                                @update-thumbnail="updateThumbnail"
                                @update-user-info="updateUserInfo"></MyPageUserInfo>
            </div>
            <div class="vr my-3"></div>
            <div class="me-3" style="width: 360px">
                <MyPageUserLink v-if="userProviders" :userProviders="userProviders"></MyPageUserLink>
            </div>
        </div>
        <div class="d-flex justify-content-end mt-5">
            <button type="button" class="btn btn-danger btn-lg" @click="deleteUserInfo">회원탈퇴</button>
        </div>
    </div>
    <EditFinished></EditFinished>
</template>

<script>
import MyPageHeader from "@/components/mypage/MyPageHeader.vue";
import MyPageUserInfo from "@/components/mypage/MyPageUserInfo.vue";
import MyPageUserLink from "@/components/mypage/MyPageUserLink.vue";
import EditFinished from "@/components/EditFinshed.vue";

export default {
  name: "MyPageView",
  components: {EditFinished, MyPageUserLink, MyPageUserInfo, MyPageHeader},
  async mounted() {
    const {isError, data} = await this.$boardApi('GET', '/api/v2/users/my-info', null, true)
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

      const {isError, data} = await this.$boardApi('DELETE', '/api/v2/users/my-info', undefined, true)
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

</style>
