<template>
  <div class="comments-title" id="comments">댓글</div>
  <div class="comments">
    <CommentItem :comment="comment"
                 :id="`comment-${comment.commentId}`"
                 :class="[comment.parentCommentId ? 'ms-5': '', currentId == comment.commentId ? 'pointed' : '']"
                 v-for="(comment, idx) in [...commentPage.comments].reverse()" :key="idx"/>
    <CommentItem class="ms-5" id="comment2"></CommentItem>
  </div>
  <CommentWrite class="mt-3"></CommentWrite>
  <CustomPagination class="m-3" :pageInfo="pageInfo" :page-size="2" @click-page="clickPage"></CustomPagination>
</template>

<script>
import CommentWrite from "@/components/comment/CommentWrite.vue";
import CommentItem from "@/components/comment/CommentItem.vue";
import CustomPagination from "@/components/CustomPagination.vue";

export default {
  name: "CommentList",
  components: {CustomPagination, CommentItem, CommentWrite},
  props: {
    commentPage: Object,
  },
  data() {
    return {
      currentId: this.$route.hash?.replace('#comment-', '')
    }
  },
  computed: {
    pageInfo() {
      const {page, totalPages} = this.commentPage
      return {page, totalPages}
    }
  },
  methods: {
    clickPage(page) {
      const articleId = this.$route.params.id
      console.log(articleId, page)
      this.$router.replace(`/articles/${articleId}?comment-page=${page}#comments`)
    }
  }
}
</script>

<style lang="scss" scoped>
.comments-title {
  font-size: 1.1rem;
  font-weight: 700;
  display: flex;
}

.pointed {
  background-color: rgb(0 0 0 / 5%);
}
</style>
