<template>
  <section>
    <div v-if="isAdmin" class="search_box">
      <i class="fas fa-search"></i>
      <input type="text" v-model="search" v-debounce:500ms="fetchSearch" placeholder="전체 사용자 검색">
    </div>
    <div class="table_box">
      <div v-if="isAdmin" class="title">전체 사용자 <span>{{ usersDto.totalElements }}</span>명</div>
      <div v-else class="title">사용자 정보</div>
      <ul v-if="!isMobile" class="head">
        <li class="username">아이디</li>
        <li class="password">가입날짜</li>
        <li class="role">권한</li>
        <li class="edit">수정</li>
        <li class="delete">삭제</li>
      </ul>
      <template v-if="!isMobile">
        <UserTable v-for="user in usersData" :user="user" :key="user.username"></UserTable>
        <infinite-loading :identifier="infiniteId" @infinite="infiniteHandler" spinner="spiral">
          <span slot="no-more"></span>
          <span slot="no-results"></span>
        </infinite-loading>
      </template>
      <template v-else>
        <UserTableMobile v-for="user in usersData" :user="user" :key="user.username"></UserTableMobile>
        <infinite-loading :identifier="infiniteId" @infinite="infiniteHandler" spinner="spiral">
          <span slot="no-more"></span>
          <span slot="no-results"></span>
        </infinite-loading>
      </template>
    </div>

  </section>
</template>

<script>
import UserTable from '@/components/layout/content/children/UserTable';
import UserTableMobile from '@/components/layout/content/children/UserTableMobile';
export default {
  components: {
    UserTable,
    UserTableMobile
  },
  data() {
    const mql = window.matchMedia("screen and (max-width: 768px)");
    return {
      mql,
      isMobile: mql.matches,
      search: "",
      beforeSearch: "",
      infiniteId: +new Date() // +는 숫자변환
    }
  },
  computed: {
    usersDto() {
      return this.$store.state.usersDto;
    },
    usersData() {
      return !this.usersDto ? null : this.usersDto.content;
    },
    isAdmin() {
      return this.$store.state.userInfo.role === 'ADMIN';
    },
    cursor() {
      return !this.usersData ? null : this.usersData[this.usersData.length - 1].id;
    }
  },
  mounted() {
    this.mql.addEventListener("change", e => {
      this.isMobile = e.matches;
    });
  },
  destroyed() {
    this.$store.state.usersDto = '';
  },
  methods: {
    async fetchSearch() {
      if (this.beforeSearch !== this.search) {
        await this.$store.dispatch('GET_USERS', { search: this.search });
        this.beforeSearch = this.search;
        this.infiniteId += 1;
      }
    },
    async infiniteHandler($state) {
      if (this.usersDto.last) {
        $state.complete();
      } else {
        await this.$store.dispatch('GET_USERS', {
          search: this.search, 
          cursor: this.cursor
        });
        $state.loaded();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
$normal-padding: 16px;
$head-font-color: #5f5f5f;

ul {
  margin: 0;
}

input {
  border: none;
  &:focus {
    outline: none;
  }
}

section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px;
  .search_box {
    width: 100%;
    display: flex;
    align-items: center;
    padding: 16px;
    border-radius: 4px;
    box-shadow: 0 0 3px 1px #d9d9d9;
    background-color: white;
    margin-bottom: 24px;
    .fa-search {
      margin-right: 16px;
    }
    input {
      width: 100%;
    }
  }
  .table_box {
    width: 100%;
    border-radius: 4px;
    box-shadow: 0 0 3px 1px #d9d9d9;
    background-color: white;
    .title {
      padding: $normal-padding;
      span {
        color: #4383ee;
      }
    }
    ul {
      .username, .role {
        flex: 1 1 25%;
      }
      .password {
        flex: 1 1 40%;
      }
      .edit, .delete {
        flex: 1 1 5%;
        text-align: right;
      }
    }
    .head {
      display: flex;
      padding: $normal-padding;
      color: $head-font-color;
    }
  }
}
</style>