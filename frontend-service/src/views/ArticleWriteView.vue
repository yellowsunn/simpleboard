<template>
    <div class="mx-auto" style="max-width: 950px">
        <section class="d-flex">
            <div style="font-size: 1.5rem; font-weight: 700; padding: 15px">글쓰기</div>
        </section>
        <section class="mb-4">
            <input type="text" v-model="title" class="form-control" placeholder="제목">
        </section>
        <ArticleWriteEditor class="mb-4" ref="editor"></ArticleWriteEditor>

        <button type="button" class="btn btn-outline-primary w-100" @click="saveNewArticle">등록</button>
    </div>
</template>

<script>

import ArticleWriteEditor from "@/components/article/ArticleWriteEditor.vue";
import sleep from "@/utils/sleepUtils";

export default {
  name: "ArticleWriteView",
  components: {ArticleWriteEditor},
  data() {
    return {
      title: "",
    }
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

      await sleep(400)
      await this.$router.push('/articles')
    }
  }
}
</script>

<style>

</style>
