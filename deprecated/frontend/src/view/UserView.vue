<template>
  <div>
    <Header></Header>
    <UserContent v-if="!forbidden"></UserContent>
    <div v-else>
      <Forbidden></Forbidden>
    </div>
  </div>
</template>

<script>
import Header from '@/components/layout/header/Header';
import UserContent from '@/components/layout/content/UserContent';
import Forbidden from '@/components/Forbidden';

export default {
  components: {
    Header,
    UserContent,
    Forbidden
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

<style scoped>

</style>