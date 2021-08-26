<template>
  <section>
    <div class="main_title">
      <div class="text" @click="firstPage">유저게시판</div>
      <i class="fas fa-pen" v-if="isMobile" @click="$router.push('/posts/write')"></i>
    </div>
    <div class="board_table" v-if="!isMobile">
      <ul class="head">
        <li class="no">Id</li>
        <li class="type">구분</li>
        <li class="title">제목</li>
        <li class="writer">글쓴이</li>
        <li class="time">작성시간</li>
        <li class="hit">조회수</li>
      </ul>
      <ul class="data" :class="{'notice' : postData.type === 'NOTICE' }" v-for="postData in boardData.posts" :key="postData.id">
        <li class="no">{{ postData.no }}</li>
        <li class="type">{{ postData.type === 'NOTICE' ? '공지' : '일반' }}</li>
        <li class="title">
          <span class="text" @click="movePostPage(postData.id)">{{ postData.title }}<span class="comment" v-if="postData.commentSize > 0">({{ postData.commentSize }})</span></span>
          <i class="far fa-image" v-if="postData.hasImage"></i>
          
        </li>
        <li class="writer">{{ postData.writer || '[탈퇴한 사용자]' }}</li>
        <li class="time">{{ postData.time }}</li>
        <li class="hit">{{ postData.hit }}</li>
      </ul>
    </div>
    <div class="board_table_mobile" v-else>
      <div class="data" :class="{'notice' : postData.type === 'NOTICE' }" v-for="postData in boardData.posts" @click="movePostPage(postData.id)" :key="postData.id">
        <div class="title_row">
          <span class="text"><span v-if="postData.type === 'NOTICE'" style="padding-right: 4px">[공지]</span>{{ postData.title }}<span class="comment" v-if="postData.commentSize > 0">({{ postData.commentSize }})</span></span>
          <i class="far fa-image" v-if="postData.hasImage"></i>
          
        </div>
        <div class="info_row">
          <span class="writer"><i class="far fa-user"></i> {{ postData.writer || '[탈퇴한 사용자]' }}</span>
          <span class="text_bar"> | </span>
          <span class="time">날짜 {{ postData.time }}</span>
          <span class="text-bar"> | </span>
          <span class="hit">조회 {{ postData.hit }}</span>
        </div>
      </div>
      <div class="pagination">
        <div class="prev" @click="prevPage(5)">
          <i class="fas fa-chevron-left"></i>
        </div>
        <div class="page">
          <span v-for="page in boardData.mobilePageInfo" :class="{'current_page' : boardData.currentPage === page}" @click="changePage(page)">
            {{ page + 1 }}
          </span>
        </div>
        <div class="after" @click="afterPage(5)">
          <i class="fas fa-chevron-right"></i>
        </div>
      </div>
    </div>
    <div class="bottom_btnbox">
      <div :class="{'list_btn' : !isMobile, 'list_btn_mobile' : isMobile}" @click="firstPage">목록보기</div>
      <div class="desktop_pagination" v-if="!isMobile">
        <div class="first" @click="changePage(0)">
          <i class="fas fa-angle-double-left"></i>
        </div>
        <div class="prev" @click="prevPage(10)">
          <i class="fas fa-angle-left"></i>
        </div>
        <div class="page">
          <div v-for="(page, i) in boardData.pageInfo">
            <strong :class="{'current_page' : boardData.currentPage === page}" @click="changePage(page)">{{ page + 1 }}</strong>
            <span v-if="boardData.pageInfo.length - 1 !== i" >|</span>
          </div>
        </div>
        <div class="after" @click="afterPage(10)">
          <i class="fas fa-angle-right"></i>
        </div>
        <div class="end" @click="changePage(boardData.totalPages - 1)">
          <i class="fas fa-angle-double-right"></i>
        </div>
      </div>
      <div class="write_btn" @click="$router.push('/posts/write')">글쓰기</div>
    </div>
    <div class="search_box">
      <div class="inner">
        <select class="sel_option" v-model="search.option">
          <option value="title">제목</option>
          <option value="username">글쓴이</option>
        </select>
        <input ref="search" class="search" placeholder="검색어를 입력하세요" :value="search.text" @input="search.text = $event.target.value" @keyup.enter="searchData">
        <i class="fas fa-search" @click="searchData"></i>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  data() {
    const mql = window.matchMedia("screen and (max-width: 768px)");
    return {
      mql,
      isMobile: mql.matches,
      search: {
        option: 'title',
        text: '',
        isSubmit: false,
        changedText: ''
      }
    }
  },
  computed: {
    boardData() {
      return this.$store.getters.boardData;
    }
  },
  mounted() {
    this.mql.addEventListener("change", e => {
      this.isMobile = e.matches;
    });
    this.$store.dispatch('FETCH_BOARD', { page: 0 });
  },
  methods: {
    firstPage() {
      this.search =  {
        option: 'title', text: '', changedText: '',
        isSubmit: false
      }
      this.$store.dispatch('FETCH_BOARD', { page: 0 });
      window.scrollTo(0, 0);
    },
    changePage(pageIndex) {
      const payload = this.checkSearch(pageIndex);
      this.$store.dispatch('FETCH_BOARD', payload);
      window.scrollTo(0, 0);
    },
    prevPage(size) {
      const pageNumber = parseInt(this.boardData.currentPage / size) * size - 1;
      const payload = this.checkSearch(pageNumber);
      this.$store.dispatch('FETCH_BOARD', payload)
      window.scrollTo(0, 0);
    },
    afterPage(size) {
      let pageNumber = (parseInt(this.boardData.currentPage / size) + 1) * size;
      if (pageNumber >= this.boardData.totalPages) {
        pageNumber = this.boardData.totalPages - 1;
      }
      const payload = this.checkSearch(pageNumber);
      this.$store.dispatch('FETCH_BOARD', payload)
      window.scrollTo(0, 0);
    },
    checkSearch(pageIndex) {
      let payload = {
        [this.search.option] : this.search.isSubmit ? this.search.changedText : '',
        page: pageIndex,
      };
      // 검색 초기화
      if (!this.search.isSubmit) {
        this.search =  {
          option: 'title', text: '',
        }
      }
      return payload;
    },
    async searchData() {
      let payload = {
        [this.search.option] : this.search.text,
        page: 0,
      };
      try {
        await this.$store.dispatch('FETCH_BOARD', payload);
        this.search.isSubmit = true;
        this.search.changedText = this.search.text;
        window.scrollTo(0, 0);
      } catch (error) {
        console.log(error);
      }
    },
    movePostPage(postId) {
      // 게시글로 이동
      this.$router.push(`/posts/${postId}`);
    }
  }
};
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');

$button-color: #1a72e6;
$button-hover-color: #4383ee;
$active-color: #a7daed;
$desktop-hover-color: #f1f1f1;
$border-color: #9ea0ab;
$border-bottom-color: #d9d9d9;

ul {
  margin: 0;
}

input {
  border: none;
  &:focus {
    outline: none;
  }
  width: 100%;
}

select {
  border: none;
  &:focus {
    outline: none;
  }
  background-color: transparent;
}

section {
  font-family: 'Noto Sans KR', sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px;
  .main_title, .board_table, .bottom_btnbox {
    width: 100%;
    max-width: 1200px;
  }
  .main_title {
    font-size: 24px;
    font-weight: 500;
    margin-bottom: 24px;
    .text {
      cursor: pointer;
    }
  }
  .board_table {
    font-size: 14px;
    .head,.data {
      border-bottom: 1px solid $border-bottom-color;
      padding: 10px 0;
      display: flex;
      flex-direction: row;
      li {
        display: flex;
        justify-content: center;
        align-items: center;
      }
      .no {
        flex: 1 1 6%;
      }
      .type {
        flex: 1 1 6%;
      }
      .title {
        flex: 1 1 50%;
        padding-left: 40px;
      }
      .writer {
        justify-content: flex-start;
        flex: 1 1 10%;
      }
      .time {
        flex: 1 1 12%;
      }
      .hit {
        flex: 1 1 7%;
      }
    }
    .head {
      border-top: 1px solid;
    }
    .data {
      &:hover {
        background-color: $desktop-hover-color;
      }
      .title {
        justify-content: flex-start;
        align-items: center;
        overflow: hidden;
        .text {
          width: 90%;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          &:hover {
            text-decoration: underline;
            cursor: pointer;
          }
        }
        .fa-image {
          color: #1A70DC;
          font-size: 17px;
          padding-left: 3px;
        }
        .comment {
          padding-left: 6px;
          font-weight: bold;
          font-size: 13px;
          color: #4376f3;
        }
      }
      .writer {
        padding-left: 3px;
      }
      .deleted {
        color: #999;
      }
    }
    .notice {
      background-color: #e9ecef;
      &:hover {
        background-color: #e2e4e6;
      }
    }
  }
  .bottom_btnbox {
    width: 100%;
    display: flex;
    justify-content: space-between;
    margin-top: 12px;
    .list_btn, .list_btn_mobile, .write_btn {
      border-radius: 4px;
      font-size: 14px;
      padding: 6px 20px;
      cursor: pointer;
    }
    .write_btn {
      color: white;
      background-color: $button-color;
      &:hover {
        background-color: $button-hover-color;
      }
    }
    .list_btn {
      border: 1px solid $border-color;
      &:hover {
        background-color: $desktop-hover-color;
      }
    }
    .list_btn_mobile {
      border: 1px solid $border-color;
      &:active {
        background-color: $active-color;
      }
    }
    .desktop_pagination {
      display: flex;
      align-items: center;
      & > * {
        font-size: 13px;
        margin: 0 4px;
      }
      .first, .prev, .after, .end {
        cursor: pointer;
        font-size: 11px;
        width: 19px;
        height: 19px;
        text-align: center;
        border: 1px solid $border-color;
        border-radius: 4px;
        color: #474747;
        &:hover {
          background-color: $desktop-hover-color;
        }
      }
      .page {
        display: flex;
        strong {
          padding: 0 8px;
          cursor: pointer;
          &:hover {
            background-color: $desktop-hover-color;
          }
        }
        span {
          cursor: default;
        }
        .current_page {
          color: #5795dd;
        }
      }
    }
  }
  .search_box {
    margin-top: 16px;
    padding: 10px 12px;
    .inner {
      font-size: 14px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      border: 1px solid $border-color;
      border-radius: 2px;
      padding: 9px 13px 9px 9px;
      .sel_option {
        margin-right: 7px;
      }
      .fa-search {
        font-size: 22px;
      }
    }
  }
}

@media screen and (max-width: 768px) {
  section {
    align-items: flex-start;
    padding: 0;
    .main_title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 16px;
      margin-bottom: 0;
      padding: 12px;
      box-shadow: 0 1px 3px 0 #bdbdbd;
      .text {
        &:active {
          background-color: $active-color;
        }
      }
      .fa-pen {
        cursor: pointer;
        padding: 3px;
        &:active {
          background-color: $active-color;
          color: #4e555b;
        }
      }
    }
    .board_table_mobile {
      width: 100%;
      .data {
        border-bottom: 1px solid $border-bottom-color;
        &:active {
          background-color: $active-color;
        }
        &:first-child {
          margin-top: 2px;
        }
        .title_row {
          display: flex;
          align-items: center;
          font-size: 14px;
          margin-bottom: 5px;
          overflow: hidden;
          .text {
            cursor: pointer;
            width: 95%;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
          .fa-image {
            color: #1A70DC;
            font-size: 17px;
            padding-left: 3px;
          }
          .comment {
            color: #5894da;
            font-size: 13px;
            font-weight: bold;
            padding-left: 6px;
          }
        }
        .info_row {
          font-size: 12px;
          color: #999;
        }
        padding: 10px;
      }
      .notice {
        background-color: #e9ecef;
      }
      .pagination {
        font-size: 14px;
        padding: 8px 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid $border-bottom-color;
        border-radius: 0;
        .prev, .after {
          cursor: pointer;
          display: flex;
          justify-content: center;
          align-items: center;
          width: 22px;
          height: 22px;
          border: 1px solid #dfe1ee;
          color: #9d9faa;
          &:active {
            background-color: $active-color;
          }
        }
        .page {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 100%;
          span {
            margin: 0 3%;
            width: 20%;
            cursor: pointer;
            text-align: center;
            &:active {
              background-color: $active-color;
            }
          }
          .current_page {
            font-weight: bold;
            color: #5795dd;
          }
        }
      }
    }
    .bottom_btnbox {
      padding: 15px 0 5px;
      margin: 0;
      .list_btn, .list_btn_mobile, .write_btn {
        flex: 1 1 50%;
        text-align: center;
        border-radius: 2px;
      }
      .list_btn, .list_btn_mobile {
        margin-left: 12px;
        margin-right: 6px;
      }
      .write_btn {
        margin-right: 12px;
        margin-left: 6px;
      }
    }
    .search_box {
      margin-top: 0;
      width: 100%;
    }
  }
}
</style>