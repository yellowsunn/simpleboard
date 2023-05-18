<template>
  <div class="my-page-header">
      <a @click="$router.push('/')">회원 정보 변경</a>
      <a>내가 쓴 글</a>
      <a>내가 쓴 댓글</a>
      <a>좋아요 한 글</a>
      <a>좋아요 한 댓글</a>
  </div>
  <div class=""></div>
</template>

<script>
export default {
  name: "MyPageHeader"
}
</script>

<style scoped>
.my-page-header {
    grid-template-columns: repeat(4,1fr);
    display: grid;
}
.my-page-header > a {
    border: 1px solid rgba(0 0 0 / 10%);
    cursor: pointer;
    padding: 10px;
    margin: 1px;
    text-decoration: none;
}
</style>
