<template>
  <div v-click-outside="initToggle">
    <template v-if="comment.createdDate === null && comment.username === null">
      <div class="deleted_comment">[ 삭제된 댓글입니다. ]</div>
    </template>
    <template v-else>
      <div :class="[comment.id == comment.parentId ? 'comment' : 'sud_comment']" @click="changeToggle">
        <div class="text_box">
          <div class="text">{{ comment.content }}</div>
          <i class="far fa-times-circle" @click.stop="deleteComment(comment.id, comment.username)" v-if="currentUser === comment.username"></i>
        </div>
        <div class="info">
          <span class="writer"><i class="far fa-user"></i> {{ comment.username || '[탈퇴한 사용자]' }} </span>
          <span class="text_bar"> | </span>
          <span class="time">{{ comment.createdDate }}</span>
        </div>
      </div>
      <div class="sub_write" v-if="comment.id == comment.parentId && toggle">
        <CommentWrite :index="index" v-on:uploadSubComment="initToggle" :writeComment="subCommentWrite" :mainCommentId="comment.id">
          <textarea :value="subCommentWrite" @input="subCommentWrite = $event.target.value" placeholder="답글을 입력해주세요."></textarea>
        </CommentWrite>
      </div>
    </template>
  </div>
</template>

<script>
import CommentWrite from '@/components/CommentWrite';
import vClickOutside from 'v-click-outside';

export default {
  // vClickOutside 컴포넌트 사용을 위해 필요한 부분
  directives: {
    clickOutside: vClickOutside.directive
  },
  components: {
    CommentWrite
  },
  props: {
    comment: Object,
    index: Number
  },
  data() {
    return {
      toggle: false,
      subCommentWrite: "",
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.userInfo.username;
    }
  },
  methods: {
    initToggle() {
      this.toggle = false;
      this.subCommentWrite = "";
    },
    changeToggle() {
      this.toggle = !this.toggle;
      if (!this.toggle) {
        this.subCommentWrite = "";
      }
    },
    async deleteComment(commentId) {
      const isDelete = confirm("댓글을 지우시겠습니까?");
      if (isDelete) {
        try {
          await this.$store.dispatch('DELETE_COMMENT_DATA', commentId);
          alert('댓글이 삭제되었습니다.');
        } catch (error) {
          alert(error.data.message);
        } finally {
          this.$router.go(0);
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
$border-bottom: 1px solid #d9d9d9;
$active-color: #a7daed;
$gray-background-color: #f3f3f3;
$desktop-hover-color: #f1f1f1;

.deleted_comment {
  font-size: 0.813rem;
  font-style: italic;
  color: #4e555b;
  border-bottom: $border-bottom;
  padding: 10px;
}

.comment {
  font-size: 0.813rem;
  cursor: default;
  padding: 10px;
  border-bottom: $border-bottom;
  &:active {
    background-color: $active-color;
  }
  .info {
    font-size: 0.923em;
    color: #999;
  }
}

.sub_write {
  display: flex;
  font-size: 12px;
  border-bottom: $border-bottom;
  padding-left: 30px;
  background: #f9fafc url("../assets/ico_reply_gray.gif") no-repeat 17px 17px;
  > * {
    width: 100%;
    padding-right: 15px;
  }
}

.sud_comment {
  font-size: 0.813rem;
  cursor: default;
  border-bottom: $border-bottom;
  padding: 10px 10px 10px 35px;
  background: $gray-background-color url("../assets/ico_reply_gray.gif") no-repeat 17px 17px;
  .info {
    font-size: 0.923em;
    color: #999;
  }
}

.comment, .sud_comment {
  .text_box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .fa-times-circle {
      color: #d2d3d5;
      margin-right: -3px;
      padding: 7px;
    }
  }
}

// 데스크탑
@media screen and (min-width: 768px) {
  .comment {
    &:active {
      background-color: transparent;
    }
    &:hover {
      cursor: pointer;
    }
  }
  .fa-times-circle {
    cursor: pointer;
  }
}
</style>