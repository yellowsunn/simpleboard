<template>
    <div class="mx-auto" v-if="article" style="max-width: 950px; padding: 0 15px">
        <ArticleHeader :article="article"/>
        <section class="py-4">
            <div class="article-content" v-html="article?.body"></div>
        </section>
        <section class="like-container mb-4">
            <div class="border rounded py-2 px-3" style="cursor: pointer"
                 @click="isArticleLiked ? handleUndoLike() : handleLike()">
                <font-awesome-icon icon="fa-regular fa-thumbs-up" v-if="!isArticleLiked"/>
                <font-awesome-icon icon="fa-solid fa-thumbs-up" v-else/>
                <div style="user-select: none">좋아요</div>
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
      isArticleLiked: false,
    }
  },
  async mounted() {
    const article = await this.getArticle();
    this.article = article
    if (article) {
      const reaction = await this.getArticleReaction(article.articleId);
      this.isArticleLiked = reaction?.isArticleLiked || false
    }
  },
  methods: {
    async getArticle() {
      const {isError, data} = await this.$boardApi('GET', `/api/v2/articles/${this.id}`, null)
      if (isError) {
        alert('게시글을 조회할 수 없습니다.')
        return
      }
      return data
    },
    async getArticleReaction(articleId) {
      const {isError, data} = await this.$boardApi('GET', `/api/v2/articles/${articleId}/reaction`, null, true)
      if (isError) {
        return
      }
      return data
    },
    async handleLike() {
      const articleId = this.article?.articleId
      if (!articleId) {
        return
      }
      const {isError, data} = await this.$boardApi('PUT', `/api/v2/articles/${articleId}/like`, null, true);
      if (isError) {
        alert(data?.message)
        return
      }
      this.isArticleLiked = true
      this.article.likeCount += 1
    },
    async handleUndoLike() {
      const articleId = this.article?.articleId
      if (!articleId) {
        return
      }
      const {isError, data} = await this.$boardApi('DELETE', `/api/v2/articles/${articleId}/like`, null, true);
      if (isError) {
        alert(data?.message)
        return
      }
      this.isArticleLiked = false
      this.article.likeCount -= 1
    }
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
