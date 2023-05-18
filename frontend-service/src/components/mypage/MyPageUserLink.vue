<template>
    <div class="w-100">
        <div class="d-flex m-3 fs-5 fw-bold">계정 관리</div>
        <div class="d-flex flex-column">
            <GoogleUserLink class="mb-4 mt-5" :isLinked="userProviders?.has('GOOGLE')"></GoogleUserLink>
            <NaverUserLink class="mb-4" :isLinked="userProviders?.has('NAVER')"></NaverUserLink>
            <KakaoUserLink :isLinked="userProviders?.has('KAKAO')"></KakaoUserLink>
        </div>
    </div>
</template>

<script>

import GoogleUserLink from "@/components/GoogleUserLink.vue";
import NaverUserLink from "@/components/NaverUserLink.vue";
import KakaoUserLink from "@/components/KakaoUserLink.vue";

export default {
  name: "MyPageUserLink",
  components: {KakaoUserLink, NaverUserLink, GoogleUserLink},
  props: {
    userProviders: Set
  },
}
</script>

<style scoped>

</style>
