<template>
    <div class="mx-auto" style="max-width: 950px">
        <section class="d-flex">
            <div class="board-title" @click="$router.push('/')">유저게시판</div>
        </section>
        <div v-if="articles">
            <ArticleList :articles="articles"/>
            <CustomPagination class="m-3" :pageInfo="pageInfo" :page-size="5" @click-page="clickPage"></CustomPagination>
            <section>
                <button @click="$router.push('/articles/new')">글쓰기</button>
            </section>
        </div>
    </div>
</template>

<script>
import ArticleList from "@/components/article/ArticleList.vue";
import CustomPagination from "@/components/CustomPagination.vue";

export default {
  name: "ArticlesView",
  data() {
    return {
      articles: null,
      pageInfo: null,
    }
  },
  components: {CustomPagination: CustomPagination, ArticleList},
  async mounted() {
    const currentPage = this.$route.query?.page || 1
    const {isError, data} = await this.$boardApi('GET', `/api/v2/articles?page=${currentPage}`, null);
    if (isError) {
      alert('게시글 목록을 조회할 수 없습니다.')
      return
    }
    this.articles = data.articles
    this.pageInfo = {
      page: data.page,
      totalPages: data.totalPages,
    }
  },
  methods: {
    clickPage(page) {
      this.$router.push(`/articles?page=${page}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.board-title {
  font-size: 1.5rem;
  font-weight: 700;
  padding: 15px;
  cursor: pointer;
}
</style>
