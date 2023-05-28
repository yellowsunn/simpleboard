<template>
    <div class="comments-title" id="comments">댓글</div>
    <div class="comments">
        <CommentItem :comment="comment"
                     :id="`comment-${comment.commentId}`"
                     :class="[comment.parentCommentId ? 'ms-5': '']"
                     v-for="(comment, idx) in [...commentPage.comments].reverse()" :key="idx"/>
        <CommentItem class="ms-5" id="comment2"></CommentItem>
    </div>
    <CommentWrite class="mt-3"></CommentWrite>
    <CommentPagination :pageInfo="pageInfo" class="m-3"></CommentPagination>
</template>

<script>
import CommentWrite from "@/components/comment/CommentWrite.vue";
import CommentItem from "@/components/comment/CommentItem.vue";
import CommentPagination from "@/components/comment/CommentPagination.vue";

export default {
  name: "CommentList",
  components: {CommentPagination, CommentItem, CommentWrite},
  props: {
    commentPage: Object,
  },
  computed: {
    pageInfo() {
      const {page, totalPages} = this.commentPage
      return {page, totalPages}
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

</style>
