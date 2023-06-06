<template>
  <div class="lnb">
      <ul> 
      <li id="info" :class="{'on': isMyInfoSelected }"><router-link to="/users/myinfo" style="cursor: pointer;">회원 정보</router-link></li>
      <li id="security" :class="{'on': !isMyInfoSelected }"><router-link to="/users" style="cursor: pointer;">사용자 조회</router-link></li>
      </ul>
  </div>
</template>

<script>
export default {
  props: {
    isMyInfoSelected: Boolean
  },
}
</script>

<style lang="scss" scoped>
.on {
  font-weight: bold;
}

.lnb {
  background-color: #ffff;
  padding: 0 30px 0 30px;
  height: 40px;
  border-bottom: 1px solid #e5e5e5;
  font-size: 14px;
  ul {
    display: flex;
    height: 100%;
    margin: 0;
    li {
      display: flex;
      align-items: center;
      &:not(:first-child) {
        margin-left: 40px;
      }
      a {
        color: #444;
      }
    }
  }
}
</style>

