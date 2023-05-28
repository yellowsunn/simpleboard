<template>
    <section class="pagination-section">
        <ul class="pagination">
            <li class="page-item" :class="[startIdx === 1 ? 'disabled': '']"
                @click="clickPage(startIdx - 1)">
                <a class="page-link">이전</a>
            </li>
            <li class="page-item" :class="[page === pageInfo.page ? 'active' : '' ]" v-for="(page, idx) in pages"
                :key="idx" @click="clickPage(page)">
                <a class="page-link">{{ page }}</a>
            </li>
            <li class="page-item" :class="[endIdx === pageInfo.totalPages ? 'disabled': '']"
                @click="clickPage(endIdx + 1)">
                <a class="page-link">다음</a>
            </li>
        </ul>
    </section>
</template>

<script>
import getPageList from "@/utils/pageUtils";

export default {
  name: "CommentPagination",
  props: {
    pageInfo: null
  },
  data() {
    return {
      pages: null,
      startIdx: null,
      endIdx: null,
    }
  },
  mounted() {
    const {page, totalPages} = this.pageInfo
    const pages = getPageList(page, totalPages, 2)
    this.startIdx = pages[0]
    this.endIdx = pages[pages.length - 1]
    this.pages = pages
  },
  methods: {
    clickPage(page) {
      if (page <= 0 || page > this.pageInfo.totalPages) {
        return
      }

      const articlePage = this.$route.query?.page || 1
      this.$router.push(`/articles/${articlePage}?comment-page=${page}#comments`)
    }
  }
}
</script>

<style scoped>
.pagination-section {
    display: flex;
    justify-content: center;
}

.page-link {
    cursor: pointer;
}
</style>
