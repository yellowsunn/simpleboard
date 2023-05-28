<template>
    <div class="comment" v-if="comment">
        <div class="main d-flex">
            <img v-if="comment.userThumbnail" class="rounded-circle" :src="comment.userThumbnail" alt="thumbnail"/>
            <img v-else class="rounded-circle" src="@/assets/default-thumbnail.svg" alt="thumbnail"/>
            <div class="comment-wrapper">
                <div class="info d-flex justify-content-between">
                    <div class="left">
                        <div class="nick-name d-flex">
                            <div class="text">{{ comment.nickName }}</div>
                            <div class="author border border-danger rounded">작성자</div>
                        </div>
                        <div class="d-flex">
                            <div class="saved-at">{{ timeAgoFormat(comment.savedAt) }}</div>
                            <div class="dot"></div>
                            <div class="like-count">
                                <font-awesome-icon icon="fa-regular fa-thumbs-up"/>
                                {{ comment.likeCount }}
                            </div>
                        </div>
                    </div>
                    <div class="right d-flex justify-content-between">
                        <div class="comment-reply p-1" @click="clickReply">{{ replyToggle ? '닫기' : '댓글' }}</div>
                        <div class="comment-delete p-1">삭제</div>
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
    </div>
</template>

<script>
import CommentWrite from "@/components/comment/CommentWrite.vue";
import {timeAgoFormat} from "@/utils/timeUtils";

export default {
  name: "CommentItem",
  components: {CommentWrite},
  props: {
    comment: Object,
  },
  data() {
    return {
      replyToggle: false,
    }
  },
  methods: {
    timeAgoFormat,
    clickReply() {
      this.replyToggle = !this.replyToggle
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
  user-select: none;
  height: fit-content;
}

.like-count {
  cursor: pointer;
  user-select: none;
}
</style>
