<template>
  <div class="home">
    <Header></Header>
    <HomeContent></HomeContent>
  </div>
</template>

<script>
import Header from '@/components/layout/header/Header';
import HomeContent from '@/components/layout/content/HomeContent';

export default {
  components: {
    Header,
    HomeContent
  }
};
</script>

<style scoped>
.home {
  width: 100%;
  height: 100%;
}
</style>