<template>
    <div class="w-100">
        <div class="d-flex m-3 fs-5 fw-bold">회원 정보 변경</div>
        <!-- 썸네일 이미지 변경 -->
        <div class="my-4">
            <img v-if="userInfo?.thumbnail" class="rounded-circle user-thumbnail" :src="resizedThumbnail"
                 alt="thumbnail"
                 @click="$refs.fileInput.click()" :width="thumbnailWidth" :height="thumbnailHeight"
                 referrerpolicy="no-referrer"/>
            <img v-else class="rounded-circle user-thumbnail" src="../../assets/default-thumbnail.svg"
                 alt="default_thumbnail"
                 @click="$refs.fileInput.click()" :width="thumbnailWidth" :height="thumbnailHeight">
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
    <Transition>
        <div class="alert alert-success px-5" role="alert" v-if="isEditFinished">
            정보 수정이 완료되었습니다.
        </div>
    </Transition>
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
      isEditFinished: false,
      thumbnailWidth: 80,
      thumbnailHeight: 80,
      nickName: '',
    }
  },
  mounted() {
    this.nickName = this.userInfo?.nickName
  },
  computed: {
    resizedThumbnail() {
      return `${this.userInfo?.thumbnail}?width=${this.thumbnailWidth}&height=${this.thumbnailHeight}`
    },
  },
  methods: {
    async handleThumbnailChange(e) {
      if (!e?.target?.files[0]) {
        return
      }
      const formData = new FormData()
      formData.append('thumbnail', e.target.files[0])
      const data = await this.$boardApi('PATCH', '/api/users/my-info/thumbnail', formData, true, {
        'Content-Type': 'multipart/form-data',
      })
      console.log(data)
      if (!data?.code) {
        this.$emit('update-thumbnail', data)
      }
    },
    async handleUserInfoChange() {
      if (this.userInfo?.nickName === this.nickName) {
        return
      }
      const response = await this.$boardApi('PUT', '/api/users/my-info', {
        nickName: this.nickName,
      }, true)
      // 실패
      if (response?.code) {
        alert(response?.message)
        this.nickName = this.userInfo?.nickName
        return
      }

      if (response === true) {
        this.$emit('update-user-info', {
          nickName: this.nickName,
        })
        this.isEditFinished = true
        setTimeout(() => {
          this.isEditFinished = false
        }, 1500)
      }
    },
  }
}
</script>

<style scoped>
.user-thumbnail {
    cursor: pointer;
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

.alert-success {
    position: absolute;
    top: 20px;
    right: 20px;
    z-index: 1;
}

.v-enter-active,
.v-leave-active {
    transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
    opacity: 0;
}
</style>
