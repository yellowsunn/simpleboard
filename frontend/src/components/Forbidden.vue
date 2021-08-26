<template>
  <section>
    <i class="fas fa-ban">
    </i>
    <div>권한이 없습니다.</div>
  </section>
</template>

<script>
export default {
  name: 'Forbidden',
};
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap');

section {
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 2rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 66.97px);
  i {
    font-size: 6em;
    margin-bottom: 0.125em;
  }
}

@media screen and (max-width: 48rem) {
  section {
    font-size: 1.8rem;
    height: calc(100vh - 58.67px);
  }
}

@media screen and (max-width: 30rem) {
  section {
    font-size: 1.6rem;
    height: calc(100vh - 55.19px);
  }
}
</style>