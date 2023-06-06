<template>
    <div class="mx-auto" style="max-width: 950px">
        <section class="d-flex">
            <div style="font-size: 1.5rem; font-weight: 700; padding: 15px">글쓰기</div>
        </section>
        <section class="mb-4">
            <input type="text" v-model="title" class="form-control" placeholder="제목">
        </section>
        <ArticleWriteEditor class="mb-4" ref="editor" :content="content" v-if="content != null"></ArticleWriteEditor>

        <button type="button" class="btn btn-outline-primary w-100"
                @click="this.articleId ? editArticle() : saveNewArticle()">등록
        </button>
    </div>
</template>

<script>

import ArticleWriteEditor from "@/components/article/ArticleWriteEditor.vue";

export default {
  name: "ArticleWriteView",
  components: {ArticleWriteEditor},
  data() {
    return {
      articleId: this.$route.params.id,
      title: "",
      content: null,
    }
  },
  async mounted() {
    if (!this.articleId) {
      this.content = ""
      return
    }

    const {data} = await this.$boardApi('GET', `/api/v2/articles/${this.articleId}`, null);
    this.title = data?.title
    this.content = data?.body
  },
  methods: {
    async saveNewArticle() {
      const content = this.$refs.editor.editorContent;
      const {isError, data} = await this.$boardApi('POST', '/api/v2/articles', {
        title: this.title,
        body: content,
      }, true);
      if (isError) {
        alert(data?.message)
        return
      }

      this.title = ""
      setTimeout(() => {
        this.$router.push('/articles')
      }, 300)
    },
    async editArticle() {
      const content = this.$refs.editor.editorContent;
      const {isError, data} = await this.$boardApi('PUT', `/api/v2/articles/${this.articleId}`, {
        title: this.title,
        body: content,
      }, true);
      if (isError) {
        alert(data?.message)
        return
      }

      this.title = ""
      setTimeout(() => {
        this.$router.push(`/articles/${this.articleId}`)
      }, 300)
    }
  },

}
</script>

<style>

</style>
