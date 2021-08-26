<template>
  <div class="comment_write">
    <slot></slot> <!-- textarea 위치 -->
    <template v-if="isMobile">
      <div class="write_btn" @click="uploadComment">
        {{ writeButtonText }}
      </div>
    </template>
    <template v-else>
      <div>
        <span class="write_btn_desktop" @click="uploadComment">
          {{ writeButtonText }}
        </span>
      </div>
    </template>
  </div>
</template>

<script>
export default {
  props: {
    writeComment: String,
    mainCommentId: Number,
    index: Number
  },
  data() {
    const mql = window.matchMedia("screen and (max-width: 48rem)");
    const writeButtonText = this.mainCommentId === undefined ? "댓글쓰기" : "답글쓰기";
    return {
      mql,
      isMobile: mql.matches,
      writeButtonText,
    }
  },
  mounted() {
    this.mql.addEventListener("change", e => {
      this.isMobile = e.matches;
    });
  },
  methods: {
    async uploadComment() {
      // 내용이 공백인 경우
      if (this.writeComment === "") {
        alert("내용을 입력하세요");
        return;
      }

      try {
        await this.$store.dispatch('UPLOAD_COMMENT_DATA', {
          content: this.writeComment,
          postId: this.$route.params.postId,
          parentId : this.mainCommentId
        });

        await this.$store.dispatch('GET_COMMENT_DATA', {
          postId: this.$route.params.postId,
          lastCommentIndex: this.index
        });
      } catch (error) {
        this.$router.go(0);
        return;
      }

      if (this.mainCommentId !== undefined) {
        this.$emit('uploadSubComment')
      }
      this.$emit('initWriteComment');

      this.$store.state.infiniteId += 1; // 무한 스크롤 다시 작동
    }
  }
};
</script>

<style lang="scss" scoped>
$button-color: #1a72e6;
$button-hover-color: #4383ee;
$border-bottom: 1px solid #d9d9d9;

.comment_write {
  padding: 0.714em;
  background-color: #f9fafc;

  textarea {
    width: 100%;
    height: 100px;
    border: 1px solid #9ea0ab;
    padding: 0.571em;
    border-radius: 0.286em;
  }

  .write_btn {
    cursor: pointer;
    margin-top: 0.313em;
    width: 100%;
    font-size: 1.143em;
    text-align: center;
    padding: 0.625em 1em;
    background-color: $button-color;
    color: white;
    border-radius: 0.375em;

    &:active {
      background-color: $button-hover-color;
    }
  }
}

// 데스크탑
@media screen and (min-width: 768px) {
  .comment_write {
    div {
      display: flex;
      justify-content: flex-end;
      .write_btn_desktop {
        padding: 6px 20px;
        border: 1px solid $button-color;
        border-radius: 4px;
        margin: 4px 0;
        background-color: $button-color;
        color: white;
        cursor: pointer;
        &:hover {
          background-color: $button-hover-color;
          border-color: $button-hover-color;
        }
      }
    }
  }
}

</style>