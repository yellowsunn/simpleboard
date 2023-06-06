<template>
  <div class="myinfo">
    <Header></Header>
    <AdminNavigationBar :isMyInfoSelected='true'></AdminNavigationBar>
    <MyInfoContent v-if="this.$route.path === '/users/myinfo'"></MyInfoContent>
    <ChangePassword v-else></ChangePassword>
  </div>
</template>

<script>
import Header from '@/components/layout/header/Header';
import MyInfoContent from '@/components/layout/content/MyInfoContent';
import ChangePassword from '@/components/ChangePassword';
import AdminNavigationBar from '@/components/AdminNavigationBar';

export default {
  components: {
    Header,
    MyInfoContent,
    ChangePassword,
    AdminNavigationBar
  },
}
</script>

<style lang="scss" scoped>
.myinfo {  
  width: 100%;
  min-height: 100vh;
  background-color: #eeeeee;
}
</style>