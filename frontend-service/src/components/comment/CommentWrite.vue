<template>
    <div class="content-container rounded">
        <div class="comment-input d-flex">
            <div class="comment-content">
                <textarea name="content" placeholder="댓글을 작성해주세요" maxlength="400" v-model="content"></textarea>
                <div class="attached d-flex">
                    <a href="#">
                        <img class="rounded" alt="attached image"
                             src="http://localhost:8000/images/thumbnail/6d3d00c7-365f-4635-9da8-6f6eeeaf55af.jpg">
                        <font-awesome-icon class="close" icon="fa-solid fa-circle-xmark"
                                           style="color: rgb(0 0 0 / 50%)"/>
                    </a>
                </div>
            </div>
            <div class="submit">
                <button class="btn btn-outline-secondary btn-sm"
                        @click="saveCommentOrReply">등록
                </button>
            </div>
        </div>
        <div>
            <div class="attaches d-flex justify-content-start mt-2">
                <div class="comment-image-button rounded-circle">
                    <font-awesome-icon icon="fa-regular fa-image"/>
                </div>
                <input class="comment-image-file" type="file" accept="image/*" style="display: none">
            </div>
        </div>
    </div>
</template>

<script>

import sleep from "@/utils/sleepUtils";

export default {
  name: "CommentWrite",
  data() {
    return {
      articleId: this.$route.params.id,
      content: ""
    }
  },
  props: {
    parentCommentId: Number,
  },
  mounted() {
    console.log(this.$route.path)
  },
  methods: {
    async saveCommentOrReply() {
      const data = this.parentCommentId != null ?
        await this.saveCommentReply() : await this.saveComment()
      if (!data) {
        return
      }

      await sleep(300)
      const page = await this.findCommentPage(data.commentId)
      this.$router.push(`${this.$route.path}?comment-page=${page}#comment-${data.commentId}`)
    },
    async saveComment() {
      const {isError, data} = await this.$boardApi('POST', `/api/v2/articles/${this.articleId}/comments`, {
        content: this.content,
      }, true)
      if (isError) {
        alert(data?.message)
        return
      }
      return data
    },
    async saveCommentReply() {
      const {
        isError,
        data
      } = await this.$boardApi('POST', `/api/v2/articles/${this.articleId}/comments/${this.parentCommentId}`, {
        content: this.content,
      }, true)
      if (isError) {
        alert(data?.message)
        return
      }
      return data
    },
    async findCommentPage(commentId) {
      const {isError, data} = await this.$boardApi('GET', `/api/v2/comments/${commentId}/page`, null);
      if (isError) {
        return 1
      }
      return data
    }
  }
}
</script>

<style lang="scss" scoped>
.form-floating {
  font-size: .875rem;
}

.content-container {
  display: flex;
  flex-direction: column;
  font-size: .875rem;
  background-color: rgb(0 0 0 / 5%);
  padding: 8px;

  .comment-content {
    width: 100%;
  }

  textarea {
    width: 100%;
    height: 60px;
    resize: none;
    border: 0;
    outline: none;
    background-color: transparent;
    padding-right: 10px;
  }
}

.attached {
  a {
    position: relative;
  }

  img {
    width: 50px;
    height: 50px;
    object-fit: cover;
  }

  .close {
    position: absolute;
    top: 3px;
    right: 3px;
    width: 13px;
    height: 13px;
  }
}

.comment-image-button {
  background-color: rgb(0 0 0 / 10%);
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30px;
  height: 30px;
  font-size: .7rem;
}

.submit {
  button {
    width: 80px;
    padding: 8px 16px;
  }
}
</style>
