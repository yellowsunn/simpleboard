<template>
  <ul class="data">
    <li class="username">{{ userEntity.username }}</li>
    <li class="createdDate">{{ userEntity.createdDate }}</li>
    <li class="role">{{ userEntity.role }}</li>
    <li class="edit">
      <i v-if="isMe" class="fas fa-pen" @click="changePassowrd"></i>
    </li>
    <li class="delete">
      <i v-if="isMe || isValidDelete" class="fas fa-trash-alt" @click="fetchDelete"></i>
    </li>
  </ul>
</template>

<script>
export default {
  props: {
    userEntity: Object,
    websocket: WebSocket
  },
  data() {
    return {
      edit: false,
      data: {
        username: this.userEntity.username,
        role: this.userEntity.role,
      },
    }
  },
  computed: {
    isMe() {
      return this.$store.state.userInfo.id === this.userEntity.id;
    },
    isValidDelete() {
      return this.$store.state.userInfo.role === 'ADMIN' && this.userEntity.role !== 'ADMIN';
    }
  },
  methods: {
    editStatus() {
      if (this.isRoot) return;
      this.data = {
        ...this.data,
        role: this.userEntity.role
      };
      this.edit = !this.edit;
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
            await this.$store.dispatch('DELETE_USER', this.userEntity.id);
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

select {
  font-size: 16px;
  background-color: #fff;
  margin: 0;
  border: 1px solid #aaa;
  border-radius: 1px;
}

.data {
  display: flex;
  padding: $normal-padding;
  border-top: 1px solid #d9d9d9;
  margin: 0;
  .username, .role {
    flex: 1 1 25%;
  }
  .createdDate {
    flex: 1 1 40%;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
    padding-right: 64px;
    input {
      width: 100%;
      height: 100%;
      border: 1px solid #aaaaaa;
      padding-left: 4px;
      &:focus {
        outline: none;
      }
    }
  }
  .edit, .delete {
    flex: 1 1 5%;
    text-align: right;
    padding-right: 8px;
    .fas {
      cursor: pointer;
    }
    .root {
      cursor: not-allowed;
    }
  }

}
</style>
