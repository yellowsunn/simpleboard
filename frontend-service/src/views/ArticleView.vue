<template>
    <div class="mx-auto" v-if="article" style="max-width: 950px; padding: 0 15px">
        <ArticleHeader :article="article"></ArticleHeader>
        <section class="py-4">
            <div class="article-content" v-html="article?.body"></div>
        </section>
        <section class="like-container mb-4">
            <div class="border rounded py-2 px-3" style="cursor: pointer">
                <font-awesome-icon icon="fa-regular fa-thumbs-up"/>
                <div>좋아요</div>
            </div>
        </section>
    </div>
</template>

<script>
import ArticleHeader from "@/components/article/ArticleHeader.vue";

export default {
  name: "ArticleView",
  components: {ArticleHeader},
  data() {
    return {
      id: this.$route.params.id,
      article: null,
    }
  },
  async mounted() {
    const {isError, data} = await this.$boardApi('GET', `/api/v2/articles/${this.id}`, null)
    if (isError) {
      alert('게시글을 조회할 수 없습니다.')
      return
    }
    this.article = data
  }
}
</script>

<style lang="scss" scoped>
.article-content {
  text-align: left;

  :deep(img) {
    max-width: 100%;
  }
}
.like-container {
    display: flex;
    justify-content: center;
    font-size: 1.125rem;
}
</style>
