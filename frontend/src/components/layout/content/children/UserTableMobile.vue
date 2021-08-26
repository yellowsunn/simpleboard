<template>
  <div class="label_and_data">
    <div class="label" @click="changeToggle">
      <div>
        <i class="fas fa-user-circle"></i>
        <span>{{ user.username }}</span>
      </div>
      <div>
        <i class="fas" :class="toggle ? 'fa-minus' : 'fa-plus'"></i>
      </div>
    </div>
    <VueSlideToggle class="data" :open="toggle">
      <ul>
        <li class="username">
          <span>아이디: </span>
          <div>{{ user.username }}</div>
        </li>
        <li class="password">
          <span>가입날짜: </span>
          <div>{{ user.createdDate }}</div>
        </li>
        <li class="role">
          <span>권한: </span>
          <div>{{ user.role }}</div>
        </li>
      </ul>
      <div class="buttons">
        <div v-if="isMe" class="edit" @click="changePassowrd">수정</div>
        <div v-if="isMe || isValidDelete" class="delete" @click="fetchDelete">삭제</div>
      </div>
    </VueSlideToggle>
  </div>
</template>

<script>
import { VueSlideToggle } from 'vue-slide-toggle';

export default {
  components: {
    VueSlideToggle
  },
  props: {
    user: Object,
  },
  data() {
    return {
      toggle: false,
      edit: false,
      data: {
        username: this.user.username,
        password: "",
        role: this.user.role,
      }
    }
  },
  computed: {
    isMe() {
      return this.$store.state.userInfo.id === this.user.id;
    },
    isValidDelete() {
      return this.$store.state.userInfo.role === 'ADMIN' && this.user.role !== 'ADMIN';
    }
  },
  methods: {
    changeToggle() {
      this.toggle= !this.toggle;
    },
    changePassowrd() {
      this.$router.push('/users/myinfo/change');
    },
    async fetchDelete() {
      if (confirm('정말로 탈퇴하시겠습니까?')) {
        try {
          if (this.isMe) {
            await this.$store.dispatch('DELETE_CURRENT_USER');
            alert('탈퇴가 성공적으로 완료되었습니다.');
            this.$router.push('/');
          } else if (this.isValidDelete) {
            await this.$store.dispatch('DELETE_USER', this.user.id);
            alert('탈퇴가 성공적으로 완료되었습니다.');
            this.$router.go(0);
          } 
          
        } catch (error) {
          alert("실패했습니다.");
        }
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

select {
  font-size: 16px;
  background-color: #fff;
  margin: 0;
  border: 1px solid #aaa;
  border-radius: 1px;
}

.label_and_data {
  display: flex;
  flex-direction: column;
  .label {
    cursor: pointer;
    border-top: 1px solid #d9d9d9;
    display: flex;
    justify-content: space-between;
    padding: $normal-padding;
    .fa-user-circle {
      margin-right: 6px;
    }
  }
  .data {
    ul {
      li {
        display: flex;
        white-space: nowrap;
        padding: 4px 16px;
        span {
          color: $head-font-color;
          flex: 1 1 30%;
        }
        div {
          text-overflow: ellipsis;
          white-space: nowrap;
          overflow: hidden;
          padding-right: 10%;
          flex: 1 1 70%;
          input[type="password"] {
            width: 100%;
            height: 100%;
            border: 1px solid #aaaaaa;
            padding-left: 4px;
            &:focus {
              outline: none;
            }
          }
        }
      }
    }
    .buttons {
      display: flex;
      .edit {
        background-color: #38b97b;
        margin: 8px 8px 16px 16px;
        &:hover {
          background-color: #5ac492;
        }
      }
      .delete {
        background-color: #f14541;
        margin: 8px 16px 16px 8px;
        &:hover {
          background-color: #ee615f;
        }
      }
      .edit, .delete {
        flex: 1 1 50%;
        text-align: center;
        padding: 4px 0;
        border-radius: 4px;
        color: white;
        cursor: pointer;
      }
      .root {
        cursor: not-allowed;
      }
    }
  }
}
</style>