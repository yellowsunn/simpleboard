<template>
  <div class="admin ">
    <Header></Header>
    <AdminNavigationBar></AdminNavigationBar>
    <AdminContent v-if="isAdmin"></AdminContent>
    <div v-else>
      <Forbidden></Forbidden>
    </div>
  </div>
</template>

<script>
import Header from '@/components/layout/header/Header';
import AdminNavigationBar from '@/components/AdminNavigationBar';
import AdminContent from '@/components/layout/content/AdminContent';
import Forbidden from '@/components/Forbidden';

export default {
  components: {
    Forbidden,
    Header,
    AdminNavigationBar,
    AdminContent,
  },
  computed: {
    isAdmin() {
      return this.$store.state.userInfo.role === 'ADMIN';
    }
  },
  data() {
    return {
      forbidden: false
    }
  },
  created() {
    this.forbidden = this.$store.getters.forbidden;
  }
};
</script>

<style lang="scss" scoped>
.admin {
  width: 100%;
  min-height: 100vh;
  background-color: #eeeeee;
}
</style>