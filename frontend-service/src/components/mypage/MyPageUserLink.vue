<template>
    <div class="w-100">
        <div class="d-flex m-3 fs-5 fw-bold">계정 관리</div>
        <GoogleUserLink :isLinked="userProviders?.has('GOOGLE')"></GoogleUserLink>
        <NaverUserLink :isLinked="userProviders?.has('NAVER')"></NaverUserLink>
    </div>
</template>

<script>

import GoogleUserLink from "@/components/GoogleUserLink.vue";
import NaverUserLink from "@/components/NaverUserLink.vue";

export default {
  name: "MyPageUserLink",
  components: {NaverUserLink, GoogleUserLink},
  props: {
    userProviders: Set
  },
}
</script>

<style scoped>

</style>
