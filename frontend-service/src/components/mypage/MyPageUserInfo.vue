<template>
    <div class="w-100">
        <div class="d-flex m-3 fs-5 fw-bold">회원 정보 변경</div>
        <!-- 썸네일 이미지 변경 -->
        <div class="my-4">
            <img v-if="userInfo?.thumbnail" class="rounded-circle user-thumbnail" :src="userInfo.thumbnail"
                 alt="thumbnail"
                 @click="$refs.fileInput.click()"
                 referrerpolicy="no-referrer-when-downgrade"/>
            <img v-else class="rounded-circle user-thumbnail" src="../../assets/default-thumbnail.svg"
                 alt="default_thumbnail"
                 @click="$refs.fileInput.click()">
            <input ref="fileInput" type="file" accept="image/*" @change="handleThumbnailChange" v-show="false">
        </div>
        <div class="input-edit-group mx-auto">
            <div class="form-floating mb-2">
                <input type="email" class="form-control" :value="userInfo?.email" placeholder="test@example.com"
                       disabled
                       readonly>
                <label>이메일</label>
            </div>
            <div class="form-floating mb-2">
                <input type="text" v-model="nickName" class="form-control input-nickname"
                       placeholder="password">
                <label>닉네임</label>
            </div>
            <button type="button" class="btn btn-secondary btn-lg mb-1 w-100" @click="handleUserInfoChange">정보
                수정
            </button>
        </div>
    </div>
</template>

<script>

export default {
  name: "MyPageUserInfo",
  props: {
    userInfo: {
      type: Object,
    }
  },
  data() {
    return {
      thumbnailWidth: 80,
      thumbnailHeight: 80,
      nickName: '',
    }
  },
  mounted() {
    this.nickName = this.userInfo?.nickName
  },
  methods: {
    async handleThumbnailChange(e) {
      if (!e?.target?.files[0]) {
        return
      }
      const formData = new FormData()
      formData.append('thumbnail', e.target.files[0])
      const {isError, data} = await this.$boardApi('PATCH', '/api/v2/users/my-info/thumbnail', formData, true, {
        'Content-Type': 'multipart/form-data',
      })
      if (isError) {
        alert(data.message)
        return
      }
      this.$emit('update-thumbnail', data)
      this.$store.dispatch('editFinished')
    },
    async handleUserInfoChange() {
      if (this.userInfo?.nickName === this.nickName) {
        return
      }
      const {isError, data} = await this.$boardApi('PUT', '/api/v2/users/my-info', {
        nickName: this.nickName,
      }, true)
      // 실패
      if (isError) {
        alert(data.message)
        this.nickName = this.userInfo?.nickName
        return
      }

      if (data === true) {
        this.$emit('update-user-info', {
          nickName: this.nickName,
        })
        this.$store.dispatch('editFinished')
      }
    },
  }
}
</script>

<style scoped>
.user-thumbnail {
    cursor: pointer;
    object-fit: cover;
    width: 80px;
    height: 80px;
}

.input-edit-group {
    width: 360px;
    display: flex;
    flex-direction: column;
}

.input-nickname {
    background-color: #f9f9f9;
}

.input-nickname:focus {
    background-color: #fff;
}

</style>
