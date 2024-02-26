<template>
  <div class="comment" v-if="comment && !isDeleted">
    <div class="main d-flex" v-if="!isEmptyObject(comment)">
      <img v-if="comment.userThumbnail" class="rounded-circle" :src="comment.userThumbnail" alt="thumbnail"
           referrerpolicy="no-referrer-when-downgrade"/>
      <img v-else class="rounded-circle" src="@/assets/default-thumbnail.svg" alt="thumbnail"/>
      <div class="comment-wrapper">
        <div class="info d-flex justify-content-between">
          <div class="left">
            <div class="nick-name d-flex">
              <div class="text">{{ comment.nickName }}</div>
              <div class="author border border-danger rounded" v-if="writerUUID === comment.userUUID">
                작성자
              </div>
            </div>
            <div class="d-flex">
              <div class="saved-at" v-if="comment?.savedAt">{{ timeAgoFormat(comment.savedAt) }}</div>
              <div class="dot"></div>
              <div class="like-count" @click="isCommentLiked ? handleUndoLike() : handleLike()">
                <font-awesome-icon icon="fa-solid fa-thumbs-up" v-if="isCommentLiked"/>
                <font-awesome-icon icon="fa-regular fa-thumbs-up" v-else/>
                {{ commentLikeCount }}
              </div>
            </div>
          </div>
          <div class="right d-flex justify-content-between">
            <div class="comment-reply p-1" @click="clickReply">{{ replyToggle ? '닫기' : '댓글' }}</div>
            <div class="comment-delete p-1" v-if="userUUID === comment.userUUID" @click="deleteComment">삭제
            </div>
          </div>
        </div>
        <div class="comment-content">
          <div v-if="comment.imageUrl">
            <img :src="comment.imageUrl">
          </div>
          <div>{{ comment.content }}</div>
        </div>
        <CommentWrite :parentCommentId="comment.commentId" v-if="replyToggle"></CommentWrite>
      </div>
    </div>
    <div class="main d-flex deleted-comment" v-else>[삭제된 댓글입니다.]</div>
  </div>
</template>

<script>
import CommentWrite from "@/components/comment/CommentWrite.vue";
import {timeAgoFormat} from "@/utils/timeUtils";
import {isEmptyObject} from "@/utils/commonUtils";

export default {
  name: "CommentItem",
  components: {CommentWrite},
  props: {
    comment: Object,
  },
  inject: ['likedCommentIds', 'writerUUID'],
  data() {
    return {
      articleId: this.$route.params.id,
      replyToggle: false,
      isCommentLiked: this.likedCommentIds.has(this.comment?.commentId) || false,
      commentLikeCount: this.comment?.likeCount,
      isDeleted: false,
    }
  },
  computed: {
    userUUID() {
      return this.$store.state.userInfo?.uuid
    },
  },
  methods: {
    isEmptyObject,
    timeAgoFormat,
    clickReply() {
      this.replyToggle = !this.replyToggle
    },
    async deleteComment() {
      const commentId = this.comment?.commentId
      if (!commentId) {
        return
      }
      const isConfirmed = confirm('댓글을 삭제 하시겠습니까?');
      if (!isConfirmed) {
        return true
      }
      const {isError, data} = await this.$boardApi('DELETE', `/api/v2/articles/${this.articleId}/comments/${commentId}`, undefined, true)
      if (isError) {
        alert(data?.message)
        return
      }
      this.isDeleted = true

      setTimeout(() => {
        this.$router.push(this.$route.path)
      }, 300)
    },
    async handleLike() {
      const commentId = this.comment?.commentId
      if (!commentId) {
        return
      }
      const {isError, data} = await this.$boardApi('PUT', `/api/v2/articles/${this.articleId}/comments/${commentId}/like`, undefined, true);
      if (isError) {
        alert(data?.message)
        return
      }
      this.isCommentLiked = true
      this.commentLikeCount += 1
    },
    async handleUndoLike() {
      const commentId = this.comment?.commentId
      if (!commentId) {
        return
      }
      const {isError, data} = await this.$boardApi('DELETE', `/api/v2/articles/${this.articleId}/comments/${commentId}/like`, undefined, true);
      if (isError) {
        alert(data?.message)
        return
      }
      this.isCommentLiked = false
      this.commentLikeCount -= 1
    }
  }
}
</script>

<style lang="scss" scoped>

.dot {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 14px;

  &::after {
    width: .125rem;
    height: .125rem;
    content: "";
    background-color: rgb(0 0 0 / 10%);
  }
}

.main {
  padding: 10px;
  border-bottom: 1px solid rgb(0 0 0 / 5%);

  > img {
    width: 32px;
    height: 32px;
    object-fit: cover;
    margin-right: 3px;
  }
}

.comment-wrapper {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.info {
  font-size: .8rem;
}

.nick-name {
  font-weight: 700;

  .author {
    margin-left: 4px;
    display: flex;
    font-size: .6rem;
    align-items: center;
    color: red;
    padding: 1px;
  }
}

.saved-at {
  color: rgb(0 0 0 / 40%);
}

.comment-content {
  text-align: left;
  font-size: .875rem;
  font-weight: 500;

  img {
    max-width: 225px;
    max-height: 400px;
  }
}

.comment-reply, .comment-delete {
  cursor: pointer;
  userEntity-select: none;
  height: fit-content;
}

.like-count {
  cursor: pointer;
  userEntity-select: none;
}

.deleted-comment {
  font-style: italic;
  font-size: .875rem;
}
</style>
