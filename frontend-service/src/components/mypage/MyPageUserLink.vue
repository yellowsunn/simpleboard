<template>
    <div class="w-100">
        <div class="d-flex m-3 fs-5 fw-bold">계정 관리</div>
        <div class="d-flex flex-column">
            <GoogleUserLink class="mb-4 google-link" :isLinked="userProviders?.has('GOOGLE')"></GoogleUserLink>
            <NaverUserLink class="mb-4" :isLinked="userProviders?.has('NAVER')"></NaverUserLink>
            <KakaoUserLink :isLinked="userProviders?.has('KAKAO')"></KakaoUserLink>
        </div>
    </div>
</template>

<script>

import GoogleUserLink from "@/components/mypage/userlink/GoogleUserLink.vue";
import NaverUserLink from "@/components/mypage/userlink/NaverUserLink.vue";
import KakaoUserLink from "@/components/mypage/userlink/KakaoUserLink.vue";

export default {
  name: "MyPageUserLink",
  components: {KakaoUserLink, NaverUserLink, GoogleUserLink},
  props: {
    userProviders: Set
  },
}
</script>

<style scoped>
.google-link {
    margin-top: 3rem;
}
@media (max-width: 720px) {
    .google-link {
        margin-top: 0;
    }
}
</style>
