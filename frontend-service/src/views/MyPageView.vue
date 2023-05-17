<template>
    <div class="mx-auto" style="width: 950px">
        <div class="mb-2">
            <MyPageHeader></MyPageHeader>
        </div>
        <div class="d-flex justify-content-between">
            <div class="ms-3" style="width: 360px">
                <MyPageUserInfo v-if="userInfo" :userInfo="userInfo"
                                @update-thumbnail="updateThumbnail"
                                @update-user-info="updateUserInfo"></MyPageUserInfo>
            </div>
            <div class="vr my-3"></div>
            <div class="me-3" style="width: 360px">
                <MyPageUserLink v-if="userProviders" :userProviders="userProviders"></MyPageUserLink>
            </div>
        </div>
        <div class="d-flex justify-content-end mt-5">
            <button type="button" class="btn btn-danger btn-lg">회원탈퇴</button>
        </div>
    </div>
</template>

<script>
import MyPageHeader from "@/components/mypage/MyPageHeader.vue";
import MyPageUserInfo from "@/components/mypage/MyPageUserInfo.vue";
import MyPageUserLink from "@/components/mypage/MyPageUserLink.vue";

export default {
  name: "MyPageView",
  components: {MyPageUserLink, MyPageUserInfo, MyPageHeader},
  async created() {
    const {email, nickName, thumbnail, providers} = await this.$boardApi('GET', '/api/users/my-info', null, true)
    this.userInfo = {
      email, nickName, thumbnail
    }
    this.userProviders = new Set(providers)
  },
  data() {
    return {
      userInfo: null,
      userProviders: null,
    }
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
    }
  }
}
</script>

<style scoped>

</style>
