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
        <div style="userEntity-select: none">좋아요</div>
      </div>
    </section>
    <section class="button-group">
      <button class="btn btn-outline-secondary py-2" @click="$router.push('/articles')">목록</button>
      <button class="btn btn-outline-success py-2" v-if="userUUID === article?.userEntity?.uuid"
              @click="routeArticleEdit">수정
      </button>
      <button class="btn btn-outline-danger py-2" v-if="userUUID === article?.userEntity?.uuid"
              @click="handleRemoveArticle">삭제
      </button>
    </section>
    <section v-if="commentPage">
      <CommentList :commentPage="commentPage"></CommentList>
    </section>
  </div>
</template>

<script>
import ArticleHeader from "@/components/article/ArticleHeader.vue";
import CommentList from "@/components/comment/CommentList.vue";
import {computed} from "vue";

export default {
  name: "ArticleView",
  components: {CommentList, ArticleHeader},
  data() {
    return {
      id: this.$route.params.id,
      article: null,
      isArticleLiked: false,
      commentPage: null,
      likedCommentIds: null,
    }
  },
  computed: {
    userUUID() {
      return this.$store.state.userInfo?.uuid
    },
  },
  provide() {
    return {
      likedCommentIds: computed(() => this.likedCommentIds),
      writerUUID: computed(() => this.article?.userEntity?.uuid),
    }
  },
  async mounted() {
    const currentCommentPage = this.$route.query?.['comment-page'] || 1
    const [article, commentPage, reaction] = await Promise.all([this.getArticle(), this.getCommentPage(currentCommentPage), this.getArticleReaction()]);
    this.article = article
    this.commentPage = commentPage
    this.likedCommentIds = new Set(reaction?.likedCommentIds || [])
    this.isArticleLiked = reaction?.isArticleLiked || false
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
    async getCommentPage(page) {
      const {isError, data} = await this.$boardApi('GET', `/api/v2/articles/${this.id}/comments?page=${page}`, null);
      if (isError) {
        alert('댓글 목록을 조회할 수 없습니다.')
        return
      }
      return {
        ...data,
        comments: this.replaceDeletedComments(data)
      }
    },
    // 답글이 있는데 삭제된 댓글은 빈 객체 추가
    replaceDeletedComments(data) {
      const comments = data?.comments || []
      const newComments = []
      const baseIdSet = new Set()
      comments.reverse().forEach(it => {
        if (it.commentId === it.baseCommentId) {
          newComments.push(it)
          baseIdSet.add(it.baseCommentId)
        } else {
          // 마지막 페이지면 첫번째 원소에도 삭제 표시하기 위한 조건
          const isCondition = data.totalPages === data.page || newComments.length > 0
          if (isCondition && !baseIdSet.has(it.baseCommentId)) {
            newComments.push({})
            baseIdSet.add(it.baseCommentId)
          }
          newComments.push(it)
        }
      })
      return newComments.reverse()
    },
    async getArticleReaction() {
      const {isError, data} = await this.$boardApi('GET', `/api/v2/articles/${this.id}/reaction`, null, true)
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
    },
    async handleRemoveArticle() {
      const isConfirmed = confirm('게시글을 삭제하시겠습니까?')
      if (!isConfirmed) {
        return
      }

      const {isError, data} = await this.$boardApi('DELETE', `/api/v2/articles/${this.id}`, null, true)
      if (isError) {
        alert(data?.message)
        return
      }
      setTimeout(() => this.$router.push('/articles'), 300)
    },
    routeArticleEdit() {
      this.$router.push(`/articles/${this.id}/edit`)
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

.button-group {
  button {
    padding: 10px 30px;
    margin: 10px;
  }
}
</style>
