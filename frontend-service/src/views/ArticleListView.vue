<template>
    <div class="mx-auto" style="max-width: 950px" v-if="articles">
        <section class="d-flex">
            <div style="font-size: 1.5rem; font-weight: 700; padding: 15px">유저게시판</div>
        </section>
        <ArticleList :articles="articles"></ArticleList>
        <section>
            <button @click="$router.push('/articles/new')">글쓰기</button>
        </section>
    </div>
</template>

<script>
import ArticleList from "@/components/article/ArticleList.vue";

export default {
  name: "ArticlesView",
  data() {
    return {
      articles: null
    }
  },
  components: {ArticleList},
  async mounted() {
    const {isError, data} = await this.$boardApi('GET', '/api/v2/articles', null);
    if (isError) {
      alert('게시글 목록을 조회할 수 없습니다.')
      return
    }
    this.articles = data.articles
  }
}
</script>

<style lang="scss" scoped>

</style>
